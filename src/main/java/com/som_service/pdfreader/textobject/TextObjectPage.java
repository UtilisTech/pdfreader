package com.som_service.pdfreader.textobject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Eugene Dementiev
 */
public class TextObjectPage {
	
	public final List<TextObject> text_objects = new ArrayList<>();
	public final TextObjectLineMap rows = new TextObjectLineMap();
	
	public void add(TextObject tx){
		if (text_objects.size() > 0){
			TextObject last = text_objects.get(text_objects.size()-1);
			if (last.bbox.equals(tx.bbox)){
				// Do not add duplicate characters that may be found in some badly formed PDFs
				return;
			}
		}
		text_objects.add(tx);
		rows.put(tx.y, tx);
	}
	
	public void clear(){
		text_objects.clear();
		rows.map.clear();
	}
	
	public void sort(){
		rows.sortBy(TextObjectLineMap.COMPARATOR_X);
		Collections.sort(text_objects, TextObjectLineMap.COMPARATOR_X);
	}
}
