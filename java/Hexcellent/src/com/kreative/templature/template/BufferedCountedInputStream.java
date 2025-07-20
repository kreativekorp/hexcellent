package com.kreative.templature.template;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class BufferedCountedInputStream extends InputStream {
	private final BufferedInputStream in;
	private long readPointer = 0;
	private long markPointer = 0;
	
	public BufferedCountedInputStream(InputStream in) {
		this.in = new BufferedInputStream(in);
	}
	
	public BufferedCountedInputStream(InputStream in, int size) {
		this.in = new BufferedInputStream(in, size);
	}
	
	public BufferedCountedInputStream(BufferedInputStream in) {
		this.in = in;
	}
	
	public int align(int mod) throws IOException {
		int r = (int)(readPointer % mod);
		if (r == 0) return 0;
		return read(new byte[mod - r]);
	}
	
	public int align(byte[] b, int mod) throws IOException {
		int r = (int)(readPointer % mod);
		if (r == 0) return 0;
		return read(b, 0, mod - r);
	}
	
	public int align(byte[] b, int off, int mod) throws IOException {
		int r = (int)(readPointer % mod);
		if (r == 0) return 0;
		return read(b, off, mod - r);
	}
	
	public int available() throws IOException {
		return in.available();
	}
	
	public void close() throws IOException {
		in.close();
	}
	
	public void mark(int readlimit) {
		in.mark(readlimit);
		markPointer = readPointer;
	}
	
	public boolean markSupported() {
		return in.markSupported();
	}
	
	public int read() throws IOException {
		int read = in.read();
		if (read >= 0) readPointer++;
		return read;
	}
	
	public int read(byte[] b) throws IOException {
		int read = in.read(b);
		if (read >= 0) readPointer += read;
		return read;
	}
	
	public int read(byte[] b, int off, int len) throws IOException {
		int read = in.read(b, off, len);
		if (read >= 0) readPointer += read;
		return read;
	}
	
	public BigInteger readBigInteger(int length, boolean signed, boolean le) throws IOException {
		byte[] data = new byte[length]; int m = length - 1; BigInteger value = BigInteger.ZERO;
		for (int i = 0; i < length; i++) { int b = read(); data[i] = (b < 0) ? 0 : (byte)b; }
		if (le) { for (int i = m; i >= 0; i--) value = value.shiftLeft(8).or(BigInteger.valueOf(data[i] & 0xFF)); }
		else    { for (int i = 0; i <= m; i++) value = value.shiftLeft(8).or(BigInteger.valueOf(data[i] & 0xFF)); }
		if (signed && value.testBit(length * 8 - 1)) value = value.or(BigInteger.valueOf(-1).shiftLeft(length * 8));
		return value;
	}
	
	public void reset() throws IOException {
		in.reset();
		readPointer = markPointer;
	}
	
	public long safeSkip(long n) throws IOException {
		long m = 0;
		byte[] tmp = new byte[65536];
		while (n > tmp.length) {
			int read = read(tmp);
			if (read < 0) return m;
			m += read; n -= read;
		}
		while (n > 0) {
			int read = read(tmp, 0, (int)n);
			if (read < 0) return m;
			m += read; n -= read;
		}
		return m;
	}
	
	public long skip(long n) throws IOException {
		long skipped = in.skip(n);
		if (skipped >= 0) readPointer += skipped;
		return skipped;
	}
}
