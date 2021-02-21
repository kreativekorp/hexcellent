package com.kreative.hexcellent.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.ByteBufferDocument;
import com.kreative.hexcellent.buffer.ByteBufferSelectionModel;
import com.kreative.hexcellent.editor.JHexEditor;
import com.kreative.hexcellent.editor.JHexEditorColors;
import com.kreative.hexcellent.editor.JHexEditorListener;

public class ColorsMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	private final Map<JHexEditorColors,JRadioButtonMenuItem> items;
	
	public ColorsMenu(final JHexEditor editor) {
		this(editor, JHexEditorColors.BUILTINS);
	}
	
	public ColorsMenu(final JHexEditor editor, final JHexEditorColors... colors) {
		this(editor, Arrays.asList(colors));
	}
	
	public ColorsMenu(final JHexEditor editor, final Collection<JHexEditorColors> c) {
		super("Colors");
		items = new HashMap<JHexEditorColors,JRadioButtonMenuItem>();
		for (final JHexEditorColors colors : c) {
			JRadioButtonMenuItem mi = new JRadioButtonMenuItem(colors.name);
			mi.setSelected(editor.getColors().equals(colors));
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.setColors(colors);
				}
			});
			items.put(colors, mi);
			add(mi);
		}
		editor.addHexEditorListener(new JHexEditorListener() {
			@Override
			public void colorsChanged(JHexEditor editor, JHexEditorColors colors) {
				for (Map.Entry<JHexEditorColors,JRadioButtonMenuItem> e : items.entrySet()) {
					e.getValue().setSelected(e.getKey().equals(colors));
				}
			}
			@Override public void dataInserted(ByteBuffer buffer, long offset, int length) {}
			@Override public void dataOverwritten(ByteBuffer buffer, long offset, int length) {}
			@Override public void dataRemoved(ByteBuffer buffer, long offset, long length) {}
			@Override public void selectionChanged(ByteBufferSelectionModel sm, long start, long end) {}
			@Override public void documentChanged(JHexEditor editor, ByteBufferDocument document) {}
			@Override public void editorStatusChanged(JHexEditor editor) {}
		});
	}
}
