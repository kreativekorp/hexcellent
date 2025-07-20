package com.kreative.templature.template.item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import com.kreative.templature.template.*;

public final class SwitchBlock implements TemplateItem {
	private final int switchTypeConstant;
	private final int[] caseTypeConstant;
	private final int endConstant;
	private final String switchTypeString;
	private final String[] caseTypeString;
	private final String binSwitchExprString;
	private final String textSwitchExprString;
	private final String[] binCaseExprString;
	private final String[] textCaseExprString;
	private final String endName;
	private final Expression switchExpression;
	private final Expression[] caseExpression;
	private final Template[] caseContent;
	private final int caseCount;
	
	public SwitchBlock(
		int switchTypeConstant, List<Integer> caseTypeConstant, int endConstant,
		String switchTypeString, List<String> caseTypeString,
		String binSwitchExprString, String textSwitchExprString,
		List<String> binCaseExprString, List<String> textCaseExprString, String endName,
		Expression switchExpression, List<? extends Expression> caseExpression,
		List<? extends List<? extends TemplateItem>> caseContent, int caseCount
	) {
		this.switchTypeConstant = switchTypeConstant;
		this.caseTypeConstant = new int[caseCount];
		this.endConstant = endConstant;
		this.switchTypeString = switchTypeString;
		this.caseTypeString = new String[caseCount];
		this.binSwitchExprString = binSwitchExprString;
		this.textSwitchExprString = textSwitchExprString;
		this.binCaseExprString = new String[caseCount];
		this.textCaseExprString = new String[caseCount];
		this.endName = endName;
		this.switchExpression = switchExpression;
		this.caseExpression = new Expression[caseCount];
		this.caseContent = new Template[caseCount];
		this.caseCount = caseCount;
		for (int i = 0; i < caseCount; i++) {
			this.caseTypeConstant[i] = caseTypeConstant.get(i);
			this.caseTypeString[i] = caseTypeString.get(i);
			this.binCaseExprString[i] = binCaseExprString.get(i);
			this.textCaseExprString[i] = textCaseExprString.get(i);
			this.caseExpression[i] = caseExpression.get(i);
			this.caseContent[i] = new Template();
			this.caseContent[i].addAll(caseContent.get(i));
		}
	}
	
	private int getCaseIndex(Closure closure) {
		Object sv = switchExpression.evaluate(closure);
		for (int i = 0; i < caseCount; i++) {
			if (caseExpression[i] != null) {
				Object cv = caseExpression[i].evaluate(closure);
				if (Expression.equal(sv, cv)) return i;
			}
		}
		for (int i = 0; i < caseCount; i++) {
			if (caseExpression[i] == null) return i;
		}
		return -1;
	}
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		final int k = getCaseIndex(closure);
		ArrayList<Template.Instance> entries = new ArrayList<Template.Instance>();
		for (int i = 0; i < caseCount; i++) {
			final BufferedCountedInputStream cin;
			if (i == k) cin = in;
			else cin = new BufferedCountedInputStream(new ByteArrayInputStream(new byte[0]));
			entries.add(caseContent[i].read(new Closure(closure), cin));
		}
		return new Instance(closure, entries, k);
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		out.write(switchTypeConstant, binSwitchExprString);
		for (int i = 0; i < caseCount; i++) {
			out.write(caseTypeConstant[i], binCaseExprString[i]);
			caseContent[i].write(out);
		}
		out.write(endConstant, endName);
	}
	
	public void print(PrintWriter out, String prefix) {
		out.println(prefix + switchTypeString + " (" + textSwitchExprString + ") {");
		for (int i = 0; i < caseCount; i++) {
			out.println(caseTypeString[i] + (
				(caseExpression[i] != null) ? (" " + textCaseExprString[i] + ":") :
				(textCaseExprString[i] == null || textCaseExprString[i].length() == 0) ? ":" :
				(" " + TemplateUtils.escapeName(textCaseExprString[i]) + ":")
			));
			caseContent[i].print(out, prefix + "\t\t");
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
			return (currentIndex >= 0 && currentIndex < caseCount) ? entries.get(currentIndex) : null;
		}
		public void valueChanged(ClosureItem item) {
			if (item == this) return;
			int newIndex = SwitchBlock.this.getCaseIndex(closure);
			if (newIndex == currentIndex) return;
			currentIndex = newIndex;
			for (SwitchListener l : listeners) l.switchCase(this, newIndex);
			closure.fireValueChanged(this);
		}
		public int getCaseCount() {
			return caseCount;
		}
		public int getCaseIndex() {
			return currentIndex;
		}
		public Template.Instance getCase(int index) {
			return (index >= 0 && index < caseCount) ? entries.get(index) : null;
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
			if (currentIndex >= 0 && currentIndex < caseCount) {
				Template.Instance entry = entries.get(currentIndex);
				if (entry != null) entry.write(out);
			}
		}
	}
}
