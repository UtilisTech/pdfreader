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
