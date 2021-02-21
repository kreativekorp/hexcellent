package com.kreative.hexcellent.test;

import java.io.ByteArrayOutputStream;
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

public class ByteBufferWriteTest {
	private static final Random random = new Random();
	
	private static ByteBuffer randomByteBuffer(int start, int end) throws IOException {
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
				return new RafByteBuffer(file, start - fileStart, end - start);
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
					buffers.add(randomByteBuffer(start, bufEnd));
					start = bufEnd;
				}
				return new CompositeByteBuffer(buffers);
		}
	}
	
	private static void testWrite() throws IOException {
		int start = random.nextInt(257);
		int end = start + random.nextInt(257 - start);
		byte[] expected = new byte[end - start];
		for (int i = 0, v = start; v < end; v++, i++) expected[i] = (byte)v;
		System.out.print("[" + toHexString(start) + "," + toHexString(end-1) + "]");
		
		ByteArrayOutputStream actual = new ByteArrayOutputStream();
		randomByteBuffer(start, end).write(actual, 0, end - start);
		System.out.print(Arrays.equals(expected, actual.toByteArray()) ? "\tPASS" : "\tFAIL\u0007");
		
		actual = new ByteArrayOutputStream();
		randomByteBuffer(0, 256).write(actual, start, end - start);
		System.out.print(Arrays.equals(expected, actual.toByteArray()) ? "\tPASS" : "\tFAIL\u0007");
		
		actual = new ByteArrayOutputStream();
		randomByteBuffer(start, end).write(actual, 0, end - start);
		System.out.print(Arrays.equals(expected, actual.toByteArray()) ? "\tPASS" : "\tFAIL\u0007");
		
		actual = new ByteArrayOutputStream();
		randomByteBuffer(0, 256).write(actual, start, end - start);
		System.out.print(Arrays.equals(expected, actual.toByteArray()) ? "\tPASS" : "\tFAIL\u0007");
		
		System.out.println();
	}
	
	public static void main(String[] args) throws IOException {
		for (;;) testWrite();
	}
	
	private static String toHexString(int v) {
		return Integer.toHexString(v | 0xF00).substring(1);
	}
}
