package com.kreative.hexcellent.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.kreative.hexcellent.buffer.ArrayByteBuffer;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.CompositeByteBuffer;
import com.kreative.hexcellent.buffer.RafByteBuffer;

public abstract class ByteBufferTestBase {
	protected static final Random random = new Random();
	
	protected static ByteBuffer randomByteBuffer(int start, int end, boolean editable) throws IOException {
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
}
