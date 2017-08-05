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
