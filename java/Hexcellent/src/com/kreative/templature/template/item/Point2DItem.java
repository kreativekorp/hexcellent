package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class Point2DItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int length;
	private final int xshift, yshift;
	private final int xwidth, ywidth;
	private final BigInteger xsign, ysign;
	
	public Point2DItem(
		int typeConstant, String typeString, String name, boolean le, int width,
		int xShift, int xWidth, int yShift, int yWidth
	) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.length = (width + 7) / 8;
		this.xshift = xShift;
		this.yshift = yShift;
		this.xwidth = xWidth;
		this.ywidth = yWidth;
		this.xsign = BigInteger.valueOf(-1).shiftLeft(xWidth);
		this.ysign = BigInteger.valueOf(-1).shiftLeft(yWidth);
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		BigInteger v = in.readBigInteger(length, false, le);
		BigInteger x = v.shiftRight(xshift).andNot(xsign);
		BigInteger y = v.shiftRight(yshift).andNot(ysign);
		if (x.testBit(xwidth - 1)) x = x.or(xsign);
		if (y.testBit(ywidth - 1)) y = y.or(ysign);
		return new Instance(closure, x.intValue(), y.intValue());
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, Point2DDataModel {
		private final Closure closure;
		private int x, y;
		private Instance(Closure closure, int x, int y) {
			this.closure = closure;
			this.x = x; this.y = y;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public int[] getValue() {
			return new int[]{ x, y };
		}
		public int[] getPoint2DValues() {
			return new int[]{ x, y };
		}
		public int[] getPoint2DMinValues() {
			return new int[]{
				xsign.shiftRight(1).intValue(),
				ysign.shiftRight(1).intValue()
			};
		}
		public int[] getPoint2DMaxValues() {
			return new int[]{
				xsign.not().shiftRight(1).intValue(),
				ysign.not().shiftRight(1).intValue()
			};
		}
		public void setPoint2DValues(int x, int y) {
			if (this.x == x && this.y == y) return;
			this.x = x; this.y = y;
			closure.fireValueChanged(this);
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createPoint2DComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			BigInteger X = BigInteger.valueOf(x).andNot(xsign).shiftLeft(xshift);
			BigInteger Y = BigInteger.valueOf(y).andNot(ysign).shiftLeft(yshift);
			out.writeBigInteger(X.or(Y), length, le);
		}
	}
}
