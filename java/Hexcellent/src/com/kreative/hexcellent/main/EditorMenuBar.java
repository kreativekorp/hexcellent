package com.kreative.hexcellent.main;

import javax.swing.JMenuBar;
import com.kreative.hexcellent.editor.JHexEditor;

public class EditorMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	public EditorMenuBar(EditorFrame f, JHexEditor editor) {
		add(new FileMenu(f));
		add(new EditMenu(f, editor));
		add(new SearchMenu(f, editor));
		add(new OptionsMenu(f, editor));
	}
}
