package com.kreative.hexcellent.main;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.kreative.hexcellent.buffer.ArrayByteBuffer;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.ByteBufferDocument;
import com.kreative.hexcellent.buffer.ByteBufferSelectionModel;
import com.kreative.hexcellent.buffer.CompositeByteBuffer;
import com.kreative.hexcellent.buffer.MMapByteBuffer;
import com.kreative.hexcellent.buffer.RafByteBuffer;
import com.kreative.hexcellent.editor.JHexEditor;
import com.kreative.hexcellent.editor.JHexEditorColors;
import com.kreative.hexcellent.editor.JHexEditorListener;
import com.kreative.hexcellent.editor.JHexEditorSuite;

public class EditorFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int VM_THRESHOLD = (16 << 20);
	
	private final CompositeByteBuffer buffer;
	private final ByteBufferDocument document;
	private final JHexEditor editor;
	private final JHexEditorSuite suite;
	private final EditorMenuBar menubar;
	private File file;
	private boolean changed;
	
	public EditorFrame() {
		this.buffer = new CompositeByteBuffer();
		this.document = new ByteBufferDocument(buffer);
		this.editor = new JHexEditor(document);
		this.suite = new JHexEditorSuite(editor);
		this.menubar = new EditorMenuBar(this, editor);
		this.file = null;
		this.changed = false;
		build();
	}
	
	public EditorFrame(byte[] data) {
		this.buffer = new CompositeByteBuffer(new ArrayByteBuffer(data));
		this.document = new ByteBufferDocument(buffer);
		this.editor = new JHexEditor(document);
		this.suite = new JHexEditorSuite(editor);
		this.menubar = new EditorMenuBar(this, editor);
		this.file = null;
		this.changed = false;
		build();
	}
	
	public EditorFrame(File file) throws IOException {
		this.buffer = new CompositeByteBuffer(readFromFile(file));
		this.document = new ByteBufferDocument(buffer);
		this.editor = new JHexEditor(document);
		this.suite = new JHexEditorSuite(editor);
		this.menubar = new EditorMenuBar(this, editor);
		this.file = file;
		this.changed = false;
		build();
	}
	
	private void build() {
		setContentPane(suite);
		setJMenuBar(menubar);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		updateWindow();
		editor.addHexEditorListener(editorListener);
		editor.addComponentListener(componentListener);
		addWindowListener(windowListener);
	}
	
	private static List<ByteBuffer> readFromFile(File f) throws IOException {
		// Try mapping the entire file into memory.
		try {
			List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
			@SuppressWarnings("resource")
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			FileChannel fc = raf.getChannel();
			long offset = 0;
			long length = raf.length();
			while (length >= VM_THRESHOLD) {
				buffers.add(new MMapByteBuffer(fc, offset, VM_THRESHOLD));
				offset += VM_THRESHOLD;
				length -= VM_THRESHOLD;
			}
			if (length > 0) {
				buffers.add(new MMapByteBuffer(fc, offset, length));
			}
			System.out.println("Mapped file into memory.");
			return buffers;
		} catch (Throwable t) {
			System.gc();
		}
		// Fall back on array or random access.
		if (f.length() < VM_THRESHOLD) {
			FileInputStream in = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[VM_THRESHOLD]; int read;
			while ((read = in.read(buf)) >= 0) out.write(buf, 0, read);
			in.close();
			out.close();
			System.out.println("Loaded file into array.");
			return Arrays.asList(new ArrayByteBuffer(out.toByteArray()));
		} else {
			System.out.println("Opened file for random access.");
			return Arrays.asList(new RafByteBuffer(f));
		}
	}
	
	private static void writeToFile(ByteBuffer bb, File f) throws IOException {
		FileOutputStream out = new FileOutputStream(f);
		bb.write(out, 0, bb.length());
		out.flush();
		out.close();
	}
	
	private static void copyFile(File inf, File outf) throws IOException {
		FileInputStream in = new FileInputStream(inf);
		FileOutputStream out = new FileOutputStream(outf);
		byte[] buf = new byte[VM_THRESHOLD]; int read;
		while ((read = in.read(buf)) >= 0) out.write(buf, 0, read);
		out.flush();
		out.close();
		in.close();
	}
	
	public JHexEditor getEditor() {
		return editor;
	}
	
	public JHexEditorSuite getEditorSuite() {
		return suite;
	}
	
	public boolean save() {
		if (file == null) {
			return saveAs();
		} else try {
			File tmp = File.createTempFile("Hexcellent", ".bin");
			writeToFile(buffer, tmp);
			copyFile(tmp, file);
			List<ByteBuffer> bb = readFromFile(file);
			buffer.getBackingBufferList().clear();
			buffer.getBackingBufferList().addAll(bb);
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
	
	public boolean saveAs() {
		FileDialog fd = new FileDialog(this, "Save", FileDialog.SAVE);
		fd.setVisible(true);
		String parent = fd.getDirectory();
		String name = fd.getFile();
		if (parent == null || name == null) return false;
		file = new File(parent, name);
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
	
	private final JHexEditorListener editorListener = new JHexEditorListener() {
		@Override
		public void dataInserted(ByteBuffer buffer, long offset, int length) {
			if (changed) return;
			changed = true;
			updateWindow();
		}
		@Override
		public void dataOverwritten(ByteBuffer buffer, long offset, int length) {
			if (changed) return;
			changed = true;
			updateWindow();
		}
		@Override
		public void dataRemoved(ByteBuffer buffer, long offset, long length) {
			if (changed) return;
			changed = true;
			updateWindow();
		}
		@Override public void selectionChanged(ByteBufferSelectionModel sm, long start, long end) {}
		@Override public void documentChanged(JHexEditor editor, ByteBufferDocument document) {}
		@Override public void colorsChanged(JHexEditor editor, JHexEditorColors colors) {}
		@Override public void editorStatusChanged(JHexEditor editor) {}
	};
	
	private final ComponentAdapter componentListener = new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			int wd = getWidth() - editor.getWidth();
			int hd = getHeight() - editor.getHeight();
			Dimension emin = editor.getMinimumSize();
			Dimension fmin = new Dimension(emin.width + wd, emin.height + hd);
			setMinimumSize(fmin);
		}
	};
	
	private final WindowAdapter windowListener = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			if (!changed || (file == null && buffer.isEmpty())) {
				dispose();
			} else {
				String title = (file == null) ? "Untitled" : file.getName();
				switch (new SaveChangesDialog(EditorFrame.this, title).showDialog()) {
					case SAVE: if (save()) dispose(); break;
					case DONT_SAVE: dispose(); break;
					case CANCEL: break;
				}
			}
		}
	};
}
