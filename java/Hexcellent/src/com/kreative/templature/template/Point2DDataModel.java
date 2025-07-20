package com.kreative.templature.template;

public interface Point2DDataModel {
	public abstract int[] getPoint2DValues();
	public abstract int[] getPoint2DMinValues();
	public abstract int[] getPoint2DMaxValues();
	public abstract void setPoint2DValues(int x, int y);
}
