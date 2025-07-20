package com.kreative.templature.template;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;

public final class PackedBlock extends ArrayList<PackedItem> implements TemplateItem {
	private static final long serialVersionUID = 1L;
	
	private final int typeConstant;
	private final int endConstant;
	private final String typeString;
	private final String beginName;
	private final String endName;
	private final boolean le;
	
	public PackedBlock(int typeConstant, int endConstant, String typeString, String beginName, String endName, boolean le) {
		this.typeConstant = typeConstant;
		this.endConstant = endConstant;
		this.typeString = typeString;
		this.beginName = beginName;
		this.endName = endName;
		this.le = le;
	}
	
	public int getBitWidth() {
		BigIntegerBitOutputStream bout = new BigIntegerBitOutputStream();
		for (PackedItem item : this) item.measure(bout);
		return bout.getWidth();
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		int width = getBitWidth();
		int length = (width + 7) / 8;
		BigInteger bits = in.readBigInteger(length, false, le);
		BigIntegerBitInputStream bin = new BigIntegerBitInputStream(bits, width);
		Instance inst = new Instance(this.size());
		for (PackedItem item : this) inst.add(item.read(closure, bin));
		return inst;
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		if (typeConstant == 0 && endConstant == 0) {
			for (PackedItem item : this) item.write(out);
		} else {
			out.write(typeConstant, beginName);
			for (PackedItem item : this) item.write(out);
			out.write(endConstant, endName);
		}
	}
	
	public void print(PrintWriter out, String prefix) {
		if (typeString == null || typeString.length() == 0) {
			for (PackedItem item : this) item.print(out, prefix);
		} else {
			out.println(prefix + typeString + " " + TemplateUtils.escapeName(beginName) + " {");
			String i = prefix + "\t"; for (PackedItem item : this) item.print(out, i);
			out.println(prefix + "} " + TemplateUtils.escapeName(endName) + ";");
		}
	}
	
	public final class Instance extends ArrayList<PackedItemInstance> implements TemplateItemInstance {
		private static final long serialVersionUID = 1L;
		private Instance(int size) {
			super(size);
		}
		public String getLabelText() {
			return null;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			ArrayList<String> labels = new ArrayList<String>();
			ArrayList<C> components = new ArrayList<C>();
			for (PackedItemInstance inst : Instance.this) {
				labels.add(inst.getLabelText());
				components.add(inst.createComponent(factory));
			}
			return factory.createFormComponent(labels, components, size());
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			BigIntegerBitOutputStream bout = new BigIntegerBitOutputStream();
			for (PackedItemInstance inst : Instance.this) inst.write(bout);
			BigInteger bits = bout.getValue();
			int length = (bout.getWidth() + 7) / 8;
			out.writeBigInteger(bits, length, le);
		}
	}
}
