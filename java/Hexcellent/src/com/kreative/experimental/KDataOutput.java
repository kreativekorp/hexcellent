package com.kreative.experimental;

import java.io.DataOutput;
import java.io.IOException;
import java.math.BigInteger;

public interface KDataOutput extends DataOutput {
	public void write(int v) throws IOException;
	public void write(byte[] b) throws IOException;
	public void write(byte[] b, int off, int len) throws IOException;
	public void writeBoolean(boolean v) throws IOException;
	public void writeByte(int v) throws IOException;
	public void writeBytes(String v) throws IOException;
	public void writeChar(int v) throws IOException;
	public void writeChars(String v) throws IOException;
	public void writeDouble(double v) throws IOException;
	public void writeFloat(float v) throws IOException;
	public void writeInt(int v) throws IOException;
	public void writeLong(long v) throws IOException;
	public void writeShort(int v) throws IOException;
	
	public void writeCharLE(int v) throws IOException;
	public void writeCharsLE(String v) throws IOException;
	public void writeDoubleLE(double v) throws IOException;
	public void writeFloatLE(float v) throws IOException;
	public void writeIntLE(int v) throws IOException;
	public void writeLongLE(long v) throws IOException;
	public void writeShortLE(int v) throws IOException;
	
	public void writeUTF(String v) throws IOException;
	public void writeUTFLE(String v) throws IOException;
	
	public void writeInt24(int v) throws IOException;
	public void writeInt48(long v) throws IOException;
	public void writeInt24LE(int v) throws IOException;
	public void writeInt48LE(long v) throws IOException;
	
	public void writeSignedBEB128(BigInteger v) throws IOException;
	public void writeUnsignedBEB128(BigInteger v) throws IOException;
	public void writeSignedLEB128(BigInteger v) throws IOException;
	public void writeUnsignedLEB128(BigInteger v) throws IOException;
	
	public void writeUnsignedBEB100(BigInteger v) throws IOException;
	public void writeUnsignedLEB100(BigInteger v) throws IOException;
	
	public void writeVLFPBE(Number value) throws IOException;
	public void writeVLFPLE(Number value) throws IOException;
}
