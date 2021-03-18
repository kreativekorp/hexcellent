package com.kreative.experimental;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.kreative.hexcellent.buffer.FloatFormat;
import com.kreative.hexcellent.buffer.FloatFormat.NaN;
import com.kreative.hexcellent.buffer.FloatFormat.Zero;

public class KDataOutput implements DataOutput {
	private final DataOutput out;
	public KDataOutput(DataOutput out) { this.out = out; }
	@Override public void write(int v) throws IOException { out.write(v); }
	@Override public void write(byte[] b) throws IOException { out.write(b); }
	@Override public void write(byte[] b, int off, int len) throws IOException { out.write(b, off, len); }
	@Override public void writeBoolean(boolean v) throws IOException { out.writeBoolean(v); }
	@Override public void writeByte(int v) throws IOException { out.writeByte(v); }
	@Override public void writeBytes(String v) throws IOException { out.writeBytes(v); }
	@Override public void writeChar(int v) throws IOException { out.writeChar(v); }
	@Override public void writeChars(String v) throws IOException { out.writeChars(v); }
	@Override public void writeDouble(double v) throws IOException { out.writeDouble(v); }
	@Override public void writeFloat(float v) throws IOException { out.writeFloat(v); }
	@Override public void writeInt(int v) throws IOException { out.writeInt(v); }
	@Override public void writeLong(long v) throws IOException { out.writeLong(v); }
	@Override public void writeShort(int v) throws IOException { out.writeShort(v); }
	
	public void writeCharLE(int v) throws IOException {
		out.writeChar(Character.reverseBytes((char)v));
	}
	public void writeCharsLE(String v) throws IOException {
		for (char ch : v.toCharArray()) out.writeChar(Character.reverseBytes(ch));
	}
	public void writeDoubleLE(double v) throws IOException {
		out.writeLong(Long.reverseBytes(Double.doubleToRawLongBits(v)));
	}
	public void writeFloatLE(float v) throws IOException {
		out.writeInt(Integer.reverseBytes(Float.floatToRawIntBits(v)));
	}
	public void writeIntLE(int v) throws IOException {
		out.writeInt(Integer.reverseBytes(v));
	}
	public void writeLongLE(long v) throws IOException {
		out.writeLong(Long.reverseBytes(v));
	}
	public void writeShortLE(int v) throws IOException {
		out.writeShort(Short.reverseBytes((short)v));
	}
	
	@Override
	public void writeUTF(String v) throws IOException {
		byte[] buf = v.getBytes("UTF-8");
		if (buf.length >= 65536) throw new UTFDataFormatException();
		out.writeShort(buf.length);
		out.write(buf);
	}
	public void writeUTF8LE(String v) throws IOException {
		byte[] buf = v.getBytes("UTF-8");
		if (buf.length >= 65536) throw new UTFDataFormatException();
		out.writeShort(Short.reverseBytes((short)buf.length));
		out.write(buf);
	}
	
	public void writeInt24(int v) throws IOException {
		out.writeByte(v >> 16);
		out.writeByte(v >> 8);
		out.writeByte(v);
	}
	public void writeInt24LE(int v) throws IOException {
		out.writeByte(v);
		out.writeByte(v >> 8);
		out.writeByte(v >> 16);
	}
	
	public void writeInt48(long v) throws IOException {
		out.writeByte((int)(v >> 40));
		out.writeByte((int)(v >> 32));
		out.writeByte((int)(v >> 24));
		out.writeByte((int)(v >> 16));
		out.writeByte((int)(v >> 8));
		out.writeByte((int)v);
	}
	public void writeInt48LE(long v) throws IOException {
		out.writeByte((int)v);
		out.writeByte((int)(v >> 8));
		out.writeByte((int)(v >> 16));
		out.writeByte((int)(v >> 24));
		out.writeByte((int)(v >> 32));
		out.writeByte((int)(v >> 40));
	}
	
	public void writeSignedBEB128(BigInteger v) throws IOException { writeBEB128(v, true); }
	public void writeUnsignedBEB128(BigInteger v) throws IOException { writeBEB128(v, false); }
	private void writeBEB128(BigInteger v, boolean signed) throws IOException {
		int n = (v.bitLength() + (signed ? 7 : 6)) / 7;
		while (n > 1) {
			n--;
			BigInteger s = v.shiftRight(n * 7);
			out.writeByte(s.byteValue() | 0x80);
		}
		out.writeByte(v.byteValue() & 0x7F);
	}
	
