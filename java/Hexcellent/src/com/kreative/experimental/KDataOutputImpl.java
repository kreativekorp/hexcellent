package com.kreative.experimental;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.kreative.hexcellent.buffer.FloatFormat;
import com.kreative.hexcellent.buffer.FloatFormat.NaN;
import com.kreative.hexcellent.buffer.FloatFormat.Zero;

public final class KDataOutputImpl implements DataOutput, KDataOutput {
	private final DataOutput out;
	public KDataOutputImpl(DataOutput out) { this.out = out; }
	
	@Override public final void write(int v) throws IOException { out.write(v); }
	@Override public final void write(byte[] b) throws IOException { out.write(b); }
	@Override public final void write(byte[] b, int off, int len) throws IOException { out.write(b, off, len); }
	@Override public final void writeBoolean(boolean v) throws IOException { out.writeBoolean(v); }
	@Override public final void writeByte(int v) throws IOException { out.writeByte(v); }
	@Override public final void writeBytes(String v) throws IOException { out.writeBytes(v); }
	@Override public final void writeChar(int v) throws IOException { out.writeChar(v); }
	@Override public final void writeChars(String v) throws IOException { out.writeChars(v); }
	@Override public final void writeDouble(double v) throws IOException { out.writeDouble(v); }
	@Override public final void writeFloat(float v) throws IOException { out.writeFloat(v); }
	@Override public final void writeInt(int v) throws IOException { out.writeInt(v); }
	@Override public final void writeLong(long v) throws IOException { out.writeLong(v); }
	@Override public final void writeShort(int v) throws IOException { out.writeShort(v); }
	
	public final void writeCharLE(int v) throws IOException {
		out.writeChar(Character.reverseBytes((char)v));
	}
	public final void writeCharsLE(String v) throws IOException {
		for (char ch : v.toCharArray()) out.writeChar(Character.reverseBytes(ch));
	}
	public final void writeDoubleLE(double v) throws IOException {
		out.writeLong(Long.reverseBytes(Double.doubleToRawLongBits(v)));
	}
	public final void writeFloatLE(float v) throws IOException {
		out.writeInt(Integer.reverseBytes(Float.floatToRawIntBits(v)));
	}
	public final void writeIntLE(int v) throws IOException {
		out.writeInt(Integer.reverseBytes(v));
	}
	public final void writeLongLE(long v) throws IOException {
		out.writeLong(Long.reverseBytes(v));
	}
	public final void writeShortLE(int v) throws IOException {
		out.writeShort(Short.reverseBytes((short)v));
	}
	
	@Override
	public final void writeUTF(String v) throws IOException {
		byte[] buf = v.getBytes("UTF-8");
		if (buf.length >= 65536) throw new UTFDataFormatException();
		out.writeShort(buf.length);
		out.write(buf);
	}
	public final void writeUTFLE(String v) throws IOException {
		byte[] buf = v.getBytes("UTF-8");
		if (buf.length >= 65536) throw new UTFDataFormatException();
		out.writeShort(Short.reverseBytes((short)buf.length));
		out.write(buf);
	}
	
	public final void writeInt24(int v) throws IOException {
		out.writeByte(v >> 16);
		out.writeByte(v >> 8);
		out.writeByte(v);
	}
	public final void writeInt24LE(int v) throws IOException {
		out.writeByte(v);
		out.writeByte(v >> 8);
		out.writeByte(v >> 16);
	}
	
	public final void writeInt48(long v) throws IOException {
		out.writeByte((int)(v >> 40));
		out.writeByte((int)(v >> 32));
		out.writeByte((int)(v >> 24));
		out.writeByte((int)(v >> 16));
		out.writeByte((int)(v >> 8));
		out.writeByte((int)v);
	}
	public final void writeInt48LE(long v) throws IOException {
		out.writeByte((int)v);
		out.writeByte((int)(v >> 8));
		out.writeByte((int)(v >> 16));
		out.writeByte((int)(v >> 24));
		out.writeByte((int)(v >> 32));
		out.writeByte((int)(v >> 40));
	}
	
