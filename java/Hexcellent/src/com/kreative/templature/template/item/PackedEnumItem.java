package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import com.kreative.templature.template.*;

public final class PackedEnumItem implements PackedItem {
	private final int typeConstant;
	private final int valueConstant;
	private final int endConstant;
	private final String typeString;
	private final String beginName;
	private final String endName;
	private final int width;
	private final TreeMap<BigInteger,String> values;
	
	public PackedEnumItem(
		int typeConstant, int valueConstant, int endConstant,
		String typeString, String beginName, String endName,
		int width, Map<? extends Number, ? extends String> values
	) {
		this.typeConstant = typeConstant;
		this.valueConstant = valueConstant;
		this.endConstant = endConstant;
		this.typeString = typeString;
		this.beginName = beginName;
		this.endName = endName;
		this.width = width;
		this.values = new TreeMap<BigInteger,String>();
		BigInteger sign = BigInteger.valueOf(-1).shiftLeft(width);
		for (Map.Entry<? extends Number, ? extends String> e : values.entrySet()) {
			BigInteger key = toBigInteger(e.getKey()).andNot(sign);
			this.values.put(key, e.getValue());
		}
	}
	
	public Instance read(Closure closure, BigIntegerBitInputStream in) {
		return new Instance(closure, in.read(width, false));
	}
	
	public void measure(BigIntegerBitOutputStream out) {
		out.write(BigInteger.ZERO, width);
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, beginName);
		for (String value : values.values()) {
			out.write(valueConstant, value);
		}
		out.write(endConstant, endName);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(beginName) + " {");
		for (String value : values.values()) {
			out.println(prefix + "\t" + TemplateUtils.escapeName(value) + ",");
		}
		out.println(prefix + "} " + TemplateUtils.escapeName(endName) + ";");
	}
	
	public final class Instance implements PackedItemInstance, ClosureItem, EnumDataModel {
		private final Closure closure;
		private BigInteger value;
		private Instance(Closure closure, BigInteger value) {
			this.closure = closure;
			this.value = value;
			closure.addItem(this);
		}
		public String getKey() {
			return beginName;
		}
		public BigInteger getValue() {
			return value;
		}
		public BigInteger getEnumValue() {
			return value;
		}
		public Map<BigInteger,String> getEnumValues() {
			return values;
		}
		public void setEnumValue(BigInteger value) {
			if (this.value.equals(value)) return;
			this.value = value;
			closure.fireValueChanged(this);
		}
		public String getLabelText() {
			return beginName;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createEnumComponent(this);
		}
		public void write(BigIntegerBitOutputStream out) {
			out.write(value, width);
		}
	}
	
	private static BigInteger toBigInteger(Number n) {
		if (n instanceof BigInteger) return (BigInteger)n;
		if (n instanceof BigDecimal) return ((BigDecimal)n).toBigInteger();
		return BigInteger.valueOf(n.longValue());
	}
}
