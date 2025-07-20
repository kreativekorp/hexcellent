package com.kreative.templature.template;

import java.math.BigInteger;

public class BigIntegerBitInputStream {
	private final BigInteger bits;
	private final int bitWidth;
	private int readPointer;
	private int markPointer;
	
	public BigIntegerBitInputStream(BigInteger value, int width) {
		this.bits = value;
		this.bitWidth = width;
		this.readPointer = 0;
		this.markPointer = 0;
	}
	
	public BigInteger align(int mod, boolean signed) {
		int r = readPointer % mod;
		if (r == 0) return null;
		return read(mod - r, signed);
	}
	
	public int available() {
		return bitWidth - readPointer;
	}
	
	public void mark() {
		markPointer = readPointer;
	}
	
	public int read() {
		readPointer++;
		int n = bitWidth - readPointer;
		return (n >= 0) ? (bits.testBit(n) ? 1 : 0) : -1;
	}
	
	public BigInteger read(int width, boolean signed) {
		readPointer += width;
		BigInteger sign = BigInteger.valueOf(-1).shiftLeft(width);
		BigInteger value = bits.shiftRight(bitWidth - readPointer).andNot(sign);
		if (signed && value.testBit(width - 1)) value = value.or(sign);
		return value;
	}
	
	public void reset() {
		readPointer = markPointer;
	}
	
	public void skip(int count) {
		readPointer += count;
	}
}
