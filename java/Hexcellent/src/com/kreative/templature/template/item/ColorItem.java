package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class ColorItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int length;
	private final int rshift, gshift, bshift, ashift;
	private final BigInteger rsign, gsign, bsign, asign;
	
	public ColorItem(
		int typeConstant, String typeString, String name, boolean le, int width,
		int rShift, int rWidth, int gShift, int gWidth,
		int bShift, int bWidth, int aShift, int aWidth
	) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.length = (width + 7) / 8;
		this.rshift = rShift;
		this.gshift = gShift;
		this.bshift = bShift;
		this.ashift = aShift;
		this.rsign = BigInteger.valueOf(-1).shiftLeft(rWidth);
		this.gsign = BigInteger.valueOf(-1).shiftLeft(gWidth);
		this.bsign = BigInteger.valueOf(-1).shiftLeft(bWidth);
		this.asign = BigInteger.valueOf(-1).shiftLeft(aWidth);
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		BigInteger v = in.readBigInteger(length, false, le);
		BigInteger r = v.shiftRight(rshift).andNot(rsign);
		BigInteger g = v.shiftRight(gshift).andNot(gsign);
		BigInteger b = v.shiftRight(bshift).andNot(bsign);
		BigInteger a = v.shiftRight(ashift).andNot(asign);
		return new Instance(closure, r.intValue(), g.intValue(), b.intValue(), a.intValue());
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, ColorDataModel {
		private final Closure closure;
		private int r, g, b, a;
		private Instance(Closure closure, int r, int g, int b, int a) {
			this.closure = closure;
			this.r = r; this.g = g; this.b = b; this.a = a;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public int[] getValue() {
			return new int[]{ r, g, b, a };
		}
		public int[] getColorValues() {
			return new int[]{ r, g, b, a };
		}
		public int[] getColorMaxValues() {
			return new int[]{
				rsign.not().intValue(),
				gsign.not().intValue(),
				bsign.not().intValue(),
				asign.not().intValue()
			};
		}
		public void setColorValues(int r, int g, int b, int a) {
			if (this.r == r && this.g == g && this.b == b && this.a == a) return;
			this.r = r; this.g = g; this.b = b; this.a = a;
			closure.fireValueChanged(this);
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createColorComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			BigInteger R = BigInteger.valueOf(r).andNot(rsign).shiftLeft(rshift);
			BigInteger G = BigInteger.valueOf(g).andNot(gsign).shiftLeft(gshift);
			BigInteger B = BigInteger.valueOf(b).andNot(bsign).shiftLeft(bshift);
			BigInteger A = BigInteger.valueOf(a).andNot(asign).shiftLeft(ashift);
			out.writeBigInteger(R.or(G).or(B).or(A), length, le);
		}
	}
}
