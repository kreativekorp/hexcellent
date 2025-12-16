package com.kreative.templature.main;

import javax.swing.JMenuBar;

public class EditorMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	public EditorMenuBar(TemplateEditorFrame f) {
		add(new FileMenu(f));
		add(new TemplateMenu(f));
	}
	
	public EditorMenuBar(TemplateInstanceEditorFrame f) {
		add(new FileMenu(f));
	}
}
