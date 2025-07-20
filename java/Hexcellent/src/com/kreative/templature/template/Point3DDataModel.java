package com.kreative.templature.template;

public interface Point3DDataModel {
	public abstract int[] getPoint3DValues();
	public abstract int[] getPoint3DMinValues();
	public abstract int[] getPoint3DMaxValues();
	public abstract void setPoint3DValues(int x, int y, int z);
}
