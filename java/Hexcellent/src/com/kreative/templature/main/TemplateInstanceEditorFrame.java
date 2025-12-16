package com.kreative.templature.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.kreative.templature.template.BufferedCountedInputStream;
import com.kreative.templature.template.BufferedCountedOutputStream;
import com.kreative.templature.template.Closure;
import com.kreative.templature.template.ClosureItem;
import com.kreative.templature.template.ClosureListener;
import com.kreative.templature.template.Template;
import com.kreative.templature.template.ui.swing.SwingComponentFactory;

public class TemplateInstanceEditorFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final Closure closure;
	private final Template template;
	private final Template.Instance instance;
	private final SwingComponentFactory factory;
	private final Component component;
	private File file;
	private boolean changed;
	
	public TemplateInstanceEditorFrame(Template template) throws IOException {
		BufferedCountedInputStream in =
			new BufferedCountedInputStream(
				new ByteArrayInputStream(new byte[0]));
		this.closure = new Closure(null);
		this.template = template;
		this.instance = template.read(closure, in);
		this.factory = new SwingComponentFactory();
		this.component = instance.createComponent(factory);
		this.file = null;
		this.changed = false;
		in.close();
		build();
	}
	
	public TemplateInstanceEditorFrame(Template template, byte[] data) throws IOException {
		BufferedCountedInputStream in =
			new BufferedCountedInputStream(
				new ByteArrayInputStream(data));
		this.closure = new Closure(null);
		this.template = template;
		this.instance = template.read(closure, in);
		this.factory = new SwingComponentFactory();
		this.component = instance.createComponent(factory);
		this.file = null;
		this.changed = false;
		in.close();
		build();
	}
	
	public TemplateInstanceEditorFrame(Template template, File file) throws IOException {
		BufferedCountedInputStream in =
			new BufferedCountedInputStream(
				new FileInputStream(file));
		this.closure = new Closure(null);
		this.template = template;
		this.instance = template.read(closure, in);
		this.factory = new SwingComponentFactory();
		this.component = instance.createComponent(factory);
		this.file = file;
		this.changed = false;
		in.close();
		build();
	}
	
	private void build() {
		final JPanel p = new JPanel(new BorderLayout());
		p.add(component, BorderLayout.PAGE_START);
		p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		final JScrollPane s = new JScrollPane(
			p,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		
		setContentPane(s);
		setJMenuBar(new EditorMenuBar(this));
		setSize(Math.max(600, getPreferredSize().width), 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		updateWindow();
		closure.addClosureListener(closureListener);
		addWindowListener(windowListener);
	}
	
	public Closure getTemplateClosure() { return closure; }
	public Template getTemplate() { return template; }
	public Template.Instance getTemplateInstance() { return instance; }
	public SwingComponentFactory getTemplateComponentFactory() { return factory; }
	public Component getTemplateComponent() { return component; }
	
	public boolean save() {
		if (file == null) {
			return saveAs();
		} else try {
			BufferedCountedOutputStream out =
				new BufferedCountedOutputStream(
					new FileOutputStream(file));
			instance.write(out);
			out.flush();
			out.close();
			changed = false;
			updateWindow();
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
				this, "An error occurred while saving this file.",
				"Save", JOptionPane.ERROR_MESSAGE
			);
			return false;
		}
	}
	
	private static String lastSaveDirectory = null;
	public boolean saveAs() {
		FileDialog fd = new FileDialog(this, "Save", FileDialog.SAVE);
		if (lastSaveDirectory != null) fd.setDirectory(lastSaveDirectory);
		fd.setVisible(true);
		String parent = fd.getDirectory();
		String name = fd.getFile();
		fd.dispose();
		if (parent == null || name == null) return false;
		file = new File((lastSaveDirectory = parent), name);
		return save();
	}
	
	private void updateWindow() {
		String title = (file == null) ? "Untitled" : file.getName();
		if (SwingUtils.IS_MAC_OS) {
			getRootPane().putClientProperty("Window.documentFile", file);
			getRootPane().putClientProperty("Window.documentModified", changed);
			setTitle(title);
		} else {
			setTitle(changed ? (title + " \u2022") : title);
		}
	}
	
	private final ClosureListener closureListener = new ClosureListener() {
		@Override
		public void valueChanged(ClosureItem item) {
			if (changed) return;
			changed = true;
			updateWindow();
		}
	};
	
	private final WindowAdapter windowListener = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			if (!changed) {
				dispose();
			} else {
				String title = (file == null) ? "Untitled" : file.getName();
				switch (new SaveChangesDialog(TemplateInstanceEditorFrame.this, title).showDialog()) {
					case SAVE: if (save()) dispose(); break;
					case DONT_SAVE: dispose(); break;
					case CANCEL: break;
				}
			}
		}
	};
}
