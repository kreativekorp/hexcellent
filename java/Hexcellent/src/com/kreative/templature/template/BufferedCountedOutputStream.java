package com.kreative.templature.template;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class BufferedCountedOutputStream extends OutputStream {
	private final BufferedOutputStream out;
	private long writePointer = 0;
	
	public BufferedCountedOutputStream(OutputStream out) {
		this.out = new BufferedOutputStream(out);
	}
	
	public BufferedCountedOutputStream(OutputStream out, int size) {
		this.out = new BufferedOutputStream(out, size);
	}
	
	public BufferedCountedOutputStream(BufferedOutputStream out) {
		this.out = out;
	}
	
	public void align(int mod) throws IOException {
		int r = (int)(writePointer % mod);
		if (r == 0) return;
		write(new byte[mod - r]);
	}
	
	public void align(int b, int mod) throws IOException {
		int r = (int)(writePointer % mod);
		if (r == 0) return;
		int n = mod - r;
		byte[] buf = new byte[n];
		for (int i = 0; i < n; i++) buf[i] = (byte)b;
		write(buf, 0, n);
	}
	
	public void align(byte[] b, int mod) throws IOException {
		int r = (int)(writePointer % mod);
		if (r == 0) return;
		write(b, 0, mod - r);
	}
	
	public void align(byte[] b, int off, int mod) throws IOException {
		int r = (int)(writePointer % mod);
		if (r == 0) return;
		write(b, off, mod - r);
	}
	
	public void alignBigInteger(BigInteger fill, int mod, boolean le) throws IOException {
		int r = (int)(writePointer % mod);
		if (r == 0) return;
		writeBigInteger(fill, mod - r, le);
	}
	
	public void close() throws IOException {
		out.close();
	}
	
	public void flush() throws IOException {
		out.flush();
	}
	
	public void write(byte[] b) throws IOException {
		out.write(b);
		writePointer += b.length;
	}
	
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
		writePointer += len;
	}
	
	public void write(int b) throws IOException {
		out.write(b);
		writePointer++;
	}
	
	public void writeBigInteger(BigInteger value, int length, boolean le) throws IOException {
		byte[] data = new byte[length]; int m = length - 1;
		if (le) { for (int i = 0; i <= m; i++) { data[i] = value.byteValue(); value = value.shiftRight(8); } }
		else    { for (int i = m; i >= 0; i--) { data[i] = value.byteValue(); value = value.shiftRight(8); } }
		write(data, 0, length);
	}
}
