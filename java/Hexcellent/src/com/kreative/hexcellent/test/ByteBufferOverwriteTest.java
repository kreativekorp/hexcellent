package com.kreative.hexcellent.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import com.kreative.hexcellent.buffer.ArrayByteBuffer;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.CompositeByteBuffer;
import com.kreative.hexcellent.buffer.RafByteBuffer;

public class ByteBufferOverwriteTest {
	private static final Random random = new Random();
	
	private static ByteBuffer randomByteBuffer(int start, int end, boolean editable) throws IOException {
		switch (random.nextInt(3)) {
			case 0:
				int fileStart = random.nextInt(start + 1);
				int fileEnd = end + random.nextInt(257 - end);
				File file = File.createTempFile("ByteBufferReadTest", ".bin");
				FileOutputStream out = new FileOutputStream(file);
				for (int i = fileStart; i < fileEnd; i++) out.write(i);
				out.flush();
				out.close();
				file.deleteOnExit();
				RafByteBuffer raf = new RafByteBuffer(file, start - fileStart, end - start);
				return editable ? new CompositeByteBuffer(raf) : raf;
			case 1:
				int arrStart = random.nextInt(start + 1);
				int arrEnd = end + random.nextInt(257 - end);
				byte[] arr = new byte[arrEnd - arrStart];
				for (int i = 0, v = arrStart; v < arrEnd; v++, i++) arr[i] = (byte)v;
				return new ArrayByteBuffer(arr, start - arrStart, end - start);
			default:
				List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
				while (start < end) {
					int bufEnd = start + random.nextInt(end - start + 1);
					buffers.add(randomByteBuffer(start, bufEnd, false));
					start = bufEnd;
				}
				return new CompositeByteBuffer(buffers);
		}
	}
	
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
