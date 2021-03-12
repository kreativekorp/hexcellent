package com.kreative.hexcellent.test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import com.kreative.hexcellent.buffer.ArrayByteBuffer;
import com.kreative.hexcellent.buffer.ByteBufferDocument;

public class ByteBufferSearchTest {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String s;
		
		s = "Hellolololol worororororororlololololold.";
		testIndexOf(s, "lolol");
		testIndexOf(s, "roror");
		testIndexOf(s, "lolo");
		testIndexOf(s, "roro");
		testIndexOf(s, "olol");
		testIndexOf(s, "oror");
		testIndexOf(s, "lol");
		testIndexOf(s, "ror");
		testIndexOf(s, "lo");
		testIndexOf(s, "ro");
		testIndexOf(s, "ol");
		testIndexOf(s, "or");
		testIndexOf(s, "l");
		testIndexOf(s, "r");
		
		s = "Helløløløløl wørørørørørørørløløløløløld.";
		testIndexOf(s, "løløl");
		testIndexOf(s, "rørør");
		testIndexOf(s, "lølø");
		testIndexOf(s, "rørø");
		testIndexOf(s, "øløl");
		testIndexOf(s, "ørør");
		testIndexOf(s, "løl");
		testIndexOf(s, "rør");
		testIndexOf(s, "lø");
		testIndexOf(s, "rø");
		testIndexOf(s, "øl");
		testIndexOf(s, "ør");
		testIndexOf(s, "l");
		testIndexOf(s, "r");
		
		byte[] b = s.getBytes("ISO-8859-1");
		ArrayByteBuffer bb = new ArrayByteBuffer(b);
		byte[] p = new byte[0];
		byte[] q = new byte[]{-1,0,42};
		System.out.println((bb.indexOf(p) == 0) ? "PASS" : "\u0007FAIL: indexOf(p) == 0");
		System.out.println((bb.indexOf(p, 1) == 1) ? "PASS" : "\u0007FAIL: indexOf(p,i) == i");
		System.out.println((bb.lastIndexOf(p) == b.length) ? "PASS" : "\u0007FAIL: lastIndexOf(p) == n");
		System.out.println((bb.lastIndexOf(p, 1) == 1) ? "PASS" : "\u0007FAIL: lastIndexOf(p,i) == i");
		System.out.println((bb.indexOf(q) == -1) ? "PASS" : "\u0007FAIL: indexOf(q) == -1");
		System.out.println((bb.indexOf(q, 1) == -1) ? "PASS" : "\u0007FAIL: indexOf(q,i) == -1");
		System.out.println((bb.lastIndexOf(q) == -1) ? "PASS" : "\u0007FAIL: lastIndexOf(q) == -1");
		System.out.println((bb.lastIndexOf(q, 1) == -1) ? "PASS" : "\u0007FAIL: lastIndexOf(q,i) == -1");
	}
	
	private static void testIndexOf(String text, String pattern) throws UnsupportedEncodingException {
		byte[] b = text.getBytes("ISO-8859-1");
		ArrayByteBuffer bb = new ArrayByteBuffer(b);
		byte[] p = pattern.getBytes("ISO-8859-1");
		
		byte[] c = new byte[b.length];
		for (int i = 0; i < b.length; i++) c[i] = 32;
		for (long o = bb.indexOf(p); o >= 0; o = bb.indexOf(p, o + 1)) {
			c[(int)o] = '^';
			for (int i = 1; i < p.length; i++) {
				if (c[(int)(o+i)] == 32) c[(int)(o+i)] = '-';
			}
		}
		
		byte[] d = new byte[b.length];
		for (int i = 0; i < b.length; i++) d[i] = 32;
		for (long o = bb.lastIndexOf(p); o >= 0; o = bb.lastIndexOf(p, o - 1)) {
			d[(int)o] = '^';
			for (int i = 1; i < p.length; i++) {
				if (d[(int)(o+i)] == 32) d[(int)(o+i)] = '-';
			}
		}
		
		byte[] e = new byte[b.length];
		for (int i = 0; i < b.length; i++) e[i] = 32;
		for (int o = text.indexOf(pattern); o >= 0; o = text.indexOf(pattern, o + 1)) {
			e[o] = '^';
			for (int i = 1; i < p.length; i++) {
				if (e[o+i] == 32) e[o+i] = '-';
			}
		}
		
		byte[] f = new byte[b.length];
		for (int i = 0; i < b.length; i++) f[i] = 32;
		for (int o = text.lastIndexOf(pattern); o >= 0; o = text.lastIndexOf(pattern, o - 1)) {
			f[o] = '^';
			for (int i = 1; i < p.length; i++) {
				if (f[o+i] == 32) f[o+i] = '-';
			}
		}
		
		if (Arrays.equals(c, d) && Arrays.equals(d, e) && Arrays.equals(e, f)) {
			System.out.print("PASS ");
		} else {
			System.out.println("\u0007FAIL: indexOf/lastIndexOf");
			System.out.println(text);
			System.out.println(new String(c, "ISO-8859-1"));
			System.out.println(new String(d, "ISO-8859-1"));
			System.out.println(new String(e, "ISO-8859-1"));
			System.out.println(new String(f, "ISO-8859-1"));
		}
		
		testReplace(text, pattern, "");
		testReplace(text, pattern, "x");
		testReplace(text, pattern, "lol");
		testReplace(text, pattern, "løl");
		testReplace(text, pattern, pattern);
		testReplace(text, pattern, pattern + "x");
		testReplace(text, pattern, "x" + pattern);
		System.out.println();
	}
	
	private static void testReplace(String text, String pattern, String replacement) throws UnsupportedEncodingException {
		byte[] b = text.getBytes("ISO-8859-1");
		ArrayByteBuffer bb = new ArrayByteBuffer(b);
		byte[] p = pattern.getBytes("ISO-8859-1");
		byte[] r = replacement.getBytes("ISO-8859-1");
		bb.replace(p, r);
		int l = (int)bb.length();
		byte[] d = new byte[l];
		bb.get(0, d, 0, l);
		String newText = text.replace(pattern, replacement);
		byte[] e = newText.getBytes("ISO-8859-1");
		if (Arrays.equals(d, e)) {
			System.out.print("PASS ");
		} else {
			System.out.println("\u0007FAIL: ByteBuffer.replace");
			System.out.println(text);
			System.out.println(new String(d, "ISO-8859-1"));
			System.out.println(new String(e, "ISO-8859-1"));
		}
		
		b = text.getBytes("ISO-8859-1");
		bb = new ArrayByteBuffer(b);
		ByteBufferDocument doc = new ByteBufferDocument(bb);
		doc.replaceAll(p, r);
		doc.selectAll();
		byte[] done = doc.getSelection();
		doc.undo();
		doc.selectAll();
		byte[] undone = doc.getSelection();
		doc.redo();
		doc.selectAll();
		byte[] redone = doc.getSelection();
		if (Arrays.equals(e, done) && Arrays.equals(b, undone) && Arrays.equals(e, redone)) {
			System.out.print("PASS ");
		} else {
			System.out.println("\u0007FAIL: ByteBufferDocument.replaceAll");
			System.out.println(text);
			System.out.println(new String(done, "ISO-8859-1"));
			System.out.println(new String(undone, "ISO-8859-1"));
			System.out.println(new String(redone, "ISO-8859-1"));
		}
	}
}
