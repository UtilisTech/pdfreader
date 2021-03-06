package com.som_service.pdfreader.textobject;

import tech.utilis.common.table.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Eugene Dementiev
 */
public class TextObjectTable {
	
	/**
	 * Column space multiplier
	 */
	public static final int CSM = 3;
	
	private static final TreeMap<Double, Integer> x_to_column = new TreeMap<>();
	
	public final TextObjectLineMap rows;
	public final BoundingBox bounds;
	public final Table<List<TextObjectWord>> table = new Table();
	
	
	public TextObjectTable(TextObjectPage page, BoundingBox bounds){
		this.bounds = bounds;
		
		this.rows = page.rows.clone(new TextObjectLineMapFilter() {
			@Override
			public Double evaluate(TextObject tx) {
				if (tx.x < bounds.left || tx.x > bounds.right || tx.y < bounds.top || tx.y > bounds.bottom){
					return null;
				}
				return tx.y;
			}
		});
		
		List<List<TextObjectWord>> lines_as_words = rows.asWords();
		
		// Determine column candidates x coordinates
		TreeSet<Double> x_cols = new TreeSet<>();
		
		double minimum_tab_space = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < lines_as_words.size(); i++) {
			List<TextObjectWord> line = lines_as_words.get(i);
			Double current_col_x = Double.valueOf(-1);
			
			for(TextObjectWord word: line){
				
				if (word.getSpaceBefore() >= word.left.space_width * CSM || current_col_x == -1){
					// Cell start
					current_col_x = word.x;
					x_cols.add(current_col_x);
					minimum_tab_space = Double.min(minimum_tab_space, word.getSpaceBefore());
				}
			}
		}
		
		// Create mapping of all column x coordinates to column indices
		//TreeMap<Double, Integer> x_to_column = new TreeMap<>();
		List<Double> x_cols_sorted = new ArrayList<>(x_cols);
		x_cols_sorted.addAll(x_to_column.keySet());
		Collections.sort(x_cols_sorted);
		int col = 0;
		for (int i = 0; i < x_cols_sorted.size(); i++) {
			Double col_x = x_cols_sorted.get(i);
			
			if (i > 0 && col_x - x_cols_sorted.get(i-1) >= minimum_tab_space){
				col++;
			}
			
			x_to_column.put(col_x, col);
		}
		//System.out.println(x_to_column);
		
		for (int row_i = 0; row_i < lines_as_words.size(); row_i++) {
			List<TextObjectWord> line = lines_as_words.get(row_i);
			
			ArrayList<TextObjectWord> cell_content = new ArrayList<>();
			
			Double current_col_x = Double.valueOf(-1);
			for(TextObjectWord word: line){
				
				if (word.getSpaceBefore() >= word.left.space_width * CSM){
					// Cell start
					
					if (!cell_content.isEmpty()){
						//System.out.println("Set "+current_col_x+":"+row_i+" = "+cell_content);
						table.set(new Cell(x_to_column.get(current_col_x), row_i, cell_content));
						cell_content = new ArrayList<>();
					}
					current_col_x = word.x;
				}
				cell_content.add(word);
				
			}
			if (!cell_content.isEmpty()){
				table.set(new Cell(x_to_column.get(current_col_x), row_i, cell_content));
			}
		}
		
	}
	
	public static final CellStringifier CELL_STRINGIFIER = new CellStringifier() {
		@Override
		public String cellToString(Cell cell) {
			if (cell.getContent() == null){
				return "";
			}
			return StringUtils.join((List<TextObjectWord>)cell.getContent()," ");
		}
	};

	
	public static void resetXColumnCandidates(){
		x_to_column.clear();
	}
}
