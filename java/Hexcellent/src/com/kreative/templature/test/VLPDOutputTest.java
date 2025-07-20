package com.kreative.templature.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import com.kreative.templature.template.BufferedCountedInputStream;
import com.kreative.templature.template.BufferedCountedOutputStream;
import com.kreative.templature.template.Closure;
import com.kreative.templature.template.item.VLPDItem;

public class VLPDOutputTest extends AbstractTest {
	private static final class B100TestCase {
		public final BigInteger value;
		public final byte[] ubeb100;
		public final byte[] uleb100;
		public B100TestCase(int value, int[] ubeb100, int[] uleb100) {
			this.value = BigInteger.valueOf(value);
			this.ubeb100 = new byte[ubeb100.length];
			for (int i = 0; i < ubeb100.length; i++) this.ubeb100[i] = (byte)ubeb100[i];
			this.uleb100 = new byte[uleb100.length];
			for (int i = 0; i < uleb100.length; i++) this.uleb100[i] = (byte)uleb100[i];
		}
	}
	
	private static final B100TestCase[] B100_TESTS = {
		new B100TestCase(     0, new int[]{0x00}, new int[]{0x00}),
		new B100TestCase(     7, new int[]{0x07}, new int[]{0x07}),
		new B100TestCase(     8, new int[]{0x08}, new int[]{0x08}),
		new B100TestCase(     9, new int[]{0x09}, new int[]{0x09}),
		new B100TestCase(    70, new int[]{0x70}, new int[]{0x70}),
		new B100TestCase(    77, new int[]{0x77}, new int[]{0x77}),
		new B100TestCase(    78, new int[]{0x78}, new int[]{0x78}),
		new B100TestCase(    79, new int[]{0x79}, new int[]{0x79}),
		new B100TestCase(    80, new int[]{0x0A}, new int[]{0x0A}),
		new B100TestCase(    87, new int[]{0x6B}, new int[]{0x6B}),
		new B100TestCase(    88, new int[]{0x4E}, new int[]{0x4E}),
		new B100TestCase(    89, new int[]{0x4F}, new int[]{0x4F}),
		new B100TestCase(    90, new int[]{0x1A}, new int[]{0x1A}),
		new B100TestCase(    97, new int[]{0x7B}, new int[]{0x7B}),
		new B100TestCase(    98, new int[]{0x5E}, new int[]{0x5E}),
		new B100TestCase(    99, new int[]{0x5F}, new int[]{0x5F}),
		new B100TestCase(   100, new int[]{0x81, 0x00}, new int[]{0x80, 0x01}),
		new B100TestCase(   123, new int[]{0x81, 0x23}, new int[]{0xA3, 0x01}),
		new B100TestCase(   234, new int[]{0x82, 0x34}, new int[]{0xB4, 0x02}),
		new B100TestCase(   345, new int[]{0x83, 0x45}, new int[]{0xC5, 0x03}),
		new B100TestCase(   456, new int[]{0x84, 0x56}, new int[]{0xD6, 0x04}),
		new B100TestCase(   567, new int[]{0x85, 0x67}, new int[]{0xE7, 0x05}),
		new B100TestCase(   678, new int[]{0x86, 0x78}, new int[]{0xF8, 0x06}),
		new B100TestCase(   789, new int[]{0x87, 0x4F}, new int[]{0xCF, 0x07}),
		new B100TestCase(   899, new int[]{0x88, 0x5F}, new int[]{0xDF, 0x08}),
		new B100TestCase(   999, new int[]{0x89, 0x5F}, new int[]{0xDF, 0x09}),
		new B100TestCase(  1000, new int[]{0x90, 0x00}, new int[]{0x80, 0x10}),
		new B100TestCase(  1234, new int[]{0x92, 0x34}, new int[]{0xB4, 0x12}),
		new B100TestCase(  2345, new int[]{0xA3, 0x45}, new int[]{0xC5, 0x23}),
		new B100TestCase(  3456, new int[]{0xB4, 0x56}, new int[]{0xD6, 0x34}),
		new B100TestCase(  4567, new int[]{0xC5, 0x67}, new int[]{0xE7, 0x45}),
		new B100TestCase(  5678, new int[]{0xD6, 0x78}, new int[]{0xF8, 0x56}),
		new B100TestCase(  6789, new int[]{0xE7, 0x4F}, new int[]{0xCF, 0x67}),
		new B100TestCase(  7899, new int[]{0xF8, 0x5F}, new int[]{0xDF, 0x78}),
		new B100TestCase(  8999, new int[]{0xCF, 0x5F}, new int[]{0xDF, 0x4F}),
		new B100TestCase(  9999, new int[]{0xDF, 0x5F}, new int[]{0xDF, 0x5F}),
		new B100TestCase( 10000, new int[]{0x81, 0x80, 0x00}, new int[]{0x80, 0x80, 0x01}),
		new B100TestCase( 12345, new int[]{0x81, 0xA3, 0x45}, new int[]{0xC5, 0xA3, 0x01}),
		new B100TestCase( 23456, new int[]{0x82, 0xB4, 0x56}, new int[]{0xD6, 0xB4, 0x02}),
		new B100TestCase( 34567, new int[]{0x83, 0xC5, 0x67}, new int[]{0xE7, 0xC5, 0x03}),
		new B100TestCase( 45678, new int[]{0x84, 0xD6, 0x78}, new int[]{0xF8, 0xD6, 0x04}),
		new B100TestCase( 56789, new int[]{0x85, 0xE7, 0x4F}, new int[]{0xCF, 0xE7, 0x05}),
		new B100TestCase( 67899, new int[]{0x86, 0xF8, 0x5F}, new int[]{0xDF, 0xF8, 0x06}),
		new B100TestCase( 78999, new int[]{0x87, 0xCF, 0x5F}, new int[]{0xDF, 0xCF, 0x07}),
		new B100TestCase( 89999, new int[]{0x88, 0xDF, 0x5F}, new int[]{0xDF, 0xDF, 0x08}),
		new B100TestCase( 99999, new int[]{0x89, 0xDF, 0x5F}, new int[]{0xDF, 0xDF, 0x09}),
		new B100TestCase(100000, new int[]{0x90, 0x80, 0x00}, new int[]{0x80, 0x80, 0x10}),
		new B100TestCase(123456, new int[]{0x92, 0xB4, 0x56}, new int[]{0xD6, 0xB4, 0x12}),
		new B100TestCase(234567, new int[]{0xA3, 0xC5, 0x67}, new int[]{0xE7, 0xC5, 0x23}),
		new B100TestCase(345678, new int[]{0xB4, 0xD6, 0x78}, new int[]{0xF8, 0xD6, 0x34}),
		new B100TestCase(456789, new int[]{0xC5, 0xE7, 0x4F}, new int[]{0xCF, 0xE7, 0x45}),
		new B100TestCase(567899, new int[]{0xD6, 0xF8, 0x5F}, new int[]{0xDF, 0xF8, 0x56}),
		new B100TestCase(678999, new int[]{0xE7, 0xCF, 0x5F}, new int[]{0xDF, 0xCF, 0x67}),
		new B100TestCase(789999, new int[]{0xF8, 0xDF, 0x5F}, new int[]{0xDF, 0xDF, 0x78}),
		new B100TestCase(899999, new int[]{0xCF, 0xDF, 0x5F}, new int[]{0xDF, 0xDF, 0x4F}),
		new B100TestCase(999999, new int[]{0xDF, 0xDF, 0x5F}, new int[]{0xDF, 0xDF, 0x5F}),
	};
	
	private static BufferedCountedInputStream emptyStream() {
		return new BufferedCountedInputStream(new ByteArrayInputStream(new byte[0]));
	}
	
	public void run() throws Exception {
		for (B100TestCase td : B100_TESTS) {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			BufferedCountedOutputStream os = new BufferedCountedOutputStream(bs);
			VLPDItem item = new VLPDItem(0, "", "", false, 10);
			VLPDItem.Instance inst = item.read(new Closure(null), emptyStream());
			inst.setValue(td.value); inst.write(os); os.close();
			expectEquals(td.value, bs.toByteArray(), td.ubeb100);
			
			bs = new ByteArrayOutputStream();
			os = new BufferedCountedOutputStream(bs);
			item = new VLPDItem(0, "", "", true, 10);
			inst = item.read(new Closure(null), emptyStream());
			inst.setValue(td.value); inst.write(os); os.close();
			expectEquals(td.value, bs.toByteArray(), td.uleb100);
		}
	}
	
	public static void main(String[] args) {
		new VLPDOutputTest().main();
	}
}
