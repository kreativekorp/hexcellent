package com.kreative.hexcellent.buffer;

import java.io.IOException;

public interface ByteEncoder {
	public abstract String encode(byte[] data, int offset, int length) throws IOException;
}
