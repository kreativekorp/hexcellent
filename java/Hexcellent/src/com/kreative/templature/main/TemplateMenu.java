package com.kreative.templature.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class TemplateMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public TemplateMenu(TemplateEditorFrame f) {
		super("Template");
		add(new EditAsBinaryMenuItem(f));
		add(new EditAsTextMenuItem(f));
		add(new CheckSyntaxMenuItem(f));
	}
	
	public static class EditAsBinaryMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public EditAsBinaryMenuItem(final TemplateEditorFrame f) {
			super("Edit As Binary");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.setTextMode(false);
				}
			});
		}
	}
	
	public static class EditAsTextMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public EditAsTextMenuItem(final TemplateEditorFrame f) {
			super("Edit As Text");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.setTextMode(true);
				}
			});
		}
	}
	
	public static class CheckSyntaxMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public CheckSyntaxMenuItem(final TemplateEditorFrame f) {
			super("Check Syntax");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.compile();
				}
			});
		}
	}
}
