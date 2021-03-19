package com.kreative.experimental.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.kreative.experimental.KDataInputStream;
import com.kreative.experimental.KDataOutputStream;
import com.kreative.hexcellent.buffer.FloatFormat;

public class KDataInputTest {
	private static final int[][] BEB_TESTS = {
		{0x00, 0x00, 0x00},
		{0x7F, -1, 0x7F},
		{0x80, 0x80, 0x81, 0x00},
		{0x2000, -0x2000, 0xC0, 0x00},
		{0x3FFF, -0x0001, 0xFF, 0x7F},
		{0x4000, 0x4000, 0x81, 0x80, 0x00},
		{0x1FFFFF, -1, 0xFF, 0xFF, 0x7F},
		{0x200000, 0x200000, 0x81, 0x80, 0x80, 0x00},
		{0x8000000, -0x8000000, 0xC0, 0x80, 0x80, 0x00},
		{0xFFFFFFF, -0x0000001, 0xFF, 0xFF, 0xFF, 0x7F},
		{624485, 624485, 0xA6, 0x8E, 0x65},
		{1973696, -123456, 0xF8, 0xBB, 0x40},
	};
	
	private static final int[][] LEB_TESTS = {
		{0x00, 0x00, 0x00},
		{0x7F, -1, 0x7F},
		{0x80, 0x80, 0x80, 0x01},
		{0x2000, -0x2000, 0x80, 0x40},
		{0x3FFF, -0x0001, 0xFF, 0x7F},
		{0x4000, 0x4000, 0x80, 0x80, 0x01},
		{0x1FFFFF, -1, 0xFF, 0xFF, 0x7F},
		{0x200000, 0x200000, 0x80, 0x80, 0x80, 0x01},
		{0x8000000, -0x8000000, 0x80, 0x80, 0x80, 0x40},
		{0xFFFFFFF, -0x0000001, 0xFF, 0xFF, 0xFF, 0x7F},
		{624485, 624485, 0xE5, 0x8E, 0x26},
		{1973696, -123456, 0xC0, 0xBB, 0x78},
	};
	
	private static final Number[][] FP128_TESTS = {
		{0x00, null, null, FloatFormat.Zero.POSITIVE_ZERO},
		{0x01, null, null, BigInteger.valueOf(1)},
		{0x02, null, 999, BigInteger.valueOf(999)},
		{0x21, 10, null, BigDecimal.valueOf(1024.0)},
		{0x22, 10, 999, BigDecimal.valueOf(999*1024.0)},
		{0x29, 10, null, BigDecimal.valueOf(0.0009765625)},
		{0x2A, 10, 999, BigDecimal.valueOf(999*0.0009765625)},
		{0x31, 10, null, new BigDecimal("1E10")},
		{0x32, 10, 999, new BigDecimal("999E10")},
		{0x39, 10, null, new BigDecimal("1E-10")},
		{0x3A, 10, 999, new BigDecimal("999E-10")},
		{0x40, null, null, FloatFormat.NaN.POSITIVE_INFINITY},
		{0x41, null, null, new FloatFormat.NaN(false, false, BigInteger.valueOf(1))},
		{0x42, null, 999, new FloatFormat.NaN(false, false, BigInteger.valueOf(999))},
		{0x44, null, null, new FloatFormat.NaN(false, true, BigInteger.valueOf(0))},
		{0x45, null, null, new FloatFormat.NaN(false, true, BigInteger.valueOf(1))},
		{0x46, null, 999, new FloatFormat.NaN(false, true, BigInteger.valueOf(999))},
		{0x80, null, null, FloatFormat.Zero.NEGATIVE_ZERO},
		{0x81, null, null, BigInteger.valueOf(-1)},
		{0x82, null, 999, BigInteger.valueOf(-999)},
		{0xA1, 10, null, BigDecimal.valueOf(-1024.0)},
		{0xA2, 10, 999, BigDecimal.valueOf(-999*1024.0)},
		{0xA9, 10, null, BigDecimal.valueOf(-0.0009765625)},
		{0xAA, 10, 999, BigDecimal.valueOf(-999*0.0009765625)},
		{0xB1, 10, null, new BigDecimal("-1E10")},
		{0xB2, 10, 999, new BigDecimal("-999E10")},
		{0xB9, 10, null, new BigDecimal("-1E-10")},
		{0xBA, 10, 999, new BigDecimal("-999E-10")},
		{0xC0, null, null, FloatFormat.NaN.NEGATIVE_INFINITY},
		{0xC1, null, null, new FloatFormat.NaN(true, false, BigInteger.valueOf(1))},
		{0xC2, null, 999, new FloatFormat.NaN(true, false, BigInteger.valueOf(999))},
		{0xC4, null, null, new FloatFormat.NaN(true, true, BigInteger.valueOf(0))},
		{0xC5, null, null, new FloatFormat.NaN(true, true, BigInteger.valueOf(1))},
		{0xC6, null, 999, new FloatFormat.NaN(true, true, BigInteger.valueOf(999))},
	};
	
