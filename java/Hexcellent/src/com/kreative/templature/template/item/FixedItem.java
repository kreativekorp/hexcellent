package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class FixedItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int length;
	private final boolean signed;
	private final BigDecimal denominator;
	
	public FixedItem(int typeConstant, String typeString, String name, boolean le, int width, boolean signed, int shift) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.length = (width + 7) / 8;
		this.signed = signed;
		this.denominator = new BigDecimal(BigInteger.ONE.shiftLeft(shift));
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		return new Instance(closure, new BigDecimal(in.readBigInteger(length, signed, le)).divide(denominator));
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, StringDataModel {
		private final Closure closure;
		private BigDecimal value;
		private Instance(Closure closure, BigDecimal value) {
			this.closure = closure;
			this.value = value;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public BigDecimal getValue() {
			return value;
		}
		public void setValue(BigDecimal value) {
			if (this.value.equals(value)) return;
			this.value = value;
			closure.fireValueChanged(this);
		}
		public String getStringValue() {
			return value.toString();
		}
		public void setStringValue(String s) {
			try { setValue(TemplateUtils.parseBigDecimal(s)); }
			catch (NumberFormatException e) {}
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createTextFieldComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			out.writeBigInteger(value.multiply(denominator).toBigInteger(), length, le);
		}
	}
}
