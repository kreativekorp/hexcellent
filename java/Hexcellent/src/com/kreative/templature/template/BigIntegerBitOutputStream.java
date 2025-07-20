package com.kreative.templature.template;

import java.math.BigInteger;

public class BigIntegerBitOutputStream {
	private BigInteger bits = BigInteger.ZERO;
	private int bitWidth = 0;
	
	public void align(BigInteger fill, int mod) {
		int r = bitWidth % mod;
		if (r == 0) return;
		write(fill, mod - r);
	}
	
	public BigInteger getValue() {
		return bits;
	}
	
	public int getWidth() {
		return bitWidth;
	}
	
	public void write(boolean bit) {
		bits = bits.shiftLeft(1);
		if (bit) bits = bits.setBit(0);
		bitWidth++;
	}
	
	public void write(BigInteger value, int width) {
		BigInteger sign = BigInteger.valueOf(-1).shiftLeft(width);
		bits = bits.shiftLeft(width).or(value.andNot(sign));
		bitWidth += width;
	}
}
