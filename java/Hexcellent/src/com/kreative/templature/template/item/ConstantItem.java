package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class ConstantItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int length;
	private final BigInteger value;
	
	public ConstantItem(int typeConstant, String typeString, String name, boolean le, int width, BigInteger value) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.length = (width + 7) / 8;
		this.value = value;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		in.safeSkip(length);
		return new Instance();
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance {
		public String getLabelText() {
			return null;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return null;
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			out.writeBigInteger(value, length, le);
		}
	}
}
