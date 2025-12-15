package com.kreative.templature.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.kreative.templature.template.BinaryTemplateFactory;
import com.kreative.templature.template.BinaryTemplateInputStream;
import com.kreative.templature.template.BinaryTemplateOutputStream;
import com.kreative.templature.template.BufferedCountedInputStream;
import com.kreative.templature.template.BufferedCountedOutputStream;
import com.kreative.templature.template.Closure;
import com.kreative.templature.template.ClosureItem;
import com.kreative.templature.template.ClosureListener;
import com.kreative.templature.template.Template;
import com.kreative.templature.template.TemplateConstants;
import com.kreative.templature.template.TemplateException;
import com.kreative.templature.template.TextTemplateFactory;
import com.kreative.templature.template.TextTemplateTokenStream;
import com.kreative.templature.template.item.CharacterItem;
import com.kreative.templature.template.item.PascalStringItem;
import com.kreative.templature.template.item.UnterminatedListBlock;
import com.kreative.templature.template.ui.swing.SwingComponentFactory;

public class TemplateEditorFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static final int $LSTB = TemplateConstants.$LSTB;
	private static final int $LSTE = TemplateConstants.$LSTE;
	private static final int $PSTR = TemplateConstants.$PSTR;
	private static final int $TNAM = TemplateConstants.$TNAM;
	private static final int PSILV = PascalStringItem.LENGTH_VARIABLE;
	
	private static final Template createMetaTemplate() {
		return new Template(Arrays.asList(
			new UnterminatedListBlock($LSTB, $LSTE, "list", "*****", "*****", Arrays.asList(
				new PascalStringItem($PSTR, "pstringbe", "Label", false, 8, PSILV, "MacRoman", "\r"),
				new CharacterItem($TNAM, "tnamebe", "Type", false, 32, "MacRoman", " ")
			))
		));
	}
	
	private static final BinaryTemplateFactory createBinaryTemplateFactory() {
		BinaryTemplateFactory btf = new BinaryTemplateFactory();
		btf.registerDefaults();
		btf.registerExtensions();
		return btf;
	}
	
	private static final TextTemplateFactory createTextTemplateFactory() {
		TextTemplateFactory ttf = new TextTemplateFactory();
		ttf.registerDefaults();
		ttf.registerExtensions();
		return ttf;
	}
	
	private static final byte[] copy(byte[] in) {
		byte[] out = new byte[in.length];
		for (int i = 0; i < in.length; i++) out[i] = in[i];
		return out;
	}
	
	private static final byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[65536]; int read;
		while ((read = in.read(buf)) >= 0) out.write(buf, 0, read);
		out.flush(); out.close(); return out.toByteArray();
	}
	
	private static final byte[] read(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] out = read(in);
		in.close();
		return out;
	}
	
	private static final boolean isProbablyTextFormat(byte[] data) {
		boolean hasC0 = false;
		boolean hasCB = false;
		for (byte b : data) {
			if (b == ';' || b == '{' || b == '}') hasCB = true;
			if (b == 9 || b == 10 || b == 13) continue;
			if (b >= 0 && b < 32) hasC0 = true;
		}
		if (hasC0) return false;
		if (hasCB) return true;
		return false;
	}
	
	private final BinaryTemplateFactory btf = createBinaryTemplateFactory();
	private final TextTemplateFactory ttf = createTextTemplateFactory();
	private final Closure closure = new Closure(null);
	private final Template metaTemplate = createMetaTemplate();
	private final SwingComponentFactory scf = new SwingComponentFactory();
	private final JPanel templateEditorContainer = new JPanel(new BorderLayout());
	private final JTextArea sourceTextArea = new JTextArea();
	private final CardLayout cardLayout = new CardLayout();
	private final JPanel cardPanel = new JPanel(cardLayout);
	private byte[] binaryData;
	private Template.Instance metaInstance;
	private Component templateEditor;
	private String sourceText;
	private File file;
	private boolean textFile;
	private boolean textMode;
	private boolean changed = false;
	private boolean compiled = false;
	
	public TemplateEditorFrame(Boolean text) throws IOException {
		if (text == null) text = false;
		this.binaryData = new byte[0];
		this.sourceText = "";
		this.file = null;
		this.textFile = text.booleanValue();
		this.textMode = text.booleanValue();
		build();
	}
	
	public TemplateEditorFrame(byte[] data) throws IOException {
		this.binaryData = copy(data);
		this.sourceText = "";
		this.file = null;
		this.textFile = false;
		this.textMode = false;
		build();
	}
	
	public TemplateEditorFrame(String source) throws IOException {
		this.binaryData = new byte[0];
		this.sourceText = source;
		this.file = null;
		this.textFile = true;
		this.textMode = true;
		build();
	}
	
	public TemplateEditorFrame(File file, Boolean text) throws IOException {
		byte[] data = read(file);
		if (text == null) text = isProbablyTextFormat(data);
		this.binaryData = text.booleanValue() ? new byte[0] : data;
		this.sourceText = text.booleanValue() ? new String(data, "UTF-8") : "";
		this.file = file;
		this.textFile = text.booleanValue();
		this.textMode = text.booleanValue();
		build();
	}
	
	private void build() throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(binaryData);
		BufferedCountedInputStream btin = new BufferedCountedInputStream(bin);
		metaInstance = metaTemplate.read(closure, btin);
		btin.close();
		
		templateEditor = metaInstance.createComponent(scf);
		templateEditorContainer.add(templateEditor, BorderLayout.PAGE_START);
		templateEditorContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		sourceTextArea.setFont(new Font("Monospaced", 0, sourceTextArea.getFont().getSize()));
		sourceTextArea.setText(sourceText);
		
		final JScrollPane bpane = new JScrollPane(
			templateEditorContainer,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		final JScrollPane tpane = new JScrollPane(
			sourceTextArea,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
		);
		cardPanel.add(bpane, "binary");
		cardPanel.add(tpane, "text");
		cardLayout.show(cardPanel, textMode ? "text" : "binary");
		
		setContentPane(cardPanel);
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		updateWindow();
		closure.addClosureListener(closureListener);
		sourceTextArea.getDocument().addDocumentListener(documentListener);
		addWindowListener(windowListener);
	}
	
	public boolean setTextMode(boolean textMode) {
		if (textMode) {
			// Set to text mode.
			if (this.textMode) {
				return true;
			} else if (compiled || binaryToText("Compile")) {
				this.textMode = true;
				cardLayout.show(cardPanel, "text");
				return true;
			} else {
				return false;
			}
		} else {
			// Set to binary mode.
			if (!this.textMode) {
				return true;
			} else if (compiled || textToBinary("Compile")) {
				this.textMode = false;
				cardLayout.show(cardPanel, "binary");
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean save() {
		if (compile("Save")) {
			if (file == null) {
				return saveAs();
			} else try {
				FileOutputStream out = new FileOutputStream(file);
				if (textFile) out.write(sourceText.getBytes("UTF-8"));
				else out.write(binaryData);
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
		} else {
			return false;
		}
	}
	
	private static String lastSaveDirectory = null;
	public boolean saveAs(boolean textFile) {
		if (compile("Save")) {
			FileDialog fd = new FileDialog(this, "Save", FileDialog.SAVE);
			if (lastSaveDirectory != null) fd.setDirectory(lastSaveDirectory);
			fd.setVisible(true);
			String parent = fd.getDirectory();
			String name = fd.getFile();
			fd.dispose();
			if (parent == null || name == null) return false;
			this.file = new File((lastSaveDirectory = parent), name);
			this.textFile = textFile;
			return save();
		} else {
			return false;
		}
	}
	
	public boolean saveAs() {
		return saveAs(textFile);
	}
	
	public boolean compile() {
		return compile("Compile");
	}
	
	private boolean compile(String action) {
		return compiled || (textMode ? textToBinary(action) : binaryToText(action));
	}
	
	private boolean binaryToText(String action) {
		try {
			// metatemplate instance -> binary data
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			BufferedCountedOutputStream btout = new BufferedCountedOutputStream(bout);
			metaInstance.write(btout);
			btout.flush();
			btout.close();
			binaryData = bout.toByteArray();
			// binary data -> template
			ByteArrayInputStream bin = new ByteArrayInputStream(binaryData);
			BinaryTemplateInputStream btin = new BinaryTemplateInputStream(bin);
			Template t = btf.createTemplate(btin);
			btin.close();
			// template -> source text
			StringWriter tout = new StringWriter();
			PrintWriter ttout = new PrintWriter(tout, true);
			t.print(ttout, "");
			ttout.flush();
			ttout.close();
			sourceText = tout.toString();
			sourceTextArea.setText(sourceText);
			compiled = true;
			return true;
		} catch (TemplateException te) {
			JOptionPane.showMessageDialog(
				this, "This template is invalid. " + te.getMessage(),
				action, JOptionPane.ERROR_MESSAGE
			);
			return false;
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(
				this, "An error occurred while compiling this template.",
				action, JOptionPane.ERROR_MESSAGE
			);
			return false;
		}
	}
	
	private boolean textToBinary(String action) {
		try {
			// source text -> template
			sourceText = sourceTextArea.getText();
			TextTemplateTokenStream ttin = new TextTemplateTokenStream(sourceText);
			Template t = ttf.createTemplate(ttin);
			ttin.close();
			// template -> binary data
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			BinaryTemplateOutputStream btout = new BinaryTemplateOutputStream(bout);
			t.write(btout);
			btout.flush();
			btout.close();
			binaryData = bout.toByteArray();
			// binary data -> metatemplate instance
			ByteArrayInputStream bin = new ByteArrayInputStream(binaryData);
			BufferedCountedInputStream btin = new BufferedCountedInputStream(bin);
			metaInstance = metaTemplate.read(closure, btin);
			btin.close();
			templateEditor = metaInstance.createComponent(scf);
			templateEditorContainer.removeAll();
			templateEditorContainer.add(templateEditor, BorderLayout.PAGE_START);
			compiled = true;
			return true;
		} catch (TemplateException te) {
			JOptionPane.showMessageDialog(
				this, "This template is invalid. " + te.getMessage(),
				"Compile", JOptionPane.ERROR_MESSAGE
			);
			return false;
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(
				this, "An error occurred while compiling this template.",
				"Compile", JOptionPane.ERROR_MESSAGE
			);
			return false;
		}
	}
	
	private void updateWindow() {
		String title = (file == null) ? "Untitled" : file.getName();
		String typeind = " <" + (textFile ? "T" : "B") + ">";
		if (SwingUtils.IS_MAC_OS) {
			getRootPane().putClientProperty("Window.documentFile", file);
			getRootPane().putClientProperty("Window.documentModified", changed);
			setTitle(title + typeind);
		} else {
			setTitle(changed ? (title + typeind + " \u2022") : (title + typeind));
		}
	}
	
	private final ClosureListener closureListener = new ClosureListener() {
		@Override
		public void valueChanged(ClosureItem item) {
			compiled = false;
			if (changed) return;
			changed = true;
			updateWindow();
		}
	};
	
	private final DocumentListener documentListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent e) {
			compiled = false;
			if (changed) return;
			changed = true;
			updateWindow();
		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			compiled = false;
			if (changed) return;
			changed = true;
			updateWindow();
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
			compiled = false;
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
				switch (new SaveChangesDialog(TemplateEditorFrame.this, title).showDialog()) {
					case SAVE: if (save()) dispose(); break;
					case DONT_SAVE: dispose(); break;
					case CANCEL: break;
				}
			}
		}
	};
}
