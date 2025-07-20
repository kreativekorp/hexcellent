package com.kreative.templature.template;

public interface HexDataModel {
	public abstract byte[] getHexValue();
	public abstract String getHexEncoding();
	public abstract boolean getHexLittleEndian();
	public abstract void setHexValue(byte[] value);
}
