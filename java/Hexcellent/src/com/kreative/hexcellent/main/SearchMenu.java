package com.kreative.hexcellent.main;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.kreative.hexcellent.editor.JHexEditor;

public class SearchMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public SearchMenu(EditorFrame f, JHexEditor editor) {
		super("Search");
		add(new FindMenuItem(f));
		add(new FindNextMenuItem(editor));
		add(new FindPreviousMenuItem(editor));
		addSeparator();
		add(new FindReplaceMenuItem(f));
		add(new ReplaceAndFindNextMenuItem(editor));
		add(new ReplaceAndFindPreviousMenuItem(editor));
		add(new ReplaceSelectionMenuItem(editor));
		add(new ReplaceAllMenuItem(editor));
		addSeparator();
		add(new UseSelectionForFindMenuItem(editor));
		add(new UseSelectionForReplaceMenuItem(editor));
	}
	
	public static class FindMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public FindMenuItem(final EditorFrame f) {
			super("Find...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FindReplacePanel.getInstance().showDialog(f);
				}
			});
		}
	}
	
	public static class FindNextMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public FindNextMenuItem(final JHexEditor editor) {
			super("Find Next");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!FindReplacePanel.getInstance().findNext(editor)) {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			});
		}
	}
	
	public static class FindPreviousMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public FindPreviousMenuItem(final JHexEditor editor) {
			super("Find Previous");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!FindReplacePanel.getInstance().findPrevious(editor)) {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			});
		}
	}
	
	public static class FindReplaceMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public FindReplaceMenuItem(final EditorFrame f) {
			super("Find & Replace...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FindReplacePanel.getInstance().showDialog(f);
				}
			});
		}
	}
	
	public static class ReplaceAndFindNextMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public ReplaceAndFindNextMenuItem(final JHexEditor editor) {
			super("Replace & Find Next");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (
						!FindReplacePanel.getInstance().replace(editor) ||
						!FindReplacePanel.getInstance().findNext(editor)
					) {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			});
		}
	}
	
	public static class ReplaceAndFindPreviousMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public ReplaceAndFindPreviousMenuItem(final JHexEditor editor) {
			super("Replace & Find Previous");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (
						!FindReplacePanel.getInstance().replace(editor) ||
						!FindReplacePanel.getInstance().findPrevious(editor)
					) {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			});
		}
	}
	
	public static class ReplaceSelectionMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public ReplaceSelectionMenuItem(final JHexEditor editor) {
			super("Replace Selection");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!FindReplacePanel.getInstance().replace(editor)) {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			});
		}
	}
	
	public static class ReplaceAllMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public ReplaceAllMenuItem(final JHexEditor editor) {
			super("Replace All");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!FindReplacePanel.getInstance().replaceAll(editor)) {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			});
		}
	}
	
	public static class UseSelectionForFindMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public UseSelectionForFindMenuItem(final JHexEditor editor) {
			super("Use Selection For Find");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!FindReplacePanel.getInstance().useSelectionForFind(editor)) {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			});
		}
	}
	
	public static class UseSelectionForReplaceMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public UseSelectionForReplaceMenuItem(final JHexEditor editor) {
			super("Use Selection For Replace");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!FindReplacePanel.getInstance().useSelectionForReplace(editor)) {
						Toolkit.getDefaultToolkit().beep();
					}
				}
			});
		}
	}
}
