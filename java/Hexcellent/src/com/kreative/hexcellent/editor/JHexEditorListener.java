package com.kreative.hexcellent.editor;

import com.kreative.hexcellent.buffer.ByteBufferDocument;
import com.kreative.hexcellent.buffer.ByteBufferListener;
import com.kreative.hexcellent.buffer.ByteBufferSelectionListener;

public interface JHexEditorListener extends ByteBufferListener, ByteBufferSelectionListener {
	public void documentChanged(JHexEditor editor, ByteBufferDocument document);
	public void colorsChanged(JHexEditor editor, JHexEditorColors colors);
	public void editorStatusChanged(JHexEditor editor);
}
