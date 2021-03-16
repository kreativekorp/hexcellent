package com.kreative.hexcellent.test;

import java.math.BigInteger;
import java.util.Random;

public class BigIntegerTest {
	public static void main(String[] args) {
		Random random = new Random();
		int n1 = 2, n2 = 3;
		while (n1 > 0 && n2 > 0) {
			test(random, n1);
			test(random, n2);
			n1 <<= 1;
			n2 <<= 1;
		}
	}
	
	public static void test(Random random, int length) {
		System.out.print(length + ": ");
		try {
			byte[] data = new byte[length];
			long time = 0;
			for (int i = 0; i < 1000; i++) {
				random.nextBytes(data);
				long t = -System.currentTimeMillis();
				new BigInteger(data).toString();
				t += System.currentTimeMillis();
				time += t;
			}
			System.out.println(time + "us");
		} catch (Throwable t) {
			System.out.println("ERROR: " + t);
		}
	}
}
