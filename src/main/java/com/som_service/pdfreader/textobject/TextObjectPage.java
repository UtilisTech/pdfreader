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