	public void writeSignedLEB128(BigInteger v) throws IOException { writeLEB128(v, true); }
	public void writeUnsignedLEB128(BigInteger v) throws IOException { writeLEB128(v, false); }
	private void writeLEB128(BigInteger v, boolean signed) throws IOException {
		int n = (v.bitLength() + (signed ? 7 : 6)) / 7;
		while (n > 1) {
			n--;
			out.writeByte(v.byteValue() | 0x80);
			v = v.shiftRight(7);
		}
		out.writeByte(v.byteValue() & 0x7F);
	}
	
	private void writeFP128NaN(boolean le, boolean negative, boolean quiet, BigInteger pattern) throws IOException {
		byte header = 0x40;
		if (negative) header |= 0x80;
		if (quiet) header |= 0x04;
		if (pattern.equals(BigInteger.ZERO)) {
			out.writeByte(header);
		} else if (pattern.equals(BigInteger.ONE)) {
			out.writeByte(header | 0x01);
		} else {
			out.writeByte(header | 0x02);
			if (le) writeUnsignedLEB128(pattern);
			else writeUnsignedBEB128(pattern);
		}
	}
	
	private void writeFP128NaN(boolean le, FloatFormat.NaN nan) throws IOException {
		writeFP128NaN(le, nan.isNegative(), nan.isQuiet(), nan.getPattern());
	}
	
	private void writeFP128Zero(boolean negative) throws IOException {
		out.writeByte(negative ? 0x80 : 0x00);
	}
	
	private void writeFP128Zero(FloatFormat.Zero zero) throws IOException {
		writeFP128Zero(zero.isNegative());
	}
	
	private void writeFP128Finite(boolean le, boolean decimal, boolean negative, BigInteger exponent, BigInteger mantissa) throws IOException {
		int sign = mantissa.signum();
		if (sign == 0) {
			writeFP128Zero(negative);
			return;
		}
		if (sign < 0) {
			negative = !negative;
			mantissa = mantissa.negate();
		}
		
		int expSign = exponent.signum();
		boolean unit = mantissa.equals(BigInteger.ONE);
		byte header = (byte)(decimal ? 0x10 : 0x00);
		if (negative) header |= 0x80;
		if (expSign != 0) header |= 0x20;
		if (expSign < 0) header |= 0x08;
		header |= (unit ? 0x01 : 0x02);
		out.writeByte(header);
		if (expSign != 0) {
			if (le) writeUnsignedLEB128(exponent.abs());
			else writeUnsignedBEB128(exponent.abs());
		}
		if (!unit) {
			if (le) writeUnsignedLEB128(mantissa);
			else writeUnsignedBEB128(mantissa);
		}
	}
	
	private void writeFP128BigDecimal(boolean le, BigDecimal value) throws IOException {
		if (value.signum() == 0) {
			writeFP128Zero(false);
			return;
		}
		
		BigInteger exp = BigInteger.valueOf(-value.scale());
		BigInteger mantissa = value.unscaledValue();
		BigInteger[] qr = mantissa.divideAndRemainder(BigInteger.TEN);
		while (qr[1].signum() == 0) {
			// remainder is zero
			mantissa = qr[0];
			exp = exp.add(BigInteger.ONE);
			qr = mantissa.divideAndRemainder(BigInteger.TEN);
		}
		writeFP128Finite(le, true, false, exp, mantissa);
	}
	
	private void writeFP128BigInteger(boolean le, BigInteger value) throws IOException {
		if (value.signum() == 0) {
			writeFP128Zero(false);
			return;
		}
		
		BigInteger exp = BigInteger.ZERO;
		BigInteger mantissa = value;
		while (!mantissa.testBit(0)) {
			mantissa = mantissa.shiftRight(1);
			exp = exp.add(BigInteger.ONE);
		}
		writeFP128Finite(le, false, false, exp, mantissa);
	}
	
	private void writeFP128Long(boolean le, long value) throws IOException {
		if (value == 0) {
			writeFP128Zero(false);
			return;
		}
		
		long exp = 0;
		long mantissa = value;
		while ((mantissa & 1) == 0) {
			mantissa >>= 1;
			exp++;
		}
		writeFP128Finite(le, false, false, BigInteger.valueOf(exp), BigInteger.valueOf(mantissa));
	}
	
