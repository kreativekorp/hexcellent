package com.kreative.experimental;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public final class KDataOutputStream extends OutputStream implements DataOutput, KDataOutput {
	private final DataOutputStream dout;
	private final KDataOutputImpl kout;
	
	public KDataOutputStream(OutputStream out) {
		this.dout = new DataOutputStream(out);
		this.kout = new KDataOutputImpl(dout);
	}
	
	@Override public final void close() throws IOException { dout.close(); }
	@Override public final void flush() throws IOException { dout.flush(); }
	
	@Override public final void write(int v) throws IOException { kout.write(v); }
	@Override public final void write(byte[] b) throws IOException { kout.write(b); }
	@Override public final void write(byte[] b, int off, int len) throws IOException { kout.write(b, off, len); }
	@Override public final void writeBoolean(boolean v) throws IOException { kout.writeBoolean(v); }
	@Override public final void writeByte(int v) throws IOException { kout.writeByte(v); }
	@Override public final void writeBytes(String v) throws IOException { kout.writeBytes(v); }
	@Override public final void writeChar(int v) throws IOException { kout.writeChar(v); }
	@Override public final void writeChars(String v) throws IOException { kout.writeChars(v); }
	@Override public final void writeDouble(double v) throws IOException { kout.writeDouble(v); }
	@Override public final void writeFloat(float v) throws IOException { kout.writeFloat(v); }
	@Override public final void writeInt(int v) throws IOException { kout.writeInt(v); }
	@Override public final void writeLong(long v) throws IOException { kout.writeLong(v); }
	@Override public final void writeShort(int v) throws IOException { kout.writeShort(v); }
	
	@Override public final void writeCharLE(int v) throws IOException { kout.writeCharLE(v); }
	@Override public final void writeCharsLE(String v) throws IOException { kout.writeCharsLE(v); }
	@Override public final void writeDoubleLE(double v) throws IOException { kout.writeDoubleLE(v); }
	@Override public final void writeFloatLE(float v) throws IOException { kout.writeFloatLE(v); }
	@Override public final void writeIntLE(int v) throws IOException { kout.writeIntLE(v); }
	@Override public final void writeLongLE(long v) throws IOException { kout.writeLongLE(v); }
	@Override public final void writeShortLE(int v) throws IOException { kout.writeShortLE(v); }
	
	@Override public final void writeUTF(String v) throws IOException { kout.writeUTF(v); }
	@Override public final void writeUTFLE(String v) throws IOException { kout.writeUTFLE(v); }
	
	@Override public final void writeInt24(int v) throws IOException { kout.writeInt24(v); }
	@Override public final void writeInt48(long v) throws IOException { kout.writeInt48(v); }
	@Override public final void writeInt24LE(int v) throws IOException { kout.writeInt24LE(v); }
	@Override public final void writeInt48LE(long v) throws IOException { kout.writeInt48LE(v); }
	
	@Override public final void writeSignedBEB128(BigInteger v) throws IOException { kout.writeSignedBEB128(v); }
	@Override public final void writeUnsignedBEB128(BigInteger v) throws IOException { kout.writeUnsignedBEB128(v); }
	@Override public final void writeSignedLEB128(BigInteger v) throws IOException { kout.writeSignedLEB128(v); }
	@Override public final void writeUnsignedLEB128(BigInteger v) throws IOException { kout.writeUnsignedLEB128(v); }
	
	@Override public final void writeBEFP128(Number value) throws IOException { kout.writeBEFP128(value); }
	@Override public final void writeLEFP128(Number value) throws IOException { kout.writeLEFP128(value); }
}
