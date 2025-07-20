package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Date;
import com.kreative.templature.template.*;

public final class DateTimeItem implements TemplateItem {
	public static final long UNIX_EPOCH = 0L; // January 1, 1970
	public static final long MAC_OS_EPOCH = -2082844800000L; // January 1, 1904
	public static final long APPLEFILE_EPOCH = 946684800000L; // January 1, 2000
	
	public static final int UNIT_SECONDS = 1;
	public static final int UNIT_MILLIS = 1000;
	public static final int UNIT_MICROS = 1000000;
	public static final int UNIT_NANOS = 1000000000;
	
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int length;
	private final BigInteger sign;
	private final boolean signed;
	private final long epoch;
	private final int unit;
	
	public DateTimeItem(int typeConstant, String typeString, String name, boolean le, int width, boolean signed, long epoch, int unit) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.length = (width + 7) / 8;
		this.sign = BigInteger.valueOf(-1).shiftLeft(length * 8);
		this.signed = signed;
		this.epoch = epoch;
		this.unit = unit;
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
	
	public final class Instance implements TemplateItemInstance, ClosureItem, DateTimeDataModel {
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
		public Date getDateValue() {
			BigInteger v = value;
			v = v.multiply(BigInteger.valueOf(1000));
			v = v.divide(BigInteger.valueOf(unit));
			v = v.add(BigInteger.valueOf(epoch));
			return new Date(v.longValue());
		}
		public Date getDateMinValue() {
			BigInteger v = signed ? sign.shiftRight(1) : BigInteger.ZERO;
			v = v.multiply(BigInteger.valueOf(1000));
			v = v.divide(BigInteger.valueOf(unit));
			v = v.add(BigInteger.valueOf(epoch));
			return new Date(v.longValue());
		}
		public Date getDateMaxValue() {
			BigInteger v = signed ? sign.not().shiftRight(1) : sign.not();
			v = v.multiply(BigInteger.valueOf(1000));
			v = v.divide(BigInteger.valueOf(unit));
			v = v.add(BigInteger.valueOf(epoch));
			return new Date(v.longValue());
		}
		public void setDateValue(Date date) {
			BigInteger v = BigInteger.valueOf(date.getTime());
			v = v.subtract(BigInteger.valueOf(epoch));
			v = v.multiply(BigInteger.valueOf(unit));
			v = v.divide(BigInteger.valueOf(1000));
			setValue(v);
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createDateTimeComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			out.writeBigInteger(value, length, le);
		}
	}
}
