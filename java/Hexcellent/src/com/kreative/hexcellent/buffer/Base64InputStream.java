package com.kreative.hexcellent.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Base64InputStream extends InputStream {
	public static final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	public static final String BASE64URL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
	public static final String RFC3501 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+,";
	
	private final CharacterIterator ci;
	private final InputStream in;
	private final String alphabet;
	private final boolean padb;
	private final char padc;
	
	public Base64InputStream(String s) {
		this(s, BASE64, '=');
	}
	
	public Base64InputStream(CharacterIterator ci) {
		this(ci, BASE64, '=');
	}
	
	public Base64InputStream(InputStream in) {
		this(in, BASE64, '=');
	}
	
	public Base64InputStream(String s, String alphabet, char pad) {
		this.ci = new StringCharacterIterator(s); this.in = null; this.alphabet = alphabet;
		if (this.alphabet.length() != 64) throw new IllegalArgumentException(alphabet);
		this.padb = (pad > '\u0000' && pad < '\uFFFD'); this.padc = pad;
	}
	
	public Base64InputStream(CharacterIterator ci, String alphabet, char pad) {
		this.ci = ci; this.in = null; this.alphabet = alphabet;
		if (this.alphabet.length() != 64) throw new IllegalArgumentException(alphabet);
		this.padb = (pad > '\u0000' && pad < '\uFFFD'); this.padc = pad;
	}
	
	public Base64InputStream(InputStream in, String alphabet, char pad) {
		this.ci = null; this.in = in; this.alphabet = alphabet;
		if (this.alphabet.length() != 64) throw new IllegalArgumentException(alphabet);
		this.padb = (pad > '\u0000' && pad < '\uFFFD'); this.padc = pad;
	}
	
	private int word = 0;
	private int count = 0;
	private boolean eof = false;
	
	@Override
	public int read() throws IOException {
		for (;;) {
			if (count > 0) {
				word <<= 8;
				count--;
				return (word >>> 24);
			}
			if (eof) return -1;
			readWord();
		}
	}
	
	private void readWord() throws IOException {
		for (;;) {
			int c = -1;
			if (ci != null) { c = ci.current(); ci.next(); }
			if (in != null) { c = in.read(); }
			if (c < 0 || c == CharacterIterator.DONE || (padb && padc == c)) {
				padWord();
				eof = true;
				return;
			} else if ((c = alphabet.indexOf(c)) >= 0) {
				word <<= 6;
				word |= c;
				count++;
				if (count > 3) {
					count = 3;
					return;
				}
			}
		}
	}
	
	private void padWord() {
		if (count > 0) {
			for (int i = count; i <= 3; i++) word <<= 6;
			count--;
		}
	}
	
	@Override
	public void close() throws IOException {
		if (in != null) in.close();
	}
}