	private void writeFP128Int(boolean le, int value) throws IOException {
		if (value == 0) {
			writeFP128Zero(false);
			return;
		}
		
		int exp = 0;
		int mantissa = value;
		while ((mantissa & 1) == 0) {
			mantissa >>= 1;
			exp++;
		}
		writeFP128Finite(le, false, false, BigInteger.valueOf(exp), BigInteger.valueOf(mantissa));
	}
	
	private void writeFP128Float(boolean le, float value) throws IOException {
		int fbits = Float.floatToRawIntBits(value);
		boolean negative = (fbits < 0);
		int rawExp = (fbits >> 23) & 0xFF;
		if (rawExp == 0xFF) {
			boolean quiet = ((fbits & (1 << 22)) != 0);
			int pattern = fbits & ((1 << 22) - 1);
			writeFP128NaN(le, negative, quiet, BigInteger.valueOf(pattern));
			return;
		}
		
		int mantissa;
		if (rawExp == 0) {
			int rawMan = fbits & ((1 << 23) - 1);
			if (rawMan == 0) {
				writeFP128Zero(negative);
				return;
			}
			mantissa = rawMan << 1;
		} else {
			mantissa = (fbits & ((1 << 23) - 1)) | (1 << 23);
		}
		
		int exp = rawExp - (127 + 23);
		while ((mantissa & 1) == 0) {
			mantissa >>= 1;
			exp++;
		}
		writeFP128Finite(le, false, negative, BigInteger.valueOf(exp), BigInteger.valueOf(mantissa));
	}
	
	private void writeFP128Double(boolean le, double value) throws IOException {
		long dbits = Double.doubleToRawLongBits(value);
		boolean negative = (dbits < 0);
		long rawExp = (dbits >> 52) & 0x7FF;
		if (rawExp == 0x7FF) {
			boolean quiet = ((dbits & (1L << 51)) != 0);
			long pattern = dbits & ((1L << 51) - 1);
			writeFP128NaN(le, negative, quiet, BigInteger.valueOf(pattern));
			return;
		}
		
		long mantissa;
		if (rawExp == 0) {
			long rawMan = dbits & ((1L << 52) - 1);
			if (rawMan == 0) {
				writeFP128Zero(negative);
				return;
			}
			mantissa = rawMan << 1;
		} else {
			mantissa = (dbits & ((1L << 52) - 1)) | (1L << 52);
		}
		
		long exp = rawExp - (1023 + 52);
		while ((mantissa & 1) == 0) {
			mantissa >>= 1;
			exp++;
		}
		writeFP128Finite(le, false, negative, BigInteger.valueOf(exp), BigInteger.valueOf(mantissa));
	}
	
	public void writeLEFP128(Number value) throws IOException { writeFP128(true, value); }
	public void writeBEFP128(Number value) throws IOException { writeFP128(false, value); }
	private void writeFP128(boolean le, Number value) throws IOException {
		if (value instanceof NaN) { writeFP128NaN(le, (NaN)value); return; }
		if (value instanceof Zero) { writeFP128Zero((Zero)value); return; }
		if (value instanceof BigDecimal) { writeFP128BigDecimal(le, (BigDecimal)value); return; }
		if (value instanceof BigInteger) { writeFP128BigInteger(le, (BigInteger)value); return; }
		if (value instanceof Long) { writeFP128Long(le, value.longValue()); return; }
		if (value instanceof Integer) { writeFP128Int(le, value.intValue()); return; }
		if (value instanceof Short) { writeFP128Int(le, value.intValue()); return; }
		if (value instanceof Byte) { writeFP128Int(le, value.intValue()); return; }
		if (value instanceof Float) { writeFP128Float(le, value.floatValue()); return; }
		if (value instanceof Double) { writeFP128Double(le, value.doubleValue()); return; }
		throw new UnsupportedOperationException(
			"unknown subclass of Number; please convert to a known subclass " +
			"(Byte, Short, Integer, Long, Float, Double, BigInteger, BigDecimal, " +
			"FloatFormat.Zero, or FloatFormat.NaN)"
		);
	}
}
