package com.kreative.hexcellent.buffer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class ByteBuffer {
	public abstract boolean isEmpty();
	public abstract long length();
	public abstract boolean get(long offset, byte[] dst, int dstOffset, int length);
	public abstract boolean insert(long offset, byte[] src, int srcOffset, int length);
	public abstract boolean overwrite(long offset, byte[] src, int srcOffset, int length);
	public abstract boolean remove(long offset, long length);
	public abstract ByteBuffer slice(long offset, long length);
	public abstract boolean write(OutputStream out, long offset, long length) throws IOException;
	
	public final boolean transform(ByteTransform tx, long offset, int length) {
		byte[] data = new byte[length];
		return (
			get(offset, data, 0, length) &&
			tx.transform(data, 0, length) &&
			overwrite(offset, data, 0, length)
		);
	}
	
	private final List<ByteBufferListener> listeners = new ArrayList<ByteBufferListener>();
	
	public final void addByteBufferListener(ByteBufferListener listener) {
		listeners.add(listener);
	}
	
	public final void removeByteBufferListener(ByteBufferListener listener) {
		listeners.remove(listener);
	}
	
	protected final void fireDataInserted(long offset, int length) {
		for (ByteBufferListener l : listeners) l.dataInserted(this, offset, length);
	}
	
	protected final void fireDataOverwritten(long offset, int length) {
		for (ByteBufferListener l : listeners) l.dataOverwritten(this, offset, length);
	}
	
	protected final void fireDataRemoved(long offset, long length) {
		for (ByteBufferListener l : listeners) l.dataRemoved(this, offset, length);
	}
}
