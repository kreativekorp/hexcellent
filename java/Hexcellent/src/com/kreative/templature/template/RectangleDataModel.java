package com.kreative.templature.template;

public interface RectangleDataModel {
	public abstract int[] getRectangleValues();
	public abstract int[] getRectangleMinValues();
	public abstract int[] getRectangleMaxValues();
	public abstract void setRectangleValues(int l, int t, int r, int b);
}