	public final void writeSignedBEB128(BigInteger v) throws IOException { writeVLA(unzipB128(v, true), true); }
	public final void writeUnsignedBEB128(BigInteger v) throws IOException { writeVLA(unzipB128(v, false), true); }
	public final void writeSignedLEB128(BigInteger v) throws IOException { writeVLA(unzipB128(v, true), false); }
	public final void writeUnsignedLEB128(BigInteger v) throws IOException { writeVLA(unzipB128(v, false), false); }
	private final byte[] unzipB128(BigInteger v, boolean signed) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int n = v.bitLength(); if (n == 0) n++; if (signed) n++;
		while (n > 0) { out.write(v.byteValue()); v = v.shiftRight(7); n -= 7; }
		return out.toByteArray();
	}
	private final void writeVLA(byte[] b, boolean be) throws IOException {
		final int m = b.length - 1;
		if (be) { for (int i = m; i >= 0; i--) out.writeByte((i == 0) ? (b[i] & 0x7F) : (b[i] | 0x80)); }
		else    { for (int i = 0; i <= m; i++) out.writeByte((i == m) ? (b[i] & 0x7F) : (b[i] | 0x80)); }
	}
	
	public final void writeUnsignedBEB100(BigInteger v) throws IOException { writeVLA(unzipB100(v), true); }
	public final void writeUnsignedLEB100(BigInteger v) throws IOException { writeVLA(unzipB100(v), false); }
	private final byte[] unzipB100(BigInteger v) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BigInteger[] dm = v.abs().divideAndRemainder(B100);
		out.write(BIN7DPD2[dm[1].intValue()]);
		while (dm[0].signum() != 0) {
			dm = dm[0].divideAndRemainder(B100);
			out.write(BIN7DPD2[dm[1].intValue()]);
		}
		return out.toByteArray();
	}
	private static final BigInteger B100 = BigInteger.valueOf(100);
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
	
	private final void writeVLFPNaN(boolean negative, boolean decimal, boolean quiet, BigInteger payload, boolean le) throws IOException {
		int sign = payload.signum();
		if (sign < 0) { negative = !negative; payload = payload.negate(); }
		
		byte header = 0x3C;
		if (negative) header |= 0x80;
		if (decimal) header |= 0x40;
		if (sign == 0) {
			if (quiet) header |= 0x02;
			out.writeByte(header);
		} else {
			if (!quiet) header |= 0x02;
			out.writeByte(header | 0x01);
			if (decimal) { if (le) writeUnsignedLEB100(payload); else writeUnsignedBEB100(payload); }
			else         { if (le) writeUnsignedLEB128(payload); else writeUnsignedBEB128(payload); }
		}
	}
	
	private final void writeVLFPNaN(FloatFormat.NaN nan, boolean le) throws IOException {
		writeVLFPNaN(nan.isNegative(), false, nan.isQuiet(), nan.getPattern(), le);
	}
	
	private final void writeVLFPZero(boolean negative, boolean decimal) throws IOException {
		byte header = 0x00;
		if (negative) header |= 0x80;
		if (decimal) header |= 0x40;
		out.writeByte(header);
	}
	
	private final void writeVLFPZero(FloatFormat.Zero zero) throws IOException {
		writeVLFPZero(zero.isNegative(), false);
	}
	
	private final void writeVLFPFinite(boolean negative, boolean decimal, int exponent, BigInteger mantissa, boolean le) throws IOException {
		int sign = mantissa.signum();
		if (sign == 0) { writeVLFPZero(negative, decimal); return; }
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
		
		writeVLFPHeader(negative, decimal, exponent, mantissa.intValue(), payload.length > 0);
		if (exponent != 0) writeVLFPExponent(exponent, le);
		if (payload.length > 0) writeVLA(payload, !le);
	}
	private final void writeVLFPHeader(boolean negative, boolean decimal, int exponent, int leadingDigit, boolean payload) throws IOException {
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
		out.writeByte(header);
	}
	private final void writeVLFPExponent(int exponent, boolean le) throws IOException {
		BigInteger exp = BigInteger.valueOf(exponent);
		if (le) writeUnsignedLEB128(exp.abs());
		else writeUnsignedBEB128(exp.abs());
	}
	
	private final void writeVLFPBigDecimal(BigDecimal value, boolean le) throws IOException {
		writeVLFPFinite(false, true, -value.scale(), value.unscaledValue(), le);
	}
	
	private final void writeVLFPBigInteger(BigInteger value, boolean le) throws IOException {
		writeVLFPFinite(false, false, 0, value, le);
	}
	
	private final void writeVLFPLong(long value, boolean le) throws IOException {
		writeVLFPFinite(false, false, 0, BigInteger.valueOf(value), le);
	}
	
	private final void writeVLFPInt(int value, boolean le) throws IOException {
		writeVLFPFinite(false, false, 0, BigInteger.valueOf(value), le);
	}
	
	private final void writeVLFPFloat(float value, boolean le) throws IOException {
		int fbits = Float.floatToRawIntBits(value);
		boolean negative = (fbits < 0);
		int exponent = (fbits >> 23) & 0xFF;
		if (exponent == 0xFF) {
			boolean quiet = ((fbits & (1 << 22)) != 0);
			int payload = fbits & ((1 << 22) - 1);
			writeVLFPNaN(negative, false, quiet, BigInteger.valueOf(payload), le);
			return;
		}
		int mantissa = fbits & ((1 << 23) - 1);
		if (exponent == 0) {
			if (mantissa == 0) { writeVLFPZero(negative, false); return; }
			exponent = 1 - (127 + 23);
		} else {
			mantissa |= (1 << 23);
			exponent -= (127 + 23);
		}
		writeVLFPFinite(negative, false, exponent, BigInteger.valueOf(mantissa), le);
	}
	
	private final void writeVLFPDouble(double value, boolean le) throws IOException {
		long dbits = Double.doubleToRawLongBits(value);
		boolean negative = (dbits < 0);
		int exponent = (int)(dbits >> 52) & 0x7FF;
		if (exponent == 0x7FF) {
			boolean quiet = ((dbits & (1L << 51)) != 0);
			long payload = dbits & ((1L << 51) - 1);
			writeVLFPNaN(negative, false, quiet, BigInteger.valueOf(payload), le);
			return;
		}
		long mantissa = dbits & ((1L << 52) - 1);
		if (exponent == 0) {
			if (mantissa == 0) { writeVLFPZero(negative, false); return; }
			exponent = 1 - (1023 + 52);
		} else {
			mantissa |= (1L << 52);
			exponent -= (1023 + 52);
		}
		writeVLFPFinite(negative, false, exponent, BigInteger.valueOf(mantissa), le);
	}
	
	public final void writeVLFPLE(Number value) throws IOException { writeVLFP(value, true); }
	public final void writeVLFPBE(Number value) throws IOException { writeVLFP(value, false); }
	private final void writeVLFP(Number value, boolean le) throws IOException {
		if (value instanceof NaN) { writeVLFPNaN((NaN)value, le); return; }
		if (value instanceof Zero) { writeVLFPZero((Zero)value); return; }
		if (value instanceof BigDecimal) { writeVLFPBigDecimal((BigDecimal)value, le); return; }
		if (value instanceof BigInteger) { writeVLFPBigInteger((BigInteger)value, le); return; }
		if (value instanceof Long) { writeVLFPLong(value.longValue(), le); return; }
		if (value instanceof Integer) { writeVLFPInt(value.intValue(), le); return; }
		if (value instanceof Short) { writeVLFPInt(value.intValue(), le); return; }
		if (value instanceof Byte) { writeVLFPInt(value.intValue(), le); return; }
		if (value instanceof Float) { writeVLFPFloat(value.floatValue(), le); return; }
		if (value instanceof Double) { writeVLFPDouble(value.doubleValue(), le); return; }
		throw new UnsupportedOperationException(
			"unknown subclass of Number; please convert to a known subclass " +
			"(Byte, Short, Integer, Long, Float, Double, BigInteger, BigDecimal, " +
			"FloatFormat.Zero, or FloatFormat.NaN)"
		);
	}
}
