package com.kreative.templature.template.item;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class VLFPItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final boolean decimal;
	
	public VLFPItem(int typeConstant, String typeString, String name, boolean le, boolean decimal) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.decimal = decimal;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		Number v = readVLFP(in, le);
		if (!decimal) v = v.doubleValue();
		return new Instance(closure, v);
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
			return value.toString();
		}
		public void setStringValue(String s) {
			try {
				Number v = TemplateUtils.parseNumber(s);
				if (!decimal) v = v.doubleValue();
				setValue(v);
			} catch (NumberFormatException e) {}
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createTextFieldComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			writeVLFP(out, value, le);
		}
	}
	
	public static Number readVLFP(InputStream in, boolean le) throws IOException {
		// Read header byte
		final int h = in.read();
		final int header = (h < 0) ? 0 : h;
		final boolean negative  = ((header & 0x80) != 0);
		final boolean decimal   = ((header & 0x40) != 0);
		final boolean negExpSLD = ((header & 0x20) != 0);
		final boolean posExpSLD = ((header & 0x10) != 0);
		final boolean negExpLLD = ((header & 0x08) != 0);
		final boolean posExpLLD = ((header & 0x04) != 0);
		final boolean signaling = ((header & 0x02) != 0);
		final boolean payload   = ((header & 0x01) != 0);
		
		// Read exponent
		int exponent;
		BigInteger mantissa;
		if (negExpSLD && posExpSLD) {
			if (negExpLLD && posExpLLD) {
				// Infinity / Not a Number
				if (payload) {
					return new FloatFormat.NaN(negative, !signaling, (
						decimal ? (le ? readLEB100(in) : readBEB100(in)) :
						(le ? readLEB128(in, false) : readBEB128(in, false))
					));
				} else {
					return new FloatFormat.NaN(negative, signaling, null);
				}
			}
			// Large Leading Digit (8 or 9)
			exponent = readVLFPExp(in, negExpLLD, posExpLLD, le);
			mantissa = BigInteger.valueOf((header >> 1) & 9);
		} else {
			// Small Leading Digit (0 to 7)
			exponent = readVLFPExp(in, negExpSLD, posExpSLD, le);
			mantissa = BigInteger.valueOf((header >> 1) & 7);
		}
		
		// Read mantissa
		if (payload) {
			final byte[] b = readVLA(in);
			final int m = b.length - 1;
			if (le) {
				for (int i = m; i >= 0; i--) {
					BigInteger d = BigInteger.valueOf(decimal ? DPD2BIN7[b[i] & 0x7F] : (b[i] & 0x7F));
					mantissa = decimal ? mantissa.multiply(B100).add(d) : mantissa.shiftLeft(7).or(d);
					exponent -= decimal ? 2 : 7;
				}
			} else {
				for (int i = 0; i <= m; i++) {
					BigInteger d = BigInteger.valueOf(decimal ? DPD2BIN7[b[i] & 0x7F] : (b[i] & 0x7F));
					mantissa = decimal ? mantissa.multiply(B100).add(d) : mantissa.shiftLeft(7).or(d);
					exponent -= decimal ? 2 : 7;
				}
			}
		}
		if (mantissa.equals(BigInteger.ZERO)) {
			return negative ? FloatFormat.Zero.NEGATIVE_ZERO : FloatFormat.Zero.POSITIVE_ZERO;
		}
		
		final BigInteger factor;
		if (decimal) factor = BigInteger.TEN.pow(Math.abs(exponent));
		else factor = BigInteger.ONE.shiftLeft(Math.abs(exponent));
		
		final BigDecimal magnitude;
		if (exponent < 0) magnitude = new BigDecimal(mantissa).divide(new BigDecimal(factor));
		else magnitude = new BigDecimal(mantissa).multiply(new BigDecimal(factor));
		
		BigDecimal result = negative ? magnitude.negate() : magnitude;
		if (result.scale() == 0) result = result.setScale(1);
		return result;
	}
	
	private static int readVLFPExp(InputStream in, boolean neg, boolean pos, boolean le) throws IOException {
		if (neg == pos) return 0;
		BigInteger exp = (le ? readLEB128(in, false) : readBEB128(in, false));
		return neg ? exp.negate().intValueExact() : exp.intValueExact();
	}
	
	public static BigInteger readBEB128(InputStream in, boolean signed) throws IOException {
		final byte[] b = readVLA(in); final int m = b.length - 1; if (m < 0) return BigInteger.ZERO;
		BigInteger v = (signed && ((b[0] & 0x40) != 0)) ? BigInteger.valueOf(-1) : BigInteger.ZERO;
		for (int i = 0; i <= m; i++) v = v.shiftLeft(7).or(BigInteger.valueOf(b[i] & 0x7F));
		return v;
	}
	
	public static BigInteger readLEB128(InputStream in, boolean signed) throws IOException {
		final byte[] b = readVLA(in); final int m = b.length - 1; if (m < 0) return BigInteger.ZERO;
		BigInteger v = (signed && ((b[m] & 0x40) != 0)) ? BigInteger.valueOf(-1) : BigInteger.ZERO;
		for (int i = m; i >= 0; i--) v = v.shiftLeft(7).or(BigInteger.valueOf(b[i] & 0x7F));
		return v;
	}
	
	public static BigInteger readBEB100(InputStream in) throws IOException {
		final byte[] b = readVLA(in); final int m = b.length - 1; BigInteger v = BigInteger.ZERO;
		for (int i = 0; i <= m; i++) v = v.multiply(B100).add(BigInteger.valueOf(DPD2BIN7[b[i] & 0x7F]));
		return v;
	}
	
	public static BigInteger readLEB100(InputStream in) throws IOException {
		final byte[] b = readVLA(in); final int m = b.length - 1; BigInteger v = BigInteger.ZERO;
		for (int i = m; i >= 0; i--) v = v.multiply(B100).add(BigInteger.valueOf(DPD2BIN7[b[i] & 0x7F]));
		return v;
	}
	
	private static byte[] readVLA(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (;;) {
			int b = in.read(); if (b < 0) break;
			out.write(b); if (b < 128) break;
		}
		return out.toByteArray();
	}
	
	public static void writeVLFP(OutputStream out, Number value, boolean le) throws IOException {
		if (value instanceof FloatFormat.NaN) { writeVLFPNaN(out, (FloatFormat.NaN)value, le); return; }
		if (value instanceof FloatFormat.Zero) { writeVLFPZero(out, (FloatFormat.Zero)value); return; }
		if (value instanceof BigDecimal) { writeVLFPBigDecimal(out, (BigDecimal)value, le); return; }
		if (value instanceof BigInteger) { writeVLFPBigInteger(out, (BigInteger)value, le); return; }
		if (value instanceof Long) { writeVLFPLong(out, value.longValue(), le); return; }
		if (value instanceof Integer) { writeVLFPInt(out, value.intValue(), le); return; }
		if (value instanceof Short) { writeVLFPInt(out, value.intValue(), le); return; }
		if (value instanceof Byte) { writeVLFPInt(out, value.intValue(), le); return; }
		if (value instanceof Float) { writeVLFPFloat(out, value.floatValue(), le); return; }
		if (value instanceof Double) { writeVLFPDouble(out, value.doubleValue(), le); return; }
		throw new UnsupportedOperationException(
			"unknown subclass of Number; please convert to a known subclass " +
			"(Byte, Short, Integer, Long, Float, Double, BigInteger, BigDecimal, " +
			"FloatFormat.Zero, or FloatFormat.NaN)"
		);
	}
	
	private static void writeVLFPNaN(OutputStream out, FloatFormat.NaN nan, boolean le) throws IOException {
		writeVLFPNaN(out, nan.isNegative(), false, nan.isQuiet(), nan.getPattern(), le);
	}
	
	private static void writeVLFPNaN(OutputStream out, boolean negative, boolean decimal, boolean quiet, BigInteger payload, boolean le) throws IOException {
		int sign = payload.signum();
		if (sign < 0) { negative = !negative; payload = payload.negate(); }
		
		byte header = 0x3C;
		if (negative) header |= 0x80;
		if (decimal) header |= 0x40;
		if (sign == 0) {
			if (quiet) header |= 0x02;
			out.write(header);
		} else {
			if (!quiet) header |= 0x02;
			out.write(header | 0x01);
			if (decimal) { if (le) writeLEB100(out, payload); else writeBEB100(out, payload); }
			else { if (le) writeLEB128(out, payload, false); else writeBEB128(out, payload, false); }
		}
	}
	
	private static void writeVLFPZero(OutputStream out, FloatFormat.Zero zero) throws IOException {
		writeVLFPZero(out, zero.isNegative(), false);
	}
	
	private static void writeVLFPZero(OutputStream out, boolean negative, boolean decimal) throws IOException {
		byte header = 0x00;
		if (negative) header |= 0x80;
		if (decimal) header |= 0x40;
		out.write(header);
	}
	
	private static void writeVLFPBigDecimal(OutputStream out, BigDecimal value, boolean le) throws IOException {
		writeVLFPFinite(out, false, true, -value.scale(), value.unscaledValue(), le);
	}
	
	private static void writeVLFPBigInteger(OutputStream out, BigInteger value, boolean le) throws IOException {
		writeVLFPFinite(out, false, false, 0, value, le);
	}
	
	private static void writeVLFPLong(OutputStream out, long value, boolean le) throws IOException {
		writeVLFPFinite(out, false, false, 0, BigInteger.valueOf(value), le);
	}
	
	private static void writeVLFPInt(OutputStream out, int value, boolean le) throws IOException {
		writeVLFPFinite(out, false, false, 0, BigInteger.valueOf(value), le);
	}
	
	private static void writeVLFPFloat(OutputStream out, float value, boolean le) throws IOException {
		int fbits = Float.floatToRawIntBits(value);
		boolean negative = (fbits < 0);
		int exponent = (fbits >> 23) & 0xFF;
		if (exponent == 0xFF) {
			boolean quiet = ((fbits & (1 << 22)) != 0);
			int payload = fbits & ((1 << 22) - 1);
			writeVLFPNaN(out, negative, false, quiet, BigInteger.valueOf(payload), le);
			return;
		}
		int mantissa = fbits & ((1 << 23) - 1);
		if (exponent == 0) {
			if (mantissa == 0) { writeVLFPZero(out, negative, false); return; }
			exponent = 1 - (127 + 23);
		} else {
			mantissa |= (1 << 23);
			exponent -= (127 + 23);
		}
		writeVLFPFinite(out, negative, false, exponent, BigInteger.valueOf(mantissa), le);
	}
	
	private static void writeVLFPDouble(OutputStream out, double value, boolean le) throws IOException {
		long dbits = Double.doubleToRawLongBits(value);
		boolean negative = (dbits < 0);
		int exponent = (int)(dbits >> 52) & 0x7FF;
		if (exponent == 0x7FF) {
			boolean quiet = ((dbits & (1L << 51)) != 0);
			long payload = dbits & ((1L << 51) - 1);
			writeVLFPNaN(out, negative, false, quiet, BigInteger.valueOf(payload), le);
			return;
		}
		long mantissa = dbits & ((1L << 52) - 1);
		if (exponent == 0) {
			if (mantissa == 0) { writeVLFPZero(out, negative, false); return; }
			exponent = 1 - (1023 + 52);
		} else {
			mantissa |= (1L << 52);
			exponent -= (1023 + 52);
		}
		writeVLFPFinite(out, negative, false, exponent, BigInteger.valueOf(mantissa), le);
	}
	
	private static void writeVLFPFinite(OutputStream out, boolean negative, boolean decimal, int exponent, BigInteger mantissa, boolean le) throws IOException {
		int sign = mantissa.signum();
		if (sign == 0) { writeVLFPZero(out, negative, decimal); return; }
		if (sign < 0) { negative = !negative; mantissa = mantissa.negate(); }
		
		byte[] payload;
		if (decimal) {
			// Truncate trailing zero digits
			BigInteger[] dm = mantissa.divideAndRemainder(BigInteger.TEN);
			while (dm[1].signum() == 0) {
				exponent++;
				mantissa = dm[0];
				dm = dm[0].divideAndRemainder(BigInteger.TEN);
			}
			// Align to a multiple of two digits (not including the leading digit)
			if ((mantissa.toString().length() & 1) != 1) {
				exponent--;
				mantissa = mantissa.multiply(BigInteger.TEN);
			}
			// Create the array of continuation bytes
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			while (mantissa.compareTo(BigInteger.TEN) >= 0) {
				dm = mantissa.divideAndRemainder(B100);
				bs.write(BIN7DPD2[dm[1].intValue()]);
				mantissa = dm[0];
				exponent += 2;
			}
			payload = bs.toByteArray();
		} else {
			// Truncate trailing zero bits
			while (!mantissa.testBit(0)) {
				exponent++;
				mantissa = mantissa.shiftRight(1);
			}
			// Align to a multiple of seven bits (not including the leading one bit)
			while ((mantissa.bitLength() % 7) != 1) {
				exponent--;
				mantissa = mantissa.shiftLeft(1);
			}
			// Create the array of continuation bytes
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			while (mantissa.bitLength() > 1) {
				bs.write(mantissa.byteValue());
				mantissa = mantissa.shiftRight(7);
				exponent += 7;
			}
			payload = bs.toByteArray();
		}
		
		writeVLFPHeader(out, negative, decimal, exponent, mantissa.intValue(), payload.length > 0);
		if (exponent != 0) writeVLFPExponent(out, exponent, le);
		if (payload.length > 0) writeVLA(out, payload, !le);
	}
	
	private static void writeVLFPHeader(OutputStream out, boolean negative, boolean decimal, int exponent, int leadingDigit, boolean payload) throws IOException {
		byte header = 0x00;
		if (negative) header |= 0x80;
		if (decimal) header |= 0x40;
		if (leadingDigit >= 8) {
			header |= 0x30;
			if (exponent < 0) header |= 0x08;
			if (exponent > 0) header |= 0x04;
		} else {
			if (exponent < 0) header |= 0x20;
			if (exponent > 0) header |= 0x10;
		}
		header |= leadingDigit << 1;
		if (payload) header |= 0x01;
		out.write(header);
	}
	
	private static void writeVLFPExponent(OutputStream out, int exponent, boolean le) throws IOException {
		BigInteger exp = BigInteger.valueOf(exponent);
		if (le) writeLEB128(out, exp.abs(), false);
		else writeBEB128(out, exp.abs(), false);
	}
	
	public static void writeBEB128(OutputStream out, BigInteger v, boolean signed) throws IOException {
		writeVLA(out, unzipB128(v, signed), true);
	}
	
	public static void writeLEB128(OutputStream out, BigInteger v, boolean signed) throws IOException {
		writeVLA(out, unzipB128(v, signed), false);
	}
	
	private static byte[] unzipB128(BigInteger v, boolean signed) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int n = v.bitLength(); if (n == 0) n++; if (signed) n++;
		while (n > 0) { out.write(v.byteValue()); v = v.shiftRight(7); n -= 7; }
		return out.toByteArray();
	}
	
	public static void writeBEB100(OutputStream out, BigInteger v) throws IOException {
		writeVLA(out, unzipB100(v), true);
	}
	
	public static void writeLEB100(OutputStream out, BigInteger v) throws IOException {
		writeVLA(out, unzipB100(v), false);
	}
	
	private static byte[] unzipB100(BigInteger v) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BigInteger[] dm = v.abs().divideAndRemainder(B100);
		out.write(BIN7DPD2[dm[1].intValue()]);
		while (dm[0].signum() != 0) {
			dm = dm[0].divideAndRemainder(B100);
			out.write(BIN7DPD2[dm[1].intValue()]);
		}
		return out.toByteArray();
	}
	
	private static void writeVLA(OutputStream out, byte[] b, boolean be) throws IOException {
		final int m = b.length - 1;
		if (be) { for (int i = m; i >= 0; i--) out.write((i == 0) ? (b[i] & 0x7F) : (b[i] | 0x80)); }
		else    { for (int i = 0; i <= m; i++) out.write((i == m) ? (b[i] & 0x7F) : (b[i] | 0x80)); }
	}
	
	private static final BigInteger B100 = BigInteger.valueOf(100);
	
	private static final int[] DPD2BIN7 = {
		 0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 80, 81, 88, 89, 88, 89,
		10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 90, 91, 98, 99, 98, 99,
		20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 82, 83, 88, 89, 88, 89,
		30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 92, 93, 98, 99, 98, 99,
		40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 84, 85, 88, 89, 88, 89,
		50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 94, 95, 98, 99, 98, 99,
		60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 86, 87, 88, 89, 88, 89,
		70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 96, 97, 98, 99, 98, 99,
	};
	
	private static final int[] BIN7DPD2 = {
		0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
		0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19,
		0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29,
		0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39,
		0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49,
		0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59,
		0x60, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69,
		0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79,
		0x0A, 0x0B, 0x2A, 0x2B, 0x4A, 0x4B, 0x6A, 0x6B, 0x4E, 0x4F,
		0x1A, 0x1B, 0x3A, 0x3B, 0x5A, 0x5B, 0x7A, 0x7B, 0x5E, 0x5F,
	};
}
