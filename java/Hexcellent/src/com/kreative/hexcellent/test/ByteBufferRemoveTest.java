package com.kreative.hexcellent.test;

import java.io.IOException;
import java.util.Arrays;
import com.kreative.hexcellent.buffer.ByteBuffer;

public class ByteBufferRemoveTest extends ByteBufferTestBase {
	private static void testRemove() throws IOException {
		int start = random.nextInt(257);
		int end = start + random.nextInt(257 - start);
		byte[] expected = new byte[256 - (end - start)];
		for (int i = 0, v = 0; v < start; v++, i++) expected[i] = (byte)v;
		for (int i = start, v = end; v < 256; v++, i++) expected[i] = (byte)v;
		System.out.print("[" + toHexString(start) + "," + toHexString(end-1) + "]");
		System.out.print("\t" + (expected.length <= 0) + "\t" + expected.length);
		
		ByteBuffer b = randomByteBuffer(0, 256, true);
		b.remove(start, end - start);
		byte[] actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		for (;;) testRemove();
	}
	
	private static String toHexString(int v) {
		return Integer.toHexString(v | 0xF00).substring(1);
	}
}
