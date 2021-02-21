package com.kreative.hexcellent.main;

import javax.swing.JMenuBar;
import com.kreative.hexcellent.editor.JHexEditor;

public class EditorMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	public EditorMenuBar(EditorFrame f, JHexEditor editor) {
		add(new FileMenu(f));
		add(new EditMenu(editor));
		add(new OptionsMenu(editor));
		add(new ColorsMenu(editor));
	}
}
