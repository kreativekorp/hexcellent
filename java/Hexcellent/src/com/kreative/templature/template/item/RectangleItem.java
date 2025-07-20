package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class RectangleItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int length;
	private final int lshift, tshift, rshift, bshift;
	private final int lwidth, twidth, rwidth, bwidth;
	private final BigInteger lsign, tsign, rsign, bsign;
	
	public RectangleItem(
		int typeConstant, String typeString, String name, boolean le, int width,
		int leftShift, int leftWidth, int topShift, int topWidth,
		int rightShift, int rightWidth, int bottomShift, int bottomWidth
	) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.length = (width + 7) / 8;
		this.lshift = leftShift;
		this.tshift = topShift;
		this.rshift = rightShift;
		this.bshift = bottomShift;
		this.lwidth = leftWidth;
		this.twidth = topWidth;
		this.rwidth = rightWidth;
		this.bwidth = bottomWidth;
		this.lsign = BigInteger.valueOf(-1).shiftLeft(leftWidth);
		this.tsign = BigInteger.valueOf(-1).shiftLeft(topWidth);
		this.rsign = BigInteger.valueOf(-1).shiftLeft(rightWidth);
		this.bsign = BigInteger.valueOf(-1).shiftLeft(bottomWidth);
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		BigInteger v = in.readBigInteger(length, false, le);
		BigInteger l = v.shiftRight(lshift).andNot(lsign);
		BigInteger t = v.shiftRight(tshift).andNot(tsign);
		BigInteger r = v.shiftRight(rshift).andNot(rsign);
		BigInteger b = v.shiftRight(bshift).andNot(bsign);
		if (l.testBit(lwidth - 1)) l = l.or(lsign);
		if (t.testBit(twidth - 1)) t = t.or(tsign);
		if (r.testBit(rwidth - 1)) r = r.or(rsign);
		if (b.testBit(bwidth - 1)) b = b.or(bsign);
		return new Instance(closure, l.intValue(), t.intValue(), r.intValue(), b.intValue());
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, RectangleDataModel {
		private final Closure closure;
		private int l, t, r, b;
		private Instance(Closure closure, int l, int t, int r, int b) {
			this.closure = closure;
			this.l = l; this.t = t;
			this.r = r; this.b = b;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public int[] getValue() {
			return new int[]{ l, t, r, b };
		}
		public int[] getRectangleValues() {
			return new int[]{ l, t, r, b };
		}
		public int[] getRectangleMinValues() {
			return new int[]{
				lsign.shiftRight(1).intValue(),
				tsign.shiftRight(1).intValue(),
				rsign.shiftRight(1).intValue(),
				bsign.shiftRight(1).intValue()
			};
		}
		public int[] getRectangleMaxValues() {
			return new int[]{
				lsign.not().shiftRight(1).intValue(),
				tsign.not().shiftRight(1).intValue(),
				rsign.not().shiftRight(1).intValue(),
				bsign.not().shiftRight(1).intValue()
			};
		}
		public void setRectangleValues(int l, int t, int r, int b) {
			if (this.l == l && this.t == t && this.r == r && this.b == b) return;
			this.l = l; this.t = t; this.r = r; this.b = b;
			closure.fireValueChanged(this);
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createRectangleComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			BigInteger L = BigInteger.valueOf(l).andNot(lsign).shiftLeft(lshift);
			BigInteger T = BigInteger.valueOf(t).andNot(tsign).shiftLeft(tshift);
			BigInteger R = BigInteger.valueOf(r).andNot(rsign).shiftLeft(rshift);
			BigInteger B = BigInteger.valueOf(b).andNot(bsign).shiftLeft(bshift);
			out.writeBigInteger(L.or(T).or(R).or(B), length, le);
		}
	}
}
