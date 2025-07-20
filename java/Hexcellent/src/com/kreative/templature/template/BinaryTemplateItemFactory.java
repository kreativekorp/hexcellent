package com.kreative.templature.template;

import java.io.IOException;

public interface BinaryTemplateItemFactory {
	public static final long TI = (1L << 32); // Template Item
	public static final long PI = (1L << 33); // Packed Item
	public static final long DM = (1L << 34); // Decimal Mask (form of ABmn or nmBA where mn is the number of bits)
	public static final long HM = (1L << 35); // Hexadecimal Mask (form of Axyz or zyxA where xyz is the number of bytes)
	public static final long LE = (1L << 36); // Little Endian
	
	public abstract long[] getTypeConstants();
	
	public abstract TemplateItem createTemplateItem(
		BinaryTemplateFactory factory,
		BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it,
		int maskedTypeConstant,
		int maskedValue
	) throws IOException;
	
	public abstract PackedItem createPackedItem(
		BinaryTemplateFactory factory,
		BinaryTemplateInputStream in,
		BinaryTemplateInputStream.Item it,
		int maskedTypeConstant,
		int maskedValue
	) throws IOException;
}
