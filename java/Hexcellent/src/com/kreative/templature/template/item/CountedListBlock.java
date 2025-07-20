package com.kreative.templature.template.item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.kreative.templature.template.*;

public final class CountedListBlock implements TemplateItem {
	private final int countTypeConstant;
	private final int listTypeConstant;
	private final int listEndConstant;
	private final String countTypeString;
	private final String listTypeString;
	private final String countName;
	private final String listBeginName;
	private final String listEndName;
	private final boolean countLE;
	private final int countLength;
	private final BigInteger countBase;
	private final BigInteger countSign;
	private final Template listContent;
	
	public CountedListBlock(
		int countTypeConstant, int listTypeConstant, int listEndConstant,
		String countTypeString, String listTypeString,
		String countName, String listBeginName, String listEndName,
		boolean countLE, int countWidth, long countBase,
		List<? extends TemplateItem> listContent
	) {
		this.countTypeConstant = countTypeConstant;
		this.listTypeConstant = listTypeConstant;
		this.listEndConstant = listEndConstant;
		this.countTypeString = countTypeString;
		this.listTypeString = listTypeString;
		this.countName = countName;
		this.listBeginName = listBeginName;
		this.listEndName = listEndName;
		this.countLE = countLE;
		this.countLength = (countWidth + 7) / 8;
		this.countBase = BigInteger.valueOf(countBase);
		this.countSign = BigInteger.valueOf(-1).shiftLeft(countLength * 8);
		this.listContent = new Template();
		this.listContent.addAll(listContent);
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		BigInteger rawCount = in.readBigInteger(countLength, false, countLE);
		long count = rawCount.subtract(countBase).andNot(countSign).longValue();
		ArrayList<Template.Instance> entries = new ArrayList<Template.Instance>();
		while (count > 0) {
			in.mark(1); if (in.read() < 0) break; in.reset();
			entries.add(listContent.read(new Closure(closure), in));
			count--;
		}
		return new Instance(closure, entries);
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(countTypeConstant, countName);
		out.write(listTypeConstant, listBeginName);
		listContent.write(out);
		out.write(listEndConstant, listEndName);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + countTypeString + " " + TemplateUtils.escapeName(countName) + ";");
		out.println(prefix + listTypeString + " " + TemplateUtils.escapeName(listBeginName) + " {");
		listContent.print(out, prefix + "\t");
		out.println(prefix + "} " + TemplateUtils.escapeName(listEndName) + ";");
	}
	
	public final class Instance implements TemplateItemInstance, ListDataModel, ClosureItem {
		private final Closure closure;
		private final ArrayList<Template.Instance> entries;
		private Instance(Closure closure, ArrayList<Template.Instance> entries) {
			this.closure = closure;
			this.entries = entries;
			closure.addItem(this);
		}
		public String getKey() {
			return listBeginName;
		}
		public List<Template.Instance> getValue() {
			return Collections.unmodifiableList(entries);
		}
		public String getCountLabelText() {
			return countName;
		}
		public String getCountValueText() {
			return BigInteger.valueOf(entries.size()).add(countBase).toString();
		}
		public int getEntryCount() {
			return entries.size();
		}
		public String getEntryLabelText(int index) {
			return (index + 1) + ") " + ((index <= 0) ? listBeginName : listEndName);
		}
		public Template.Instance getEntry(int index) {
			return entries.get(index);
		}
		public Template.Instance createNewEntry() throws IOException {
			ByteArrayInputStream bin = new ByteArrayInputStream(new byte[0]);
			BufferedCountedInputStream in = new BufferedCountedInputStream(bin);
			return listContent.read(new Closure(closure), in);
		}
		public void addEntry(int index, Template.Instance instance) {
			entries.add(index, instance);
			closure.fireValueChanged(this);
		}
		public Template.Instance removeEntry(int index) {
			Template.Instance entry = entries.remove(index);
			closure.fireValueChanged(this);
			return entry;
		}
		public String getLabelText() {
			return null;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createListComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			BigInteger count = BigInteger.valueOf(entries.size()).add(countBase);
			out.writeBigInteger(count, countLength, countLE);
			for (Template.Instance entry : entries) entry.write(out);
		}
	}
}
