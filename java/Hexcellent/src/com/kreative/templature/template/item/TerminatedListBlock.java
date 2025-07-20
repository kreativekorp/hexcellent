package com.kreative.templature.template.item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.kreative.templature.template.*;

public final class TerminatedListBlock implements TemplateItem {
	private final int typeConstant;
	private final int endConstant;
	private final String typeString;
	private final String beginName;
	private final String endName;
	private final int terminator;
	private final Template listContent;
	
	public TerminatedListBlock(
		int typeConstant, int endConstant, String typeString,
		String beginName, String endName, int terminator,
		List<? extends TemplateItem> listContent
	) {
		this.typeConstant = typeConstant;
		this.endConstant = endConstant;
		this.typeString = typeString;
		this.beginName = beginName;
		this.endName = endName;
		this.terminator = (terminator & 0xFF);
		this.listContent = new Template();
		this.listContent.addAll(listContent);
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		ArrayList<Template.Instance> entries = new ArrayList<Template.Instance>();
		for (;;) {
			in.mark(1); int b = in.read(); if (b < 0 || b == terminator) break; in.reset();
			entries.add(listContent.read(new Closure(closure), in));
		}
		return new Instance(closure, entries);
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(typeConstant, beginName);
		listContent.write(out);
		out.write(endConstant, endName);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + typeString + " " + TemplateUtils.escapeName(beginName) + " {");
		listContent.print(out, prefix + "\t");
		out.println(prefix + "} " + TemplateUtils.escapeName(endName) + ";");
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
			return beginName;
		}
		public List<Template.Instance> getValue() {
			return Collections.unmodifiableList(entries);
		}
		public String getCountLabelText() {
			return null;
		}
		public String getCountValueText() {
			return null;
		}
		public int getEntryCount() {
			return entries.size();
		}
		public String getEntryLabelText(int index) {
			return (index + 1) + ") " + ((index <= 0) ? beginName : endName);
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
			for (Template.Instance entry : entries) entry.write(out);
			out.write(terminator);
		}
	}
}
