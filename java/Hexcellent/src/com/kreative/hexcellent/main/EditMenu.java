package com.kreative.hexcellent.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.kreative.hexcellent.editor.JHexEditor;

public class EditMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public EditMenu(JHexEditor editor) {
		super("Edit");
		add(new UndoMenuItem(editor));
		add(new RedoMenuItem(editor));
		addSeparator();
		add(new CutMenuItem(editor));
		add(new CopyMenuItem(editor));
		add(new PasteMenuItem(editor));
		add(new ClearMenuItem(editor));
		addSeparator();
		add(new SelectAllMenuItem(editor));
		add(new GoToAddressMenuItem(editor));
		editor.setEnableShortcutKeys(false);
	}
	
	public static class UndoMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public UndoMenuItem(final JHexEditor editor) {
			super("Undo");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.undo();
				}
			});
		}
	}
	
	public static class RedoMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public RedoMenuItem(final JHexEditor editor) {
			super("Redo");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.redo();
				}
			});
		}
	}
	
	public static class CutMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public CutMenuItem(final JHexEditor editor) {
			super("Cut");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.cut();
				}
			});
		}
	}
	
	public static class CopyMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public CopyMenuItem(final JHexEditor editor) {
			super("Copy");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.copy();
				}
			});
		}
	}
	
	public static class PasteMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public PasteMenuItem(final JHexEditor editor) {
			super("Paste");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.paste();
				}
			});
		}
	}
	
	public static class ClearMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public ClearMenuItem(final JHexEditor editor) {
			super("Clear");
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.deleteSelection("Clear");
				}
			});
		}
	}
	
	public static class SelectAllMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public SelectAllMenuItem(final JHexEditor editor) {
			super("Select All");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.selectAll();
				}
			});
		}
	}
	
	public static class GoToAddressMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public GoToAddressMenuItem(final JHexEditor editor) {
			super("Go To Address...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.showSetSelectionDialog("Go To Address");
				}
			});
		}
	}
}
