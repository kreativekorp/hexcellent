package com.kreative.experimental;

import java.io.DataInput;
import java.io.IOException;
import java.math.BigInteger;

public interface KDataInput extends DataInput {
	public boolean readBoolean() throws IOException;
	public byte readByte() throws IOException;
	public char readChar() throws IOException;
	public double readDouble() throws IOException;
	public float readFloat() throws IOException;
	public void readFully(byte[] b) throws IOException;
	public void readFully(byte[] b, int off, int len) throws IOException;
	public int readInt() throws IOException;
	public String readLine() throws IOException;
	public long readLong() throws IOException;
	public short readShort() throws IOException;
	public int readUnsignedByte() throws IOException;
	public int readUnsignedShort() throws IOException;
	public int skipBytes(int n) throws IOException;
	
	public char readCharLE() throws IOException;
	public double readDoubleLE() throws IOException;
	public float readFloatLE() throws IOException;
	public int readIntLE() throws IOException;
	public long readLongLE() throws IOException;
	public short readShortLE() throws IOException;
	public int readUnsignedShortLE() throws IOException;
	
	public String readUTF() throws IOException;
	public String readUTFLE() throws IOException;
	
	public int readInt24() throws IOException;
	public long readInt48() throws IOException;
	public int readUnsignedInt24() throws IOException;
	public long readUnsignedInt48() throws IOException;
	
	public int readInt24LE() throws IOException;
	public long readInt48LE() throws IOException;
	public int readUnsignedInt24LE() throws IOException;
	public long readUnsignedInt48LE() throws IOException;
	
	public BigInteger readSignedBEB128() throws IOException;
	public BigInteger readUnsignedBEB128() throws IOException;
	public BigInteger readSignedLEB128() throws IOException;
	public BigInteger readUnsignedLEB128() throws IOException;
	
	public BigInteger readUnsignedBEB100() throws IOException;
	public BigInteger readUnsignedLEB100() throws IOException;
	
	public Number readVLFPBE() throws IOException;
	public Number readVLFPLE() throws IOException;
}
