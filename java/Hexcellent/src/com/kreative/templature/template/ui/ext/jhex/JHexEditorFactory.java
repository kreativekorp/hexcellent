package com.kreative.templature.template.ui.ext.jhex;

import java.awt.Component;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.ByteBufferDocument;
import com.kreative.hexcellent.buffer.ByteBufferSelectionModel;
import com.kreative.hexcellent.editor.JHexEditor;
import com.kreative.hexcellent.editor.JHexEditorColors;
import com.kreative.hexcellent.editor.JHexEditorInspector;
import com.kreative.hexcellent.editor.JHexEditorListener;
import com.kreative.hexcellent.editor.JHexEditorSuite;
import com.kreative.templature.template.HexDataModel;
import com.kreative.templature.template.ui.swing.HexAreaFactory;

public class JHexEditorFactory extends HexAreaFactory {
	public Component createHexAreaComponent(final HexDataModel m) {
		final JHexEditorSuite suite = new JHexEditorSuite(m.getHexValue());
		final JHexEditorInspector inspector = suite.getInspector();
		final JHexEditor editor = suite.getEditor();
		inspector.setVisible(false);
		editor.setPreferredRowCount(8);
		editor.setEnableShortcutKeys(true);
		editor.setCharset(m.getHexEncoding());
		editor.setLittleEndian(m.getHexLittleEndian());
		editor.addHexEditorListener(new JHexEditorListener() {
			public void dataInserted(ByteBuffer buffer, long offset, int length) { update(); }
			public void dataOverwritten(ByteBuffer buffer, long offset, int length) { update(); }
			public void dataRemoved(ByteBuffer buffer, long offset, long length) { update(); }
			public void selectionChanged(ByteBufferSelectionModel sm, long start, long end) {}
			public void documentChanged(JHexEditor editor, ByteBufferDocument document) {}
			public void colorsChanged(JHexEditor editor, JHexEditorColors colors) {}
			public void editorStatusChanged(JHexEditor editor) {}
			private void update() {
				long length = editor.length();
				int len = (length < Integer.MAX_VALUE) ? (int)length : Integer.MAX_VALUE;
				byte[] data = new byte[len];
				editor.get(0, data, 0, len);
				m.setHexValue(data);
			}
		});
		return suite;
	}
}
