package com.kreative.hexcellent.buffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class MMapByteBuffer extends ByteBuffer {
	private static final int WRITE_BUFFER_SIZE = (1 << 20); // 1MB
	
	private final MappedByteBuffer mbb;
	private final long baseOffset;
	private final long baseLength;
	
	public MMapByteBuffer(FileChannel fc, long baseOffset, long baseLength) throws IOException {
		this(fc.map(MapMode.READ_ONLY, baseOffset, baseLength), 0, baseLength);
	}
	
	public MMapByteBuffer(MappedByteBuffer mbb, long baseOffset, long baseLength) {
		this.mbb = mbb;
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
		mbb.position((int)(baseOffset + offset));
		mbb.get(dst, dstOffset, length);
		return true;
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
	public MMapByteBuffer slice(long offset, long length) {
		return new MMapByteBuffer(mbb, baseOffset + offset, length);
	}
	
	@Override
	public boolean write(OutputStream out, long offset, long length) throws IOException {
		if (length <= 0) return true;
		mbb.position((int)(baseOffset + offset));
		byte[] tmp = new byte[WRITE_BUFFER_SIZE];
		while (length > WRITE_BUFFER_SIZE) {
			mbb.get(tmp, 0, WRITE_BUFFER_SIZE);
			out.write(tmp, 0, WRITE_BUFFER_SIZE);
			length -= WRITE_BUFFER_SIZE;
		}
		if (length > 0) {
			mbb.get(tmp, 0, (int)length);
			out.write(tmp, 0, (int)length);
		}
		return true;
	}
}