	public static void main(String[] args) throws IOException {
		for (int[] td : BEB_TESTS) {
			byte[] data = new byte[td.length];
			for (int i = 0; i < td.length; i++) data[i] = (byte)td[i];
			ByteArrayInputStream bin = new ByteArrayInputStream(data, 2, td.length - 2);
			KDataInputStream kin = new KDataInputStream(bin);
			BigInteger v = kin.readUnsignedBEB128();
			kin.close();
			boolean pass = v.equals(BigInteger.valueOf(td[0]));
			System.out.print(pass ? "PASS " : ("FAIL: [ " + v + " != " + td[0] + " ] "));
			
			bin = new ByteArrayInputStream(data, 2, td.length - 2);
			kin = new KDataInputStream(bin);
			v = kin.readSignedBEB128();
			kin.close();
			pass = v.equals(BigInteger.valueOf(td[1]));
			System.out.print(pass ? "PASS " : ("FAIL: [ " + v + " != " + td[1] + " ] "));
		}
		System.out.println();
		
		for (int[] td : LEB_TESTS) {
			byte[] data = new byte[td.length];
			for (int i = 0; i < td.length; i++) data[i] = (byte)td[i];
			ByteArrayInputStream bin = new ByteArrayInputStream(data, 2, td.length - 2);
			KDataInputStream kin = new KDataInputStream(bin);
			BigInteger v = kin.readUnsignedLEB128();
			kin.close();
			boolean pass = v.equals(BigInteger.valueOf(td[0]));
			System.out.print(pass ? "PASS " : ("FAIL: [ " + v + " != " + td[0] + " ] "));
			
			bin = new ByteArrayInputStream(data, 2, td.length - 2);
			kin = new KDataInputStream(bin);
			v = kin.readSignedLEB128();
			kin.close();
			pass = v.equals(BigInteger.valueOf(td[1]));
			System.out.print(pass ? "PASS " : ("FAIL: [ " + v + " != " + td[1] + " ] "));
		}
		System.out.println();
		
		for (Number[] td : FP128_TESTS) {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			KDataOutputStream kout = new KDataOutputStream(bout);
			kout.writeByte(td[0].byteValue());
			if (td[1] != null) kout.writeUnsignedBEB128(BigInteger.valueOf(td[1].intValue()));
			if (td[2] != null) kout.writeUnsignedBEB128(BigInteger.valueOf(td[2].intValue()));
			kout.close();
			byte[] data = bout.toByteArray();
			
			ByteArrayInputStream bin = new ByteArrayInputStream(data);
			KDataInputStream kin = new KDataInputStream(bin);
			Number actual = kin.readBEFP128();
			Number expected = td[3];
			kin.close();
			final boolean pass1;
			if (actual instanceof BigDecimal && expected instanceof BigDecimal) {
				pass1 = ((BigDecimal)actual).compareTo((BigDecimal)expected) == 0;
			} else {
				pass1 = actual.equals(expected);
			}
			System.out.print(pass1 ? "PASS " : ("FAIL: [ " + actual + " != " + expected + " ] "));
			
			bout = new ByteArrayOutputStream();
			kout = new KDataOutputStream(bout);
			kout.writeByte(td[0].byteValue());
			if (td[1] != null) kout.writeUnsignedLEB128(BigInteger.valueOf(td[1].intValue()));
			if (td[2] != null) kout.writeUnsignedLEB128(BigInteger.valueOf(td[2].intValue()));
			kout.close();
			data = bout.toByteArray();
			
			bin = new ByteArrayInputStream(data);
			kin = new KDataInputStream(bin);
			actual = kin.readLEFP128();
			expected = td[3];
			kin.close();
			final boolean pass2;
			if (actual instanceof BigDecimal && expected instanceof BigDecimal) {
				pass2 = ((BigDecimal)actual).compareTo((BigDecimal)expected) == 0;
			} else {
				pass2 = actual.equals(expected);
			}
			System.out.print(pass2 ? "PASS " : ("FAIL: [ " + actual + " != " + expected + " ] "));
		}
		System.out.println();
	}
}
