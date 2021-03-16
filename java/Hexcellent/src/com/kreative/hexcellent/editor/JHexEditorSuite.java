package com.kreative.hexcellent.editor;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.ByteBufferDocument;

public class JHexEditorSuite extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JHexEditor editor;
	private final JScrollPane scrollPane;
	private final JHexEditorHeader header;
	private final JHexEditorInspector inspector;
	
	public JHexEditorSuite() {
		this(new JHexEditor());
	}
	
	public JHexEditorSuite(byte[] data) {
		this(new JHexEditor(data));
	}
	
	public JHexEditorSuite(ByteBuffer buffer) {
		this(new JHexEditor(buffer));
	}
	
	public JHexEditorSuite(ByteBufferDocument document) {
		this(new JHexEditor(document));
	}
	
	public JHexEditorSuite(JHexEditor editor) {
		this.editor = editor;
		this.scrollPane = new JScrollPane(
			editor,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		this.header = new JHexEditorHeader(editor);
		this.inspector = new JHexEditorInspector(editor);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		add(header, BorderLayout.NORTH);
		add(inspector, BorderLayout.SOUTH);
	}
	
	public JHexEditor getEditor() {
		return this.editor;
	}
	
	public JScrollPane getScrollPane() {
		return this.scrollPane;
	}
	
	public JHexEditorHeader getHeader() {
		return this.header;
	}
	
	public JHexEditorInspector getInspector() {
		return this.inspector;
	}
}
