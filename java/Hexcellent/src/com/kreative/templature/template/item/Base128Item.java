package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class Base128Item implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final boolean signed;
	private final int radix;
	
	public Base128Item(int typeConstant, String typeString, String name, boolean le, boolean signed, int radix) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.signed = signed;
		this.radix = radix;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		if (le) return new Instance(closure, VLFPItem.readLEB128(in, signed));
		else    return new Instance(closure, VLFPItem.readBEB128(in, signed));
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
			return TemplateUtils.formatBigInteger(value, 0, radix);
		}
		public void setStringValue(String s) {
			try { setValue(TemplateUtils.parseBigInteger(s, radix)); }
			catch (NumberFormatException e) {}
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createTextFieldComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			if (le) VLFPItem.writeLEB128(out, value, signed);
			else    VLFPItem.writeBEB128(out, value, signed);
		}
	}
}
