package com.kreative.templature.main;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class FileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public FileMenu(TemplateEditorFrame f) {
		super("File");
		add(new NewMenuItem());
		add(new OpenMenuItem());
		add(new CloseMenuItem(f));
		addSeparator();
		add(new SaveMenuItem(f));
		add(new SaveAsMenuItem(f));
		add(new SaveAsBinaryMenuItem(f));
		add(new SaveAsTextMenuItem(f));
		if (!SwingUtils.IS_MAC_OS) {
			addSeparator();
			add(new ExitMenuItem());
		}
	}
	
	public FileMenu(TemplateInstanceEditorFrame f) {
		super("File");
		add(new NewMenuItem());
		add(new OpenMenuItem());
		add(new CloseMenuItem(f));
		addSeparator();
		add(new SaveMenuItem(f));
		add(new SaveAsMenuItem(f));
		if (!SwingUtils.IS_MAC_OS) {
			addSeparator();
			add(new ExitMenuItem());
		}
	}
	
	public static class NewMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public NewMenuItem() {
			super("New...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.newTemplateInstanceEditor(null);
				}
			});
		}
	}
	
	public static class OpenMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public OpenMenuItem() {
			super("Open...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Main.openTemplateInstanceEditor(null, null);
				}
			});
		}
	}
	
	public static class CloseMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public CloseMenuItem(final Window window) {
			super("Close Window");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
				}
			});
		}
	}
	
	public static class SaveMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public SaveMenuItem(final TemplateEditorFrame f) {
			super("Save");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.save();
				}
			});
		}
		public SaveMenuItem(final TemplateInstanceEditorFrame f) {
			super("Save");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.save();
				}
			});
		}
	}
	
	public static class SaveAsMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public SaveAsMenuItem(final TemplateEditorFrame f) {
			super("Save As...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.saveAs();
				}
			});
		}
		public SaveAsMenuItem(final TemplateInstanceEditorFrame f) {
			super("Save As...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.saveAs();
				}
			});
		}
	}
	
	public static class SaveAsBinaryMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public SaveAsBinaryMenuItem(final TemplateEditorFrame f) {
			super("Save As (Binary Format)...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SwingUtils.SHORTCUT_KEY | KeyEvent.ALT_MASK));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.saveAs(false);
				}
			});
		}
	}
	
	public static class SaveAsTextMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public SaveAsTextMenuItem(final TemplateEditorFrame f) {
			super("Save As (Text Format)...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, SwingUtils.SHORTCUT_KEY | KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					f.saveAs(true);
				}
			});
		}
	}
	
	public static class ExitMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public ExitMenuItem() {
			super("Exit");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.gc();
					for (Window window : Window.getWindows()) {
						if (window.isVisible()) {
							window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
							if (window.isVisible()) return;
						}
					}
					System.exit(0);
				}
			});
		}
	}
}
