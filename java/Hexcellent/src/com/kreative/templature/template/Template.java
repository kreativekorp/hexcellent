package com.kreative.templature.template;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class Template extends ArrayList<TemplateItem> implements TemplateItem {
	private static final long serialVersionUID = 1L;
	
	public Instance read(Closure closure, BufferedCountedInputStream in) throws IOException {
		Instance inst = new Instance(this.size());
		for (TemplateItem item : this) inst.add(item.read(closure, in));
		return inst;
	}
	
	public void write(BinaryTemplateOutputStream out) throws IOException {
		for (TemplateItem item : this) item.write(out);
	}
	
	public void print(PrintWriter out, String prefix) {
		for (TemplateItem item : this) item.print(out, prefix);
	}
	
	public final class Instance extends ArrayList<TemplateItemInstance> implements TemplateItemInstance {
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
			for (TemplateItemInstance inst : Instance.this) {
				labels.add(inst.getLabelText());
				components.add(inst.createComponent(factory));
			}
			return factory.createFormComponent(labels, components, size());
		}
		public void write(BufferedCountedOutputStream out) throws IOException {
			for (TemplateItemInstance inst : Instance.this) inst.write(out);
		}
	}
}
