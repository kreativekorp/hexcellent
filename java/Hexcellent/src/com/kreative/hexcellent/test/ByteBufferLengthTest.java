package com.kreative.hexcellent.test;

import java.io.IOException;

public class ByteBufferLengthTest extends ByteBufferTestBase {
	private static void testLength() throws IOException {
		int start = random.nextInt(257);
		int end = start + random.nextInt(257 - start);
		System.out.print("[" + toHexString(start) + "," + toHexString(end-1) + "]");
		
		boolean isEmpty = randomByteBuffer(start, end, false).isEmpty();
		System.out.print("\t" + isEmpty);
		System.out.print((isEmpty == (end == start)) ? "\tPASS" : "\tFAIL\u0007");
		
		long length = randomByteBuffer(start, end, false).length();
		System.out.print("\t" + length);
		System.out.print((length == (end - start)) ? "\tPASS" : "\tFAIL\u0007");
		
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		for (;;) testLength();
	}
	
	private static String toHexString(int v) {
		return Integer.toHexString(v | 0xF00).substring(1);
	}
}
