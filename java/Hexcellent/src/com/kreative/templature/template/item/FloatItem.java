package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import com.kreative.templature.template.*;

public final class FloatItem implements TemplateItem {
	private static final int SINGLE_EXP_MIN = 1 - FloatFormat.SINGLE.getExponentBias();
	private static final int SINGLE_EXP_MAX = (1 << FloatFormat.SINGLE.getExponentWidth()) + SINGLE_EXP_MIN - 3;
	private static final int SINGLE_MAN_WIDTH = FloatFormat.SINGLE.getMantissaWidth();
	private static final int DOUBLE_EXP_MIN = 1 - FloatFormat.DOUBLE.getExponentBias();
	private static final int DOUBLE_EXP_MAX = (1 << FloatFormat.DOUBLE.getExponentWidth()) + DOUBLE_EXP_MIN - 3;
	private static final int DOUBLE_MAN_WIDTH = FloatFormat.DOUBLE.getMantissaWidth();
	
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final FloatFormat format;
	private final int length;
	private final boolean inFloatRange;
	private final boolean inDoubleRange;
	
	public FloatItem(int typeConstant, String typeString, String name, boolean le, FloatFormat format) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.format = format;
		this.length = (format.getExponentWidth() + format.getMantissaWidth() + 8) / 8;
		int expMin = 1 - format.getExponentBias();
		int expMax = (1 << format.getExponentWidth()) + expMin - 3;
		int manWidth = format.getMantissaWidth();
		this.inFloatRange = (expMin >= SINGLE_EXP_MIN && expMax <= SINGLE_EXP_MAX && manWidth <= SINGLE_MAN_WIDTH);
		this.inDoubleRange = (expMin >= DOUBLE_EXP_MIN && expMax <= DOUBLE_EXP_MAX && manWidth <= DOUBLE_MAN_WIDTH);
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		return new Instance(closure, format.bitsToNumber(in.readBigInteger(length, false, le)));
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, StringDataModel {
		private final Closure closure;
		private Number value;
		private Instance(Closure closure, Number value) {
			this.closure = closure;
			this.value = value;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public Number getValue() {
			return value;
		}
		public void setValue(Number value) {
			if (this.value.equals(value)) return;
			this.value = value;
			closure.fireValueChanged(this);
		}
		public String getStringValue() {
			if (inFloatRange) return Float.toString(value.floatValue());
			if (inDoubleRange) return Double.toString(value.doubleValue());
			return value.toString();
		}
		public void setStringValue(String s) {
			try { setValue(TemplateUtils.parseNumber(s)); }
			catch (NumberFormatException e) {}
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createTextFieldComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			out.writeBigInteger(format.numberToBits(value), length, le);
		}
	}
}
