package com.kreative.experimental;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.kreative.hexcellent.buffer.FloatFormat;

public final class KDataInputImpl implements DataInput, KDataInput {
	private final DataInput in;
	public KDataInputImpl(DataInput in) { this.in = in; }
	
	@Override public final boolean readBoolean() throws IOException { return in.readBoolean(); }
	@Override public final byte readByte() throws IOException { return in.readByte(); }
	@Override public final char readChar() throws IOException { return in.readChar(); }
	@Override public final double readDouble() throws IOException { return in.readDouble(); }
	@Override public final float readFloat() throws IOException { return in.readFloat(); }
	@Override public final void readFully(byte[] b) throws IOException { in.readFully(b); }
	@Override public final void readFully(byte[] b, int off, int len) throws IOException { in.readFully(b, off, len); }
	@Override public final int readInt() throws IOException { return in.readInt(); }
	@Override public final String readLine() throws IOException { return in.readLine(); }
	@Override public final long readLong() throws IOException { return in.readLong(); }
	@Override public final short readShort() throws IOException { return in.readShort(); }
	@Override public final int readUnsignedByte() throws IOException { return in.readUnsignedByte(); }
	@Override public final int readUnsignedShort() throws IOException { return in.readUnsignedShort(); }
	@Override public final int skipBytes(int n) throws IOException { return in.skipBytes(n); }
	
	public final char readCharLE() throws IOException {
		return Character.reverseBytes(in.readChar());
	}
	public final double readDoubleLE() throws IOException {
		return Double.longBitsToDouble(Long.reverseBytes(in.readLong()));
	}
	public final float readFloatLE() throws IOException {
		return Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
	}
	public final int readIntLE() throws IOException {
		return Integer.reverseBytes(in.readInt());
	}
	public final long readLongLE() throws IOException {
		return Long.reverseBytes(in.readLong());
	}
	public final short readShortLE() throws IOException {
		return Short.reverseBytes(in.readShort());
	}
	public final int readUnsignedShortLE() throws IOException {
		return Short.reverseBytes(in.readShort()) & 0xFFFF;
	}
	
	@Override
	public final String readUTF() throws IOException {
		int len = in.readUnsignedShort();
		byte[] buf = new byte[len];
		in.readFully(buf);
		return new String(buf, "UTF-8");
	}
	public final String readUTFLE() throws IOException {
		int len = Short.reverseBytes(in.readShort()) & 0xFFFF;
		byte[] buf = new byte[len];
		in.readFully(buf);
		return new String(buf, "UTF-8");
	}
	
	public final int readInt24() throws IOException {
		int v = (int)in.readByte() << 16;
		v |= (int)in.readUnsignedByte() << 8;
		v |= (int)in.readUnsignedByte();
		return v;
	}
	public final int readInt24LE() throws IOException {
		int v = (int)in.readUnsignedByte();
		v |= (int)in.readUnsignedByte() << 8;
		v |= (int)in.readByte() << 16;
		return v;
	}
	public final int readUnsignedInt24() throws IOException {
		int v = (int)in.readUnsignedByte() << 16;
		v |= (int)in.readUnsignedByte() << 8;
		v |= (int)in.readUnsignedByte();
		return v;
	}
	public final int readUnsignedInt24LE() throws IOException {
		int v = (int)in.readUnsignedByte();
		v |= (int)in.readUnsignedByte() << 8;
		v |= (int)in.readUnsignedByte() << 16;
		return v;
	}
	
