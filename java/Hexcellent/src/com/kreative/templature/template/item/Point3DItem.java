package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class Point3DItem implements TemplateItem {
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int length;
	private final int xshift, yshift, zshift;
	private final int xwidth, ywidth, zwidth;
	private final BigInteger xsign, ysign, zsign;
	
	public Point3DItem(
		int typeConstant, String typeString, String name, boolean le, int width,
		int xShift, int xWidth, int yShift, int yWidth, int zShift, int zWidth
	) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.length = (width + 7) / 8;
		this.xshift = xShift;
		this.yshift = yShift;
		this.zshift = zShift;
		this.xwidth = xWidth;
		this.ywidth = yWidth;
		this.zwidth = zWidth;
		this.xsign = BigInteger.valueOf(-1).shiftLeft(xWidth);
		this.ysign = BigInteger.valueOf(-1).shiftLeft(yWidth);
		this.zsign = BigInteger.valueOf(-1).shiftLeft(zWidth);
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		BigInteger v = in.readBigInteger(length, false, le);
		BigInteger x = v.shiftRight(xshift).andNot(xsign);
		BigInteger y = v.shiftRight(yshift).andNot(ysign);
		BigInteger z = v.shiftRight(zshift).andNot(zsign);
		if (x.testBit(xwidth - 1)) x = x.or(xsign);
		if (y.testBit(ywidth - 1)) y = y.or(ysign);
		if (z.testBit(zwidth - 1)) z = z.or(zsign);
		return new Instance(closure, x.intValue(), y.intValue(), z.intValue());
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, Point3DDataModel {
		private final Closure closure;
		private int x, y, z;
		private Instance(Closure closure, int x, int y, int z) {
			this.closure = closure;
			this.x = x; this.y = y; this.z = z;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public int[] getValue() {
			return new int[]{ x, y, z };
		}
		public int[] getPoint3DValues() {
			return new int[]{ x, y, z };
		}
		public int[] getPoint3DMinValues() {
			return new int[]{
				xsign.shiftRight(1).intValue(),
				ysign.shiftRight(1).intValue(),
				zsign.shiftRight(1).intValue()
			};
		}
		public int[] getPoint3DMaxValues() {
			return new int[]{
				xsign.not().shiftRight(1).intValue(),
				ysign.not().shiftRight(1).intValue(),
				zsign.not().shiftRight(1).intValue()
			};
		}
		public void setPoint3DValues(int x, int y, int z) {
			if (this.x == x && this.y == y && this.z == z) return;
			this.x = x; this.y = y; this.z = z;
			closure.fireValueChanged(this);
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createPoint3DComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			BigInteger X = BigInteger.valueOf(x).andNot(xsign).shiftLeft(xshift);
			BigInteger Y = BigInteger.valueOf(y).andNot(ysign).shiftLeft(yshift);
			BigInteger Z = BigInteger.valueOf(z).andNot(zsign).shiftLeft(zshift);
			out.writeBigInteger(X.or(Y).or(Z), length, le);
		}
	}
}
