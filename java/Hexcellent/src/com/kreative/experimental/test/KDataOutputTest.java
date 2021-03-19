package com.kreative.experimental.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import com.kreative.experimental.KDataOutputStream;
import com.kreative.hexcellent.buffer.FloatFormat;

public class KDataOutputTest {
	private static final class TestCase {
		public final BigInteger value;
		public final byte[] ubeb128;
		public final byte[] sbeb128;
		public final byte[] uleb128;
		public final byte[] sleb128;
		public TestCase(int value, int[] ubeb128, int[] sbeb128, int[] uleb128, int[] sleb128) {
			this.value = BigInteger.valueOf(value);
			this.ubeb128 = new byte[ubeb128.length];
			for (int i = 0; i < ubeb128.length; i++) this.ubeb128[i] = (byte)ubeb128[i];
			this.sbeb128 = new byte[sbeb128.length];
			for (int i = 0; i < sbeb128.length; i++) this.sbeb128[i] = (byte)sbeb128[i];
			this.uleb128 = new byte[uleb128.length];
			for (int i = 0; i < uleb128.length; i++) this.uleb128[i] = (byte)uleb128[i];
			this.sleb128 = new byte[sleb128.length];
			for (int i = 0; i < sleb128.length; i++) this.sleb128[i] = (byte)sleb128[i];
		}
	}
	
	private static final TestCase[] B128_TESTS = {
		new TestCase(
			0,
			new int[]{0},
			new int[]{0},
			new int[]{0},
			new int[]{0}
		),
		new TestCase(
			1,
			new int[]{1},
			new int[]{1},
			new int[]{1},
			new int[]{1}
		),
		new TestCase(
			-1,
			new int[]{0x7F},
			new int[]{0x7F},
			new int[]{0x7F},
			new int[]{0x7F}
		),
		new TestCase(
			127,
			new int[]{0x7F},
			new int[]{0x80, 0x7F},
			new int[]{0x7F},
			new int[]{0xFF, 0x00}
		),
		new TestCase(
			-128,
			new int[]{0x00},
			new int[]{0xFF, 0x00},
			new int[]{0x00},
			new int[]{0x80, 0x7F}
		),
		new TestCase(
			624485,
			new int[]{0xA6, 0x8E, 0x65},
			new int[]{0xA6, 0x8E, 0x65},
			new int[]{0xE5, 0x8E, 0x26},
			new int[]{0xE5, 0x8E, 0x26}
		),
		new TestCase(
			-123456,
			new int[]{0xF8, 0xBB, 0x40},
			new int[]{0xF8, 0xBB, 0x40},
			new int[]{0xC0, 0xBB, 0x78},
			new int[]{0xC0, 0xBB, 0x78}
		),
		new TestCase(
			1973696,
			new int[]{0xF8, 0xBB, 0x40},
			new int[]{0x80, 0xF8, 0xBB, 0x40},
			new int[]{0xC0, 0xBB, 0x78},
			new int[]{0xC0, 0xBB, 0xF8, 0x00}
		),
	};
	
