package com.kreative.experimental;

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
	private final BigInteger readBEB128(boolean signed) throws IOException {
		byte b = in.readByte();
		byte s = (byte)(b & 0x7F);
		if (signed) s |= ((b << 1) & 0x80);
		BigInteger v = BigInteger.valueOf(s);
		while (b < 0) {
			b = in.readByte();
			s = (byte)(b & 0x7F);
			v = v.shiftLeft(7).or(BigInteger.valueOf(s));
		}
		return v;
	}
	
	public final BigInteger readSignedLEB128() throws IOException { return readLEB128(true); }
	public final BigInteger readUnsignedLEB128() throws IOException { return readLEB128(false); }
	private final BigInteger readLEB128(boolean signed) throws IOException {
		int shift = 0;
		byte b = in.readByte();
		byte s = (byte)(b & 0x7F);
		if (signed && b >= 0) s |= ((b << 1) & 0x80);
		BigInteger v = BigInteger.valueOf(s);
		while (b < 0) {
			shift += 7;
			b = in.readByte();
			s = (byte)(b & 0x7F);
			if (signed && b >= 0) s |= ((b << 1) & 0x80);
			v = BigInteger.valueOf(s).shiftLeft(shift).or(v);
		}
		return v;
	}
	
	public final Number readLEFP128() throws IOException { return readFP128(true); }
	public final Number readBEFP128() throws IOException { return readFP128(false); }
	private final Number readFP128(boolean le) throws IOException {
		final int header = in.readByte();
		final boolean negative = ((header & 0x80) != 0);
		final boolean isNaN    = ((header & 0x40) != 0);
		final boolean hasExp   = ((header & 0x20) != 0);
		final boolean decimal  = ((header & 0x10) != 0);
		final boolean negExp   = ((header & 0x08) != 0);
		final boolean quiet    = ((header & 0x04) != 0);
		final boolean hasMan   = ((header & 0x02) != 0);
		final boolean unit     = ((header & 0x01) != 0);
		
		final BigInteger exponent;
		if (hasExp) exponent = le ? readUnsignedLEB128() : readUnsignedBEB128();
		else exponent = BigInteger.ZERO;
		
		final BigInteger mantissa;
		if (hasMan) mantissa = le ? readUnsignedLEB128() : readUnsignedBEB128();
		else mantissa = unit ? BigInteger.ONE : BigInteger.ZERO;
		
		if (isNaN) {
			return new FloatFormat.NaN(negative, quiet, mantissa);
		}
		if (mantissa.equals(BigInteger.ZERO)) {
			return negative ? FloatFormat.Zero.NEGATIVE_ZERO : FloatFormat.Zero.POSITIVE_ZERO;
		}
		if (exponent.equals(BigInteger.ZERO)) {
			return negative ? mantissa.negate() : mantissa;
		}
		
		final BigInteger factor;
		if (decimal) factor = BigInteger.TEN.pow(exponent.intValueExact());
		else factor = BigInteger.ONE.shiftLeft(exponent.intValueExact());
		
		final BigDecimal magnitude;
		if (negExp) magnitude = new BigDecimal(mantissa).divide(new BigDecimal(factor));
		else magnitude = new BigDecimal(mantissa).multiply(new BigDecimal(factor));
		
		BigDecimal result = negative ? magnitude.negate() : magnitude;
		if (result.scale() == 0) result = result.setScale(1);
		return result;
	}
}
