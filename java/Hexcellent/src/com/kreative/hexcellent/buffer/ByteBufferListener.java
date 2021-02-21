package com.kreative.hexcellent.buffer;

public interface ByteBufferListener {
	public void dataInserted(ByteBuffer buffer, long offset, int length);
	public void dataOverwritten(ByteBuffer buffer, long offset, int length);
	public void dataRemoved(ByteBuffer buffer, long offset, long length);
}
