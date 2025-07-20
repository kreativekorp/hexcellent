package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class PackedBooleanItem implements PackedItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final int width;
	private final BigInteger falseValue;
	private final BigInteger trueValue;
	private final boolean defaultValue;
	
	public PackedBooleanItem(int typeConstant, String typeString, String name, int width, BigInteger falseValue, BigInteger trueValue, boolean defaultValue) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.width = width;
		BigInteger sign = BigInteger.valueOf(-1).shiftLeft(width);
		this.falseValue = falseValue.andNot(sign);
		this.trueValue = trueValue.andNot(sign);
		this.defaultValue = defaultValue;
	}
	
	public Instance read(Closure closure, BigIntegerBitInputStream in) {
		BigInteger iv = in.read(width, false);
		boolean bv = iv.equals(trueValue) ? true : iv.equals(falseValue) ? false : defaultValue;
		return new Instance(closure, bv);
	}
	
	public void measure(BigIntegerBitOutputStream out) {
		out.write(falseValue, width);
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements PackedItemInstance, ClosureItem, BooleanDataModel {
		private final Closure closure;
		private boolean value;
		private Instance(Closure closure, boolean value) {
			this.closure = closure;
			this.value = value;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public Boolean getValue() {
			return value;
		}
		public boolean getBooleanValue() {
			return value;
		}
		public void setBooleanValue(boolean value) {
			if (this.value == value) return;
			this.value = value;
			closure.fireValueChanged(this);
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createBooleanComponent(this);
		}
		public void write(BigIntegerBitOutputStream out) {
			BigInteger iv = value ? trueValue : falseValue;
			out.write(iv, width);
		}
	}
}
