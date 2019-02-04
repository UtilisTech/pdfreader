package com.som_service.pdfreader.textobject;

import org.apache.pdfbox.text.TextPosition;

/**
 *
 * @author Eugene Dementiev
 */
public class TextObject {
	public final TextPosition tp;
	public final double x, y, width, height, space_width, font_size;
	public final String text;
	public final BoundingBox bbox;
	
	public TextObject(TextPosition tp){
		this.tp = tp;
		this.text = tp.getUnicode();
		this.x = tp.getXDirAdj();
		this.y = tp.getYDirAdj();
		this.width = tp.getWidthDirAdj();
		this.height = tp.getHeightDir();
		this.bbox = new BoundingBox(this.x, this.y, this.x + this.width, this.y + this.height);
		this.space_width = tp.getWidthOfSpace();
		this.font_size  = tp.getFontSize();
	}
	
	public String toString(){
		return this.text;
	}
}
