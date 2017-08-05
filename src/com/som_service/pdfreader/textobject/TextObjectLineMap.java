/*
 * MIT License
 * 
 * Copyright (c) 2017 Eugene Dementiev
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.som_service.pdfreader.textobject;

import com.som_service.extra.utils.DebugUtils;
import com.som_service.hsbc.Environment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Eugene Dementiev
 */
public class TextObjectLineMap {
	public final Map<Double, List<TextObject>> map;
	public final List<TextObject> list;
	
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
		
		if (Environment.DEBUG){
			System.out.println(StringUtils.join(new String[]{
				"Char",
				"New Word",
				"Current End",
				"X",
				"X-C",
				"Width",
				"Space",
				"ScaleX"
			}, "\t"));
		}
		
		for(Double index: map.keySet()){
			List<TextObjectWord> words = new ArrayList<>();
			List<TextObject> word_members = new ArrayList<>();
			double word_prefix_distance = -1;
			double last_end = 0;
			double last_pos_diff = 0;
			for(TextObject tx: map.get(index)){
				double pos_diff = tx.x - last_end;
				boolean condition1 = pos_diff > tx.space_width * tx.tp.getXScale() / 10.1;
				boolean condition2 = last_pos_diff <= 0 && pos_diff > 0;
				
				if (Environment.DEBUG){
					System.out.println(StringUtils.join(new Object[]{
						tx.text,
						condition1||condition2,
						last_end,
						tx.x,
						tx.x - last_end,
						tx.width,
						tx.space_width,
						tx.tp.getXScale()
					}, "\t"));
				}
				
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
				
				last_end = tx.x + tx.width;
				last_pos_diff = pos_diff;
				
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
}
