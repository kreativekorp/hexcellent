package com.kreative.templature.template.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import com.kreative.templature.template.*;

public final class PascalStringItem implements TemplateItem {
	public static final int LENGTH_MAX = Integer.MAX_VALUE;
	public static final int LENGTH_VARIABLE = -1;
	public static final int LENGTH_EVEN = -2;
	public static final int LENGTH_ODD = -3;
	
	private final int typeConstant;
	private final String typeString;
	private final String name;
	private final boolean le;
	private final int countLength;
	private final int stringLength;
	private final String encoding;
	private final String lineEnding;
	
	public PascalStringItem(int typeConstant, String typeString, String name, boolean le, int countWidth, int stringLength, String encoding, String lineEnding) {
		this.typeConstant = typeConstant;
		this.typeString = typeString;
		this.name = name;
		this.le = le;
		this.countLength = (countWidth + 7) / 8;
		this.stringLength = stringLength;
		this.encoding = encoding;
		this.lineEnding = lineEnding;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		long count = in.readBigInteger(countLength, false, le).longValue();
		String s;
		if (stringLength >= 0) {
			// Fixed size string
			byte[] data = new byte[stringLength];
			int read = in.read(data); if (read < 0) read = 0;
			s = new String(data, 0, ((read <= count) ? read : (int)count), encoding);
		} else {
			// Variable size string
			byte[] data = new byte[(count < LENGTH_MAX) ? (int)count : LENGTH_MAX];
			int read = in.read(data); if (read < 0) read = 0;
			if (read < count) in.safeSkip(count - read);
			s = new String(data, 0, ((read <= count) ? read : (int)count), encoding);
			if (stringLength == LENGTH_ODD && ((countLength + count) & 1) == 0) in.read();
			if (stringLength == LENGTH_EVEN && ((countLength + count) & 1) != 0) in.read();
		}
		if (lineEnding != null && lineEnding.length() > 0) s = s.replace(lineEnding, "\n");
		return new Instance(closure, s);
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, name);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(name) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ClosureItem, StringDataModel {
		private final Closure closure;
		private String value;
		private Instance(Closure closure, String value) {
			this.closure = closure;
			this.value = value;
			closure.addItem(this);
		}
		public String getKey() {
			return name;
		}
		public String getValue() {
			return value;
		}
		public String getStringValue() {
			return value;
		}
		public void setStringValue(String value) {
			if (this.value.equals(value)) return;
			this.value = value;
			closure.fireValueChanged(this);
		}
		public String getLabelText() {
			return name;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createTextAreaComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			String s = value;
			if (lineEnding != null && lineEnding.length() > 0) s = s.replace("\n", lineEnding);
			byte[] b = s.getBytes(encoding);
			if (stringLength >= 0) {
				// Fixed size string
				int count = (b.length <= stringLength) ? b.length : stringLength;
				out.writeBigInteger(BigInteger.valueOf(count), countLength, le);
				out.write(b, 0, count);
				out.write(new byte[stringLength - count]);
			} else {
				// Variable size string
				long lengthMax = (1L << (countLength * 8)) - 1L;
				int count = (b.length <= lengthMax) ? b.length : (int)lengthMax;
				out.writeBigInteger(BigInteger.valueOf(count), countLength, le);
				out.write(b, 0, count);
				if (stringLength == LENGTH_ODD && ((countLength + count) & 1) == 0) out.write(0);
				if (stringLength == LENGTH_EVEN && ((countLength + count) & 1) != 0) out.write(0);
			}
		}
	}
}
