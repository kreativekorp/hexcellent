package com.kreative.hexcellent.buffer;

import java.io.IOException;

public interface ByteDecoder {
	public abstract byte[] decode(String data) throws IOException;
}
