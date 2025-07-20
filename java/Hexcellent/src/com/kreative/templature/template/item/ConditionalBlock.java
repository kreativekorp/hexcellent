package com.kreative.templature.template.item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.kreative.templature.template.*;

public final class ConditionalBlock implements TemplateItem {
	private final int[] blockTypeConstant;
	private final int endConstant;
	private final String[] blockTypeString;
	private final String[] binBlockExprString;
	private final String[] textBlockExprString;
	private final String endName;
	private final Expression[] blockExpression;
	private final Template[] blockContent;
	private final int blockCount;
	
	public ConditionalBlock(
		List<Integer> blockTypeConstant, int endConstant, List<String> blockTypeString,
		List<String> binBlockExprString, List<String> textBlockExprString, String endName,
		List<? extends Expression> blockExpression,
		List<? extends List<? extends TemplateItem>> blockContent, int blockCount
	) {
		this.blockTypeConstant = new int[blockCount];
		this.endConstant = endConstant;
		this.blockTypeString = new String[blockCount];
		this.binBlockExprString = new String[blockCount];
		this.textBlockExprString = new String[blockCount];
		this.endName = endName;
		this.blockExpression = new Expression[blockCount];
		this.blockContent = new Template[blockCount];
		this.blockCount = blockCount;
		for (int i = 0; i < blockCount; i++) {
			this.blockTypeConstant[i] = blockTypeConstant.get(i);
			this.blockTypeString[i] = blockTypeString.get(i);
			this.binBlockExprString[i] = binBlockExprString.get(i);
			this.textBlockExprString[i] = textBlockExprString.get(i);
			this.blockExpression[i] = blockExpression.get(i);
			this.blockContent[i] = new Template();
			this.blockContent[i].addAll(blockContent.get(i));
		}
	}
	
	private int getBlockIndex(Closure closure) {
		for (int i = 0; i < blockCount; i++) {
			if (blockExpression[i] != null) {
				Object cv = blockExpression[i].evaluate(closure);
				if (Expression.toBoolean(cv)) return i;
			}
		}
		for (int i = 0; i < blockCount; i++) {
			if (blockExpression[i] == null) return i;
		}
		return -1;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		final int k = getBlockIndex(closure);
		ArrayList<Template.Instance> entries = new ArrayList<Template.Instance>();
		for (int i = 0; i < blockCount; i++) {
			final BufferedCountedInputStream cin;
			if (i == k) cin = in;
			else cin = new BufferedCountedInputStream(new ByteArrayInputStream(new byte[0]));
			entries.add(blockContent[i].read(new Closure(closure), cin));
		}
		return new Instance(closure, entries, k);
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		for (int i = 0; i < blockCount; i++) {
			out.write(blockTypeConstant[i], binBlockExprString[i]);
			blockContent[i].write(out);
		}
		out.write(endConstant, endName);
	}
	
	public void print(PrintWriter out, String prefix) {
		for (int i = 0; i < blockCount; i++) {
			out.println(((i == 0) ? prefix : (prefix + "} ")) + blockTypeString[i] + (
				(blockExpression[i] != null) ? (" (" + textBlockExprString[i] + ") {") :
				(textBlockExprString[i] == null || textBlockExprString[i].length() == 0) ? " {" :
				(" " + TemplateUtils.escapeName(textBlockExprString[i]) + " {")
			));
			blockContent[i].print(out, prefix + "\t");
		}
		out.println(prefix + (
			(endName == null || endName.length() == 0) ? "};" :
			("} " + TemplateUtils.escapeName(endName) + ";")
		));
	}
	
	public final class Instance implements TemplateItemInstance, SwitchDataModel, ClosureItem, ClosureListener {
		private final Closure closure;
		private final ArrayList<Template.Instance> entries;
		private final ArrayList<SwitchListener> listeners;
		private int currentIndex;
		private Instance(Closure closure, ArrayList<Template.Instance> entries, int currentIndex) {
			this.closure = closure;
			this.entries = entries;
			this.listeners = new ArrayList<SwitchListener>();
			this.currentIndex = currentIndex;
			closure.addItem(this);
			closure.addClosureListener(this);
		}
		public String getKey() {
			return endName;
		}
		public Template.Instance getValue() {
			return (currentIndex >= 0 && currentIndex < blockCount) ? entries.get(currentIndex) : null;
		}
		public void valueChanged(ClosureItem item) {
			if (item == this) return;
			int newIndex = ConditionalBlock.this.getBlockIndex(closure);
			if (newIndex == currentIndex) return;
			currentIndex = newIndex;
			for (SwitchListener l : listeners) l.switchCase(this, newIndex);
			closure.fireValueChanged(this);
		}
		public int getCaseCount() {
			return blockCount;
		}
		public int getCaseIndex() {
			return currentIndex;
		}
		public Template.Instance getCase(int index) {
			return (index >= 0 && index < blockCount) ? entries.get(index) : null;
		}
		public void addSwitchListener(SwitchListener listener) {
			listeners.add(listener);
		}
		public void removeSwitchListener(SwitchListener listener) {
			listeners.remove(listener);
		}
		public String getLabelText() {
			return null;
		}
		public <C> C createComponent(ComponentFactory<C> factory) {
			return factory.createSwitchComponent(this);
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			if (currentIndex >= 0 && currentIndex < blockCount) {
				Template.Instance entry = entries.get(currentIndex);
				if (entry != null) entry.write(out);
			}
		}
	}
}
