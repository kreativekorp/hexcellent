package com.kreative.hexcellent.test;

import java.io.IOException;
import java.util.Arrays;
import com.kreative.hexcellent.buffer.ByteBuffer;

public class ByteBufferOverwriteTest extends ByteBufferTestBase {
	private static void testOverwrite() throws IOException {
		int start = random.nextInt(257);
		int end = start + random.nextInt(257 - start);
		byte repl = (byte)random.nextInt(256);
		byte[] expected = new byte[256];
		for (int i = 0; i < 256; i++) expected[i] = (byte)i;
		for (int i = start; i < end; i++) expected[i] = repl;
		System.out.print("[" + toHexString(start) + "," + toHexString(end-1) + "]");
		System.out.print("\t" + (end - start <= 0) + "\t" + (end - start >= 256) + "\t" + (end - start));
		
		ByteBuffer b = randomByteBuffer(0, 256, true);
		byte[] src = new byte[end - start];
		for (int i = 0; i < end - start; i++) src[i] = repl;
		b.overwrite(start, src, 0, end - start);
		byte[] actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		
		int padding = random.nextInt(256) + 1;
		int offset = random.nextInt(padding);
		
		b = randomByteBuffer(0, 256, true);
		src = new byte[end - start + padding];
		for (int i = 0; i < end - start; i++) src[offset + i] = repl;
		b.overwrite(start, src, offset, end - start);
		actual = new byte[(int)b.length()];
		b.get(0, actual, 0, (int)b.length());
		System.out.print(Arrays.equals(expected, actual) ? "\tPASS" : "\tFAIL\u0007");
		
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		for (;;) testOverwrite();
	}
	
	private static String toHexString(int v) {
		return Integer.toHexString(v | 0xF00).substring(1);
	}
}
