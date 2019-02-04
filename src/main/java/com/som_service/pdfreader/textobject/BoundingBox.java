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
