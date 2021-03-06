package com.som_service.pdfreader.textobject;

import tech.utilis.common.lang.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Eugene Dementiev
 */
public class TextObjectLineMap {
	
	public final Map<Double, List<TextObject>> map;
	public final List<TextObject> list;
	
	private double spaceScaleMultiplier = 10.1;
	
	public TextObjectLineMap(){
		list = new ArrayList<>();
		map = new TreeMap<>();
	}
	
	public TextObjectLineMap(TreeMap<Double, List<TextObject>> map){
		this.map = map;
		this.list = new ArrayList<>();
		for(Double key: map.keySet()){
			List line = map.get(key);
			line.stream().forEach(tx -> this.list.add((TextObject)tx));
		}
	}

	public List<TextObject> get(Double key){
		return map.get(key);
	}
	
	public void put(Double key, TextObject tx){
		if (!map.containsKey(key)){
			map.put(key, new ArrayList<>());
		}
		map.get(key).add(tx);
		list.add(tx);
	}
	
	
	public static final Comparator<TextObject> COMPARATOR_X = new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			double o1x = ((TextObject)o1).x;
			double o2x = ((TextObject)o2).x;
			/*
			if (o1x > o2x){
				return 1;
			}
			if (o1x < o2x){
				return -1;
			}
			return 0;
			*/
			return (int)((o1x-o2x)*10000);
		}
	};
	
	public static final Comparator<TextObject> COMPARATOR_Y = new Comparator() {
		@Override
		public int compare(Object o1, Object o2) {
			double o1y = ((TextObject)o1).y;
			double o2y = ((TextObject)o2).y;
			if (o1y > o2y){
				return 1;
			}
			if (o1y < o2y){
				return -1;
			}
			return 0;
		}
	};
	
	public void sortBy(Comparator<TextObject> comparator){
		for(Double key: map.keySet()){
			Collections.sort(map.get(key), comparator);
		}
	}
	
	public List<List<TextObjectWord>> asWords(){
		
		List<List<TextObjectWord>> lines = new ArrayList<>();
		
		for(Double index: map.keySet()){
			List<TextObjectWord> words = new ArrayList<>();
			List<TextObject> word_members = new ArrayList<>();
			double word_prefix_distance = -1;
			double last_end = 0;
			double last_pos_diff = 0;
			for(TextObject tx: map.get(index)){
				if(StringUtils.isVisibleWhiteSpace(tx.text.charAt(0))){
					// Do not count white spaces
					continue;
				}
				double pos_diff = tx.x - last_end;
				
				// Distance between last token's bbox in word members to this token's bbox greater than a space (scaled and multiplied)
				boolean condition1 = pos_diff > tx.space_width * tx.tp.getXScale() / spaceScaleMultiplier;
				
				// wtf?
				boolean condition2 = last_pos_diff <= 0 && pos_diff > 0;
				condition2 = false;
				
				if (condition1 || condition2){
					if (!word_members.isEmpty()){
						TextObjectWord word = new TextObjectWord(word_members);
						word.setSpaceBefore(word_prefix_distance);
						words.add(word);
						word_members = new ArrayList<>();
					}
				}
				
				word_members.add(tx);
				
				if (word_prefix_distance < 0){
					// Set prefix distance for the very first word
					word_prefix_distance = Double.max(0, word_members.get(0).x);
				}
				else if(word_members.size() == 1){
					// Set prefix distance for consecutive words 
					word_prefix_distance = tx.x - last_end;
				}
				
				last_end = tx.bbox.right;
				last_pos_diff = pos_diff;
				
			}
			
			if (word_members.isEmpty()){
				continue;
			}
			
			TextObjectWord word = new TextObjectWord(word_members);
			word.setSpaceBefore(word_prefix_distance);
			words.add(word);
			
			lines.add(words);
			
		}
		
		return lines;
	}
	
	public TextObjectLineMap clone(TextObjectLineMapFilter txf){
		
		TextObjectLineMap txolm = new TextObjectLineMap();
		
		list.stream().forEach(tx -> {
			Double evaluation_result = txf.evaluate(tx);
			if(evaluation_result != null){
				txolm.put(evaluation_result, tx);
			}
		});
		
		return txolm;
	}

	/**
	 * Gets the value of space scale multiplier
	 * @return spaceScaleMultiplier
	 */
	public double getSpaceScaleMultiplier() {
		return spaceScaleMultiplier;
	}

	/**
	 * Sets space scale multiplier, which impacts how symbols are grouped into words<br/>
	 * Larger number decreases space between symbols required to qualify as a word separator<br/>
	 * Smaller number increases space between symbols required to qualify as a word separator
	 * @param spaceScaleMultiplier 
	 */
	public void setSpaceScaleMultiplier(double spaceScaleMultiplier) {
		this.spaceScaleMultiplier = spaceScaleMultiplier;
	}
	
	
}
