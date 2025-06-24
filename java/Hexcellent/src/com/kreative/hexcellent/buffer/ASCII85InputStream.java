package com.kreative.hexcellent.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class ASCII85InputStream extends InputStream {
	public static final String ASCII85 = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstu";
	public static final String ASCII85XML = "!w#$%|v()*+,-./0123456789:;{=}?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstu";
	public static final String RFC1924 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#$%&()*+-;<=>?@^_`{|}~";
	public static final String Z85 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-:+=^!/*?&<>()[]{}@%$#";
	
	private final CharacterIterator ci;
	private final InputStream in;
	private final String alphabet;
	private final boolean xb, yb, zb, tb;
	private final char xc, yc, zc, tc;
	
	public ASCII85InputStream(String s) {
		this(s, ASCII85, 'x', 'y', 'z', '~');
	}
	
	public ASCII85InputStream(CharacterIterator ci) {
		this(ci, ASCII85, 'x', 'y', 'z', '~');
	}
	
	public ASCII85InputStream(InputStream in) {
		this(in, ASCII85, 'x', 'y', 'z', '~');
	}
	
	public ASCII85InputStream(String s, String alphabet) {
		this(s, alphabet, '\u0000', '\u0000', '\u0000', '\u0000');
	}
	
	public ASCII85InputStream(CharacterIterator ci, String alphabet) {
		this(ci, alphabet, '\u0000', '\u0000', '\u0000', '\u0000');
	}
	
	public ASCII85InputStream(InputStream in, String alphabet) {
		this(in, alphabet, '\u0000', '\u0000', '\u0000', '\u0000');
	}
	
	public ASCII85InputStream(String s, String alphabet, char x, char y, char z, char t) {
		this.ci = new StringCharacterIterator(s); this.in = null; this.alphabet = alphabet;
		if (this.alphabet.length() != 85) throw new IllegalArgumentException(alphabet);
		this.xb = (x > '\u0000' && x < '\uFFFD'); this.xc = x;
		this.yb = (y > '\u0000' && y < '\uFFFD'); this.yc = y;
		this.zb = (z > '\u0000' && z < '\uFFFD'); this.zc = z;
		this.tb = (t > '\u0000' && t < '\uFFFD'); this.tc = t;
	}
	
	public ASCII85InputStream(CharacterIterator ci, String alphabet, char x, char y, char z, char t) {
		this.ci = ci; this.in = null; this.alphabet = alphabet;
		if (this.alphabet.length() != 85) throw new IllegalArgumentException(alphabet);
		this.xb = (x > '\u0000' && x < '\uFFFD'); this.xc = x;
		this.yb = (y > '\u0000' && y < '\uFFFD'); this.yc = y;
		this.zb = (z > '\u0000' && z < '\uFFFD'); this.zc = z;
		this.tb = (t > '\u0000' && t < '\uFFFD'); this.tc = t;
	}
	
	public ASCII85InputStream(InputStream in, String alphabet, char x, char y, char z, char t) {
		this.ci = null; this.in = in; this.alphabet = alphabet;
		if (this.alphabet.length() != 85) throw new IllegalArgumentException(alphabet);
		this.xb = (x > '\u0000' && x < '\uFFFD'); this.xc = x;
		this.yb = (y > '\u0000' && y < '\uFFFD'); this.yc = y;
		this.zb = (z > '\u0000' && z < '\uFFFD'); this.zc = z;
		this.tb = (t > '\u0000' && t < '\uFFFD'); this.tc = t;
	}
	
	private long word = 0;
	private int count = 0;
	private int rbyte = 0;
	private int rcount = 0;
	private boolean eof = false;
	
	@Override
	public int read() throws IOException {
		for (;;) {
			if (count > 0) {
				int b = (int)((word >> 24) & 0xFF);
				word <<= 8;
				count--;
				return b;
			}
			if (rcount > 0) {
				rcount--;
				return rbyte;
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
			if (c < 0 || c == CharacterIterator.DONE || (tb && tc == c)) {
				padWord();
				eof = true;
				return;
			} else if (zb && zc == c) {
				padWord();
				rbyte = 0x00;
				rcount = 4;
				return;
			} else if (yb && yc == c) {
				padWord();
				rbyte = 0x20;
				rcount = 4;
				return;
			} else if (xb && xc == c) {
				padWord();
				rbyte = 0xFF;
				rcount = 4;
				return;
			} else if ((c = alphabet.indexOf(c)) >= 0) {
				word *= 85;
				word += c;
				count++;
				if (count > 4) {
					count = 4;
					return;
				}
			}
		}
	}
	
	private void padWord() {
		if (count > 0) {
			for (int i = count; i <= 4; i++) {
				word *= 85;
				word += 84;
			}
			count--;
		}
	}
	
	@Override
	public void close() throws IOException {
		if (in != null) in.close();
	}
}