	private static final Number[][] FP128_TESTS = {
		{0x00, null, null, FloatFormat.Zero.POSITIVE_ZERO, (byte)0, (short)0, 0, 0L, 0f, 0.0},
		{0x01, null, null, BigInteger.valueOf(1), (byte)1, (short)1, 1, 1L, 1f, 1.0},
		{0x02, null, 999, BigInteger.valueOf(999), (short)999, 999, 999L, 999f, 999.0},
		{0x21, 10, null, BigInteger.valueOf(1024), (short)1024, 1024, 1024L, 1024f, 1024.0},
		{0x22, 10, 999, BigInteger.valueOf(999<<10), 999<<10, 999L<<10L, 999*1024f, 999*1024.0},
		{0x29, 10, null, 0.0009765625f, 0.0009765625},
		{0x2A, 10, 999, 999*0.0009765625f, 999*0.0009765625},
		{0x31, 10, null, new BigDecimal("1E10"), new BigDecimal("10000000000")},
		{0x32, 10, 999, new BigDecimal("999E10"), new BigDecimal("9.99E12")},
		{0x39, 10, null, new BigDecimal("1E-10"), new BigDecimal("0.0000000001")},
		{0x3A, 10, 999, new BigDecimal("999E-10"), new BigDecimal("9.99E-8")},
		{0x40, null, null, FloatFormat.NaN.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
		{0x41, null, null, new FloatFormat.NaN(false, false, BigInteger.valueOf(1))},
		{0x42, null, 999, new FloatFormat.NaN(false, false, BigInteger.valueOf(999))},
		{0x44, null, null, FloatFormat.NaN.NaN, Float.NaN, Double.NaN, new FloatFormat.NaN(false, true, BigInteger.valueOf(0))},
		{0x45, null, null, new FloatFormat.NaN(false, true, BigInteger.valueOf(1))},
		{0x46, null, 999, new FloatFormat.NaN(false, true, BigInteger.valueOf(999))},
		{0x80, null, null, FloatFormat.Zero.NEGATIVE_ZERO, -0f, -0.0},
		{0x81, null, null, BigInteger.valueOf(-1), (byte)-1, (short)-1, -1, -1L, -1f, -1.0},
		{0x82, null, 999, BigInteger.valueOf(-999), (short)-999, -999, -999L, -999f, -999.0},
		{0xA1, 10, null, BigInteger.valueOf(-1024), (short)-1024, -1024, -1024L, -1024f, -1024.0},
		{0xA2, 10, 999, BigInteger.valueOf(-999<<10), -999<<10, -999L<<10L, -999*1024f, -999*1024.0},
		{0xA9, 10, null, -0.0009765625f, -0.0009765625},
		{0xAA, 10, 999, -999*0.0009765625f, -999*0.0009765625},
		{0xB1, 10, null, new BigDecimal("-1E10"), new BigDecimal("-10000000000")},
		{0xB2, 10, 999, new BigDecimal("-999E10"), new BigDecimal("-9.99E12")},
		{0xB9, 10, null, new BigDecimal("-1E-10"), new BigDecimal("-0.0000000001")},
		{0xBA, 10, 999, new BigDecimal("-999E-10"), new BigDecimal("-9.99E-8")},
		{0xC0, null, null, FloatFormat.NaN.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
		{0xC1, null, null, new FloatFormat.NaN(true, false, BigInteger.valueOf(1))},
		{0xC2, null, 999, new FloatFormat.NaN(true, false, BigInteger.valueOf(999))},
		{0xC4, null, null, new FloatFormat.NaN(true, true, BigInteger.valueOf(0))},
		{0xC5, null, null, new FloatFormat.NaN(true, true, BigInteger.valueOf(1))},
		{0xC6, null, 999, new FloatFormat.NaN(true, true, BigInteger.valueOf(999))},
	};
	
	public static void main(String[] args) throws IOException {
		for (TestCase td : B128_TESTS) {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			KDataOutputStream out = new KDataOutputStream(bs);
			out.writeUnsignedBEB128(td.value);
			out.close();
			boolean pass = Arrays.equals(bs.toByteArray(), td.ubeb128);
			System.out.print(pass ? "PASS " : "FAIL: ");
			if (!pass) { System.out.println(td.value); printHex(bs.toByteArray()); printHex(td.ubeb128); }
			
			bs = new ByteArrayOutputStream();
			out = new KDataOutputStream(bs);
			out.writeSignedBEB128(td.value);
			out.close();
			pass = Arrays.equals(bs.toByteArray(), td.sbeb128);
			System.out.print(pass ? "PASS " : "FAIL: ");
			if (!pass) { System.out.println(td.value); printHex(bs.toByteArray()); printHex(td.sbeb128); }
			
			bs = new ByteArrayOutputStream();
			out = new KDataOutputStream(bs);
			out.writeUnsignedLEB128(td.value);
			out.close();
			pass = Arrays.equals(bs.toByteArray(), td.uleb128);
			System.out.print(pass ? "PASS " : "FAIL: ");
			if (!pass) { System.out.println(td.value); printHex(bs.toByteArray()); printHex(td.uleb128); }
			
			bs = new ByteArrayOutputStream();
			out = new KDataOutputStream(bs);
			out.writeSignedLEB128(td.value);
			out.close();
			pass = Arrays.equals(bs.toByteArray(), td.sleb128);
			System.out.print(pass ? "PASS " : "FAIL: ");
			if (!pass) { System.out.println(td.value); printHex(bs.toByteArray()); printHex(td.sleb128); }
		}
		System.out.println();
		
		for (Number[] td : FP128_TESTS) {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			KDataOutputStream kout = new KDataOutputStream(bout);
			kout.writeByte(td[0].byteValue());
			if (td[1] != null) kout.writeUnsignedBEB128(BigInteger.valueOf(td[1].intValue()));
			if (td[2] != null) kout.writeUnsignedBEB128(BigInteger.valueOf(td[2].intValue()));
			kout.close();
			byte[] expected = bout.toByteArray();
			
			for (int i = 3; i < td.length; i++) {
				bout = new ByteArrayOutputStream();
				kout = new KDataOutputStream(bout);
				kout.writeBEFP128(td[i]);
				kout.close();
				byte[] actual = bout.toByteArray();
				boolean pass = Arrays.equals(expected, actual);
				System.out.print(pass ? "PASS " : "FAIL: ");
				if (!pass) { System.out.println(td[i]); printHex(expected); printHex(actual); }
			}
			
			bout = new ByteArrayOutputStream();
			kout = new KDataOutputStream(bout);
			kout.writeByte(td[0].byteValue());
			if (td[1] != null) kout.writeUnsignedLEB128(BigInteger.valueOf(td[1].intValue()));
			if (td[2] != null) kout.writeUnsignedLEB128(BigInteger.valueOf(td[2].intValue()));
			kout.close();
			expected = bout.toByteArray();
			
			for (int i = 3; i < td.length; i++) {
				bout = new ByteArrayOutputStream();
				kout = new KDataOutputStream(bout);
				kout.writeLEFP128(td[i]);
				kout.close();
				byte[] actual = bout.toByteArray();
				boolean pass = Arrays.equals(expected, actual);
				System.out.print(pass ? "PASS " : "FAIL: ");
				if (!pass) { System.out.println(td[i]); printHex(expected); printHex(actual); }
			}
		}
		System.out.println();
	}
	
	private static void printHex(byte[] data) {
		for (byte b : data) {
			String h = "00" + Integer.toHexString(b);
			h = h.substring(h.length() - 2).toUpperCase();
			System.out.print(h);
		}
		System.out.println();
	}
}
