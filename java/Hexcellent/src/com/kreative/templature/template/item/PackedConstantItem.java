package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class PackedConstantItem implements PackedItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final int width;
	private final BigInteger value;
	
	public PackedConstantItem(int typeConstant, String typeString, String name, int width, BigInteger value) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.width = width;
		this.value = value;
	}
	
	public Instance read(Closure closure, BigIntegerBitInputStream in) {
		in.skip(width);
		return new Instance();
	}
	
	public void measure(BigIntegerBitOutputStream out) {
		out.write(value, width);
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements PackedItemInstance {
		public String getLabelText() {
			return null;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return null;
		}
		public void write(BigIntegerBitOutputStream out) {
			out.write(value, width);
		}
	}
}
