package com.kreative.hexcellent.test;

import com.kreative.hexcellent.buffer.FloatFormat;

public class NaNTest {
	public static void main(String[] args) {
		System.out.println(Float.POSITIVE_INFINITY);
		System.out.println(Double.POSITIVE_INFINITY);
		System.out.println(FloatFormat.NaN.POSITIVE_INFINITY);
		System.out.println(new FloatFormat.NaN(Float.POSITIVE_INFINITY));
		System.out.println(new FloatFormat.NaN(Double.POSITIVE_INFINITY));
		System.out.println(new FloatFormat.NaN(Float.POSITIVE_INFINITY).equals(FloatFormat.NaN.POSITIVE_INFINITY));
		System.out.println(new FloatFormat.NaN(Double.POSITIVE_INFINITY).equals(FloatFormat.NaN.POSITIVE_INFINITY));
		System.out.println(FloatFormat.NaN.POSITIVE_INFINITY.floatValue() == Float.POSITIVE_INFINITY);
		System.out.println(FloatFormat.NaN.POSITIVE_INFINITY.doubleValue() == Double.POSITIVE_INFINITY);
		
		System.out.println(Float.NEGATIVE_INFINITY);
		System.out.println(Double.NEGATIVE_INFINITY);
		System.out.println(FloatFormat.NaN.NEGATIVE_INFINITY);
		System.out.println(new FloatFormat.NaN(Float.NEGATIVE_INFINITY));
		System.out.println(new FloatFormat.NaN(Double.NEGATIVE_INFINITY));
		System.out.println(new FloatFormat.NaN(Float.NEGATIVE_INFINITY).equals(FloatFormat.NaN.NEGATIVE_INFINITY));
		System.out.println(new FloatFormat.NaN(Double.NEGATIVE_INFINITY).equals(FloatFormat.NaN.NEGATIVE_INFINITY));
		System.out.println(FloatFormat.NaN.NEGATIVE_INFINITY.floatValue() == Float.NEGATIVE_INFINITY);
		System.out.println(FloatFormat.NaN.NEGATIVE_INFINITY.doubleValue() == Double.NEGATIVE_INFINITY);
		
		System.out.println(Float.NaN);
		System.out.println(Double.NaN);
		System.out.println(FloatFormat.NaN.NaN);
		System.out.println(new FloatFormat.NaN(Float.NaN));
		System.out.println(new FloatFormat.NaN(Double.NaN));
		System.out.println(new FloatFormat.NaN(Float.NaN).equals(FloatFormat.NaN.NaN));
		System.out.println(new FloatFormat.NaN(Double.NaN).equals(FloatFormat.NaN.NaN));
		System.out.println(Float.isNaN(FloatFormat.NaN.NaN.floatValue()));
		System.out.println(Double.isNaN(FloatFormat.NaN.NaN.doubleValue()));
		
		System.out.println(FloatFormat.Zero.POSITIVE_ZERO);
		System.out.println(FloatFormat.Zero.POSITIVE_ZERO.floatValue());
		System.out.println(FloatFormat.Zero.POSITIVE_ZERO.doubleValue());
		System.out.println(FloatFormat.Zero.NEGATIVE_ZERO);
		System.out.println(FloatFormat.Zero.NEGATIVE_ZERO.floatValue());
		System.out.println(FloatFormat.Zero.NEGATIVE_ZERO.doubleValue());
	}
}
