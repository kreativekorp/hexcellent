package com.kreative.hexcellent.buffer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class RafByteBuffer extends ByteBuffer {
	private static final int WRITE_BUFFER_SIZE = (1 << 20); // 1MB
	
	private final RandomAccessFile raf;
	private final long baseOffset;
	private final long baseLength;
	
	public RafByteBuffer(File file) throws IOException {
		this(new RandomAccessFile(file, "r"));
	}
	
	public RafByteBuffer(File file, long baseOffset, long baseLength) throws IOException {
		this(new RandomAccessFile(file, "r"), baseOffset, baseLength);
	}
	
	public RafByteBuffer(RandomAccessFile raf) throws IOException {
		this(raf, 0, raf.length());
	}
	
	public RafByteBuffer(RandomAccessFile raf, long baseOffset, long baseLength) {
		this.raf = raf;
		this.baseOffset = baseOffset;
		this.baseLength = baseLength;
	}
	
	@Override
	public boolean isEmpty() {
		return baseLength <= 0;
	}
	
	@Override
	public long length() {
		return baseLength;
	}
	
	@Override
	public boolean get(long offset, byte[] dst, int dstOffset, int length) {
		if (length <= 0) return true;
		try {
			raf.seek(baseOffset + offset);
			raf.readFully(dst, dstOffset, length);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public boolean insert(long offset, byte[] src, int srcOffset, int length) {
		if (length <= 0) return true;
		return false;
	}
	
	@Override
	public boolean overwrite(long offset, byte[] src, int srcOffset, int length) {
		if (length <= 0) return true;
		return false;
	}
	
	@Override
	public boolean remove(long offset, long length) {
		if (length <= 0) return true;
		return false;
	}
	
	@Override
	public RafByteBuffer slice(long offset, long length) {
		return new RafByteBuffer(raf, baseOffset + offset, length);
	}
	
	@Override
	public boolean write(OutputStream out, long offset, long length) throws IOException {
		if (length <= 0) return true;
		raf.seek(baseOffset + offset);
		byte[] tmp = new byte[WRITE_BUFFER_SIZE];
		while (length > WRITE_BUFFER_SIZE) {
			raf.readFully(tmp, 0, WRITE_BUFFER_SIZE);
			out.write(tmp, 0, WRITE_BUFFER_SIZE);
			length -= WRITE_BUFFER_SIZE;
		}
		if (length > 0) {
			raf.readFully(tmp, 0, (int)length);
			out.write(tmp, 0, (int)length);
		}
		return true;
	}
}
