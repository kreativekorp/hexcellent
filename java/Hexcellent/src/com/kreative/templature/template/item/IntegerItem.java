package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class IntegerItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int length;
	private final boolean signed;
	private final int radix;
	
	public IntegerItem(int typeConstant, String typeString, String name, boolean le, int width, boolean signed, int radix) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.length = (width + 7) / 8;
		this.signed = signed;
		this.radix = radix;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		return new Instance(closure, in.readBigInteger(length, signed, le));
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, StringDataModel {
		private final Closure closure;
		private BigInteger value;
		private Instance(Closure closure, BigInteger value) {
			this.closure = closure;
			this.value = value;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public BigInteger getValue() {
			return value;
		}
		public void setValue(BigInteger value) {
			if (this.value.equals(value)) return;
			this.value = value;
			closure.fireValueChanged(this);
		}
		public String getStringValue() {
			return TemplateUtils.formatBigInteger(value, length * 8, radix);
		}
		public void setStringValue(String s) {
			try { setValue(TemplateUtils.parseBigInteger(s, radix)); }
			catch (NumberFormatException e) {}
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			BigInteger max = BigInteger.valueOf(-1).shiftLeft(length * 8).not();
			int columns = TemplateUtils.formatBigInteger(max, length * 8, radix).length() + 2;
			return factory.createTextFieldComponent(this, columns);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			out.writeBigInteger(value, length, le);
		}
	}
}
