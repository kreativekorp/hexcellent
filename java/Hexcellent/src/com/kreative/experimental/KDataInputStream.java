package com.kreative.experimental;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public final class KDataInputStream extends InputStream implements DataInput, KDataInput {
	private final DataInputStream din;
	private final KDataInputImpl kin;
	
	public KDataInputStream(InputStream in) {
		this.din = new DataInputStream(in);
		this.kin = new KDataInputImpl(din);
	}
	
	@Override public final int available() throws IOException { return din.available(); }
	@Override public final void close() throws IOException { din.close(); }
	@Override public final void mark(int limit) { din.mark(limit); }
	@Override public final boolean markSupported() { return din.markSupported(); }
	@Override public final int read() throws IOException { return din.read(); }
	@Override public final int read(byte[] b) throws IOException { return din.read(b); }
	@Override public final int read(byte[] b, int off, int len) throws IOException { return din.read(b, off, len); }
	@Override public final void reset() throws IOException { din.reset(); }
	@Override public final long skip(long n) throws IOException { return din.skip(n); }
	
	@Override public final boolean readBoolean() throws IOException { return kin.readBoolean(); }
	@Override public final byte readByte() throws IOException { return kin.readByte(); }
	@Override public final char readChar() throws IOException { return kin.readChar(); }
	@Override public final double readDouble() throws IOException { return kin.readDouble(); }
	@Override public final float readFloat() throws IOException { return kin.readFloat(); }
	@Override public final void readFully(byte[] b) throws IOException { kin.readFully(b); }
	@Override public final void readFully(byte[] b, int off, int len) throws IOException { kin.readFully(b, off, len); }
	@Override public final int readInt() throws IOException { return kin.readInt(); }
	@Override public final String readLine() throws IOException { return kin.readLine(); }
	@Override public final long readLong() throws IOException { return kin.readLong(); }
	@Override public final short readShort() throws IOException { return kin.readShort(); }
	@Override public final int readUnsignedByte() throws IOException { return kin.readUnsignedByte(); }
	@Override public final int readUnsignedShort() throws IOException { return kin.readUnsignedShort(); }
	@Override public final int skipBytes(int n) throws IOException { return kin.skipBytes(n); }
	
	@Override public final char readCharLE() throws IOException { return kin.readCharLE(); }
	@Override public final double readDoubleLE() throws IOException { return kin.readDoubleLE(); }
	@Override public final float readFloatLE() throws IOException { return kin.readFloatLE(); }
	@Override public final int readIntLE() throws IOException { return kin.readIntLE(); }
	@Override public final long readLongLE() throws IOException { return kin.readLongLE(); }
	@Override public final short readShortLE() throws IOException { return kin.readShortLE(); }
	@Override public final int readUnsignedShortLE() throws IOException { return kin.readUnsignedShortLE(); }
	
	@Override public final String readUTF() throws IOException { return kin.readUTF(); }
	@Override public final String readUTFLE() throws IOException { return kin.readUTFLE(); }
	
	@Override public final int readInt24() throws IOException { return kin.readInt24(); }
	@Override public final long readInt48() throws IOException { return kin.readInt48(); }
	@Override public final int readUnsignedInt24() throws IOException { return kin.readUnsignedInt24(); }
	@Override public final long readUnsignedInt48() throws IOException { return kin.readUnsignedInt48(); }
	
	@Override public final int readInt24LE() throws IOException { return kin.readInt24LE(); }
	@Override public final long readInt48LE() throws IOException { return kin.readInt48LE(); }
	@Override public final int readUnsignedInt24LE() throws IOException { return kin.readUnsignedInt24LE(); }
	@Override public final long readUnsignedInt48LE() throws IOException { return kin.readUnsignedInt48LE(); }
	
	@Override public final BigInteger readSignedBEB128() throws IOException { return kin.readSignedBEB128(); }
	@Override public final BigInteger readUnsignedBEB128() throws IOException { return kin.readUnsignedBEB128(); }
	@Override public final BigInteger readSignedLEB128() throws IOException { return kin.readSignedLEB128(); }
	@Override public final BigInteger readUnsignedLEB128() throws IOException { return kin.readUnsignedLEB128(); }
	
	@Override public final BigInteger readUnsignedBEB100() throws IOException { return kin.readUnsignedBEB100(); }
	@Override public final BigInteger readUnsignedLEB100() throws IOException { return kin.readUnsignedLEB100(); }
	
	@Override public final Number readVLFPBE() throws IOException { return kin.readVLFPBE(); }
	@Override public final Number readVLFPLE() throws IOException { return kin.readVLFPLE(); }
}