	public final long readInt48() throws IOException {
		long v = (long)in.readByte() << 40;
		v |= (long)in.readUnsignedByte() << 32;
		v |= (long)in.readUnsignedByte() << 24;
		v |= (long)in.readUnsignedByte() << 16;
		v |= (long)in.readUnsignedByte() << 8;
		v |= (long)in.readUnsignedByte();
		return v;
	}
	public final long readInt48LE() throws IOException {
		long v = (long)in.readUnsignedByte();
		v |= (long)in.readUnsignedByte() << 8;
		v |= (long)in.readUnsignedByte() << 16;
		v |= (long)in.readUnsignedByte() << 24;
		v |= (long)in.readUnsignedByte() << 32;
		v |= (long)in.readByte() << 40;
		return v;
	}
	public final long readUnsignedInt48() throws IOException {
		long v = (long)in.readUnsignedByte() << 40;
		v |= (long)in.readUnsignedByte() << 32;
		v |= (long)in.readUnsignedByte() << 24;
		v |= (long)in.readUnsignedByte() << 16;
		v |= (long)in.readUnsignedByte() << 8;
		v |= (long)in.readUnsignedByte();
		return v;
	}
	public final long readUnsignedInt48LE() throws IOException {
		long v = (long)in.readUnsignedByte();
		v |= (long)in.readUnsignedByte() << 8;
		v |= (long)in.readUnsignedByte() << 16;
		v |= (long)in.readUnsignedByte() << 24;
		v |= (long)in.readUnsignedByte() << 32;
		v |= (long)in.readUnsignedByte() << 40;
		return v;
	}
	
	public final BigInteger readSignedBEB128() throws IOException { return readBEB128(true); }
	public final BigInteger readUnsignedBEB128() throws IOException { return readBEB128(false); }
	public final BigInteger readSignedLEB128() throws IOException { return readLEB128(true); }
	public final BigInteger readUnsignedLEB128() throws IOException { return readLEB128(false); }
	private final BigInteger readBEB128(boolean signed) throws IOException {
		final byte[] b = readVLA(); final int m = b.length - 1;
		BigInteger v = (signed && ((b[0] & 0x40) != 0)) ? BigInteger.ONE.negate() : BigInteger.ZERO;
		for (int i = 0; i <= m; i++) v = v.shiftLeft(7).or(BigInteger.valueOf(b[i] & 0x7F));
		return v;
	}
	private final BigInteger readLEB128(boolean signed) throws IOException {
		final byte[] b = readVLA(); final int m = b.length - 1;
		BigInteger v = (signed && ((b[m] & 0x40) != 0)) ? BigInteger.ONE.negate() : BigInteger.ZERO;
		for (int i = m; i >= 0; i--) v = v.shiftLeft(7).or(BigInteger.valueOf(b[i] & 0x7F));
		return v;
	}
	private final byte[] readVLA() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte b; do out.write((b = in.readByte())); while (b < 0);
		return out.toByteArray();
	}
	
	public final BigInteger readUnsignedBEB100() throws IOException {
		final byte[] b = readVLA(); final int m = b.length - 1;
		BigInteger v = BigInteger.ZERO;
		for (int i = 0; i <= m; i++) v = v.multiply(B100).add(BigInteger.valueOf(DPD2BIN7[b[i] & 0x7F]));
		return v;
	}
	public final BigInteger readUnsignedLEB100() throws IOException {
		final byte[] b = readVLA(); final int m = b.length - 1;
		BigInteger v = BigInteger.ZERO;
		for (int i = m; i >= 0; i--) v = v.multiply(B100).add(BigInteger.valueOf(DPD2BIN7[b[i] & 0x7F]));
		return v;
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
	
	public final Number readVLFPLE() throws IOException { return readVLFP(true); }
	public final Number readVLFPBE() throws IOException { return readVLFP(false); }
	private final Number readVLFP(boolean le) throws IOException {
		// Read header byte
		final int header = in.readByte();
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
						decimal ? (le ? readUnsignedLEB100() : readUnsignedBEB100())
						        : (le ? readUnsignedLEB128() : readUnsignedBEB128())
					));
				} else {
					return new FloatFormat.NaN(negative, signaling, null);
				}
			}
			// Large Leading Digit (8 or 9)
			exponent = readVLFPExp(negExpLLD, posExpLLD, le);
			mantissa = BigInteger.valueOf((header >> 1) & 9);
		} else {
			// Small Leading Digit (0 to 7)
			exponent = readVLFPExp(negExpSLD, posExpSLD, le);
			mantissa = BigInteger.valueOf((header >> 1) & 7);
		}
		
		// Read mantissa
		if (payload) {
			final byte[] b = readVLA();
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
	private final int readVLFPExp(boolean neg, boolean pos, boolean le) throws IOException {
		if (neg == pos) return 0;
		BigInteger exp = (le ? readUnsignedLEB128() : readUnsignedBEB128());
		return neg ? exp.negate().intValueExact() : exp.intValueExact();
	}
}
