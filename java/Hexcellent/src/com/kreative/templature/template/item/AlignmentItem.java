package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class AlignmentItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int mod;
	private final BigInteger fill;
	
	public AlignmentItem(int typeConstant, String typeString, String name, boolean le, int mod, BigInteger fill) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.mod = (mod + 7) / 8;
		this.fill = fill;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		in.align(mod);
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
			out.alignBigInteger(fill, mod, le);
		}
	}
}
