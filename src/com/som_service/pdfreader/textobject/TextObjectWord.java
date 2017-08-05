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

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Eugene Dementiev
 */

public class TextObjectWord {
	public final StringBuffer text = new StringBuffer();
	public final List<TextObject> letters;
	public final double x, y, width, height;
	public final TextObject left, top, right, bottom;
	public final BoundingBox bbox;
	
	private double space_before = 0;
	

	public TextObjectWord(List<TextObject> letters) {
		Collections.sort(letters, TextObjectLineMap.COMPARATOR_X);
		this.letters = letters;
		
		TextObject left = null;
		TextObject top = null;
		TextObject right = null;
		TextObject bottom = null;
		
		for(TextObject tx: letters){
			if (left == null){
				left = tx;
			}
			else {
				if (tx.x < left.x){
					left = tx;
				}
			}
			
			if (top == null){
				top = tx;
			}
			else {
				if (tx.y < top.y){
					top = tx;
				}
			}
			
			if (right == null){
				right = tx;
			}
			else {
				if (tx.x + tx.width > right.x + right.width){
					right = tx;
				}
			}
			
			if (bottom == null){
				bottom = tx;
			}
			else {
				if (tx.y + tx.height > bottom.y + bottom.height){
					bottom = tx;
				}
			}
			
			text.append(tx.text);
		}
		
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		
		this.x = left.x;
		this.y = top.y;
		this.width = right.x + right.width - left.x;
		this.height = bottom.y + bottom.height - top.y;
		
		this.bbox = new BoundingBox(x, y, x + width, y + height);
	}
	
	@Override
	public String toString(){
		return text.toString();
	}

	public double getSpaceBefore() {
		return space_before;
	}

	public void setSpaceBefore(double space_before) {
		this.space_before = space_before;
	}
	
	
}
