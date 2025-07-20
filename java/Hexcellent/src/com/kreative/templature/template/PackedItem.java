package com.kreative.templature.template;

import java.io.IOException;
import java.io.PrintWriter;

public interface PackedItem {
	public abstract PackedItemInstance read(Closure closure, BigIntegerBitInputStream in);
	public abstract void measure(BigIntegerBitOutputStream out);
	public abstract void write(BinaryTemplateOutputStream out) throws IOException;
	public abstract void print(PrintWriter out, String prefix);
}
