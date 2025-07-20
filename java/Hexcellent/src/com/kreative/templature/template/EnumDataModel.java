package com.kreative.templature.template;

import java.math.BigInteger;
import java.util.Map;

public interface EnumDataModel {
	public abstract BigInteger getEnumValue();
	public abstract Map<BigInteger,String> getEnumValues();
	public abstract void setEnumValue(BigInteger value);
}
