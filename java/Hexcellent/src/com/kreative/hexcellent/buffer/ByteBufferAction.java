package com.kreative.hexcellent.buffer;

public abstract class ByteBufferAction {
	protected final String name;
	
	protected ByteBufferAction(String name) {
		this.name = name;
	}
	
	public final String getName() { return name; }
	public final String toString() { return name; }
	
	public abstract void redo();
	public abstract void undo();
}
