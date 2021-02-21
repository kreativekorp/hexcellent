package com.kreative.hexcellent.buffer;

public interface ByteBufferSelectionListener {
	public void selectionChanged(ByteBufferSelectionModel sm, long start, long end);
}
