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

/**
 *
 * @author Eugene Dementiev
 */
public class BoundingBox {
	public final double left, top, right, bottom;

	public BoundingBox(double left, double top, double right, double bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public String toString(){
		return left + ", " + top + ", " + right + ", " + bottom;
	}
	
	public boolean equals(Object obj){
		if (!obj.getClass().equals(this.getClass())){
			return false;
		}
		
		BoundingBox bbox = (BoundingBox)obj;
		if (this.left == bbox.left && this.top == bbox.top && this.right == bbox.right && this.bottom == bbox.bottom){
			return true;
		}
		
		return false;
	}
}
