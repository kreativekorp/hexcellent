package com.kreative.hexcellent.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.kreative.hexcellent.buffer.ASCII85Codec;
import com.kreative.hexcellent.buffer.Base64Codec;
import com.kreative.hexcellent.buffer.ByteDecoder;
import com.kreative.hexcellent.buffer.ByteEncoder;
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
		add(new CutAsMenu(editor));
		add(new CopyAsMenu(editor));
		add(new PasteAsMenu(editor));
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
	
	public static class CutAsMenu extends JMenu {
		private static final long serialVersionUID = 1L;
		public CutAsMenu(JHexEditor editor) {
			super("Cut As");
			add(new CutAsMenuItem(editor));
			add(new CutAsMenuItem(editor, null));
			addSeparator();
			add(new CutAsMenuItem(editor, "UTF-8"));
			add(new CutAsMenuItem(editor, "UTF-16BE"));
			add(new CutAsMenuItem(editor, "UTF-16LE"));
			addSeparator();
			add(new CutAsMenuItem(editor, Base64Codec.BASE64, "base64 (padded)"));
			add(new CutAsMenuItem(editor, Base64Codec.BASE64_UNPADDED, "base64 (unpadded)"));
			add(new CutAsMenuItem(editor, Base64Codec.BASE64URL, "base64url"));
			add(new CutAsMenuItem(editor, Base64Codec.RFC3501, "RFC-3501"));
			addSeparator();
			add(new CutAsMenuItem(editor, ASCII85Codec.ASCII85, "ASCII85"));
			add(new CutAsMenuItem(editor, ASCII85Codec.ASCII85XML, "ASCII85XML"));
			add(new CutAsMenuItem(editor, ASCII85Codec.RFC1924, "RFC-1924"));
			add(new CutAsMenuItem(editor, ASCII85Codec.Z85, "Z85"));
		}
	}
	
	public static class CutAsMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public CutAsMenuItem(final JHexEditor editor) {
			super("Hex");
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.cutAsHex();
				}
			});
		}
		public CutAsMenuItem(final JHexEditor editor, final String charset) {
			super((charset == null) ? "Text" : charset);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (charset == null) editor.cutAsString();
					else editor.cutAsString(charset);
				}
			});
		}
		public CutAsMenuItem(final JHexEditor editor, final ByteEncoder encoder, final String name) {
			super(name);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.cutEncoded(encoder);
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
	
	public static class CopyAsMenu extends JMenu {
		private static final long serialVersionUID = 1L;
		public CopyAsMenu(JHexEditor editor) {
			super("Copy As");
			add(new CopyAsMenuItem(editor));
			add(new CopyAsMenuItem(editor, null));
			addSeparator();
			add(new CopyAsMenuItem(editor, "UTF-8"));
			add(new CopyAsMenuItem(editor, "UTF-16BE"));
			add(new CopyAsMenuItem(editor, "UTF-16LE"));
			addSeparator();
			add(new CopyAsMenuItem(editor, Base64Codec.BASE64, "base64 (padded)"));
			add(new CopyAsMenuItem(editor, Base64Codec.BASE64_UNPADDED, "base64 (unpadded)"));
			add(new CopyAsMenuItem(editor, Base64Codec.BASE64URL, "base64url"));
			add(new CopyAsMenuItem(editor, Base64Codec.RFC3501, "RFC-3501"));
			addSeparator();
			add(new CopyAsMenuItem(editor, ASCII85Codec.ASCII85, "ASCII85"));
			add(new CopyAsMenuItem(editor, ASCII85Codec.ASCII85XML, "ASCII85XML"));
			add(new CopyAsMenuItem(editor, ASCII85Codec.RFC1924, "RFC-1924"));
			add(new CopyAsMenuItem(editor, ASCII85Codec.Z85, "Z85"));
		}
	}
	
	public static class CopyAsMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public CopyAsMenuItem(final JHexEditor editor) {
			super("Hex");
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.copyAsHex();
				}
			});
		}
		public CopyAsMenuItem(final JHexEditor editor, final String charset) {
			super((charset == null) ? "Text" : charset);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (charset == null) editor.copyAsString();
					else editor.copyAsString(charset);
				}
			});
		}
		public CopyAsMenuItem(final JHexEditor editor, final ByteEncoder encoder, final String name) {
			super(name);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.copyEncoded(encoder);
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
	
	public static class PasteAsMenu extends JMenu {
		private static final long serialVersionUID = 1L;
		public PasteAsMenu(JHexEditor editor) {
			super("Paste As");
			add(new PasteAsMenuItem(editor));
			add(new PasteAsMenuItem(editor, null));
			addSeparator();
			add(new PasteAsMenuItem(editor, "UTF-8"));
			add(new PasteAsMenuItem(editor, "UTF-16BE"));
			add(new PasteAsMenuItem(editor, "UTF-16LE"));
			addSeparator();
			add(new PasteAsMenuItem(editor, Base64Codec.BASE64, "base64"));
			add(new PasteAsMenuItem(editor, Base64Codec.BASE64URL, "base64url"));
			add(new PasteAsMenuItem(editor, Base64Codec.RFC3501, "RFC-3501"));
			addSeparator();
			add(new PasteAsMenuItem(editor, ASCII85Codec.ASCII85, "ASCII85"));
			add(new PasteAsMenuItem(editor, ASCII85Codec.ASCII85XML, "ASCII85XML"));
			add(new PasteAsMenuItem(editor, ASCII85Codec.RFC1924, "RFC-1924"));
			add(new PasteAsMenuItem(editor, ASCII85Codec.Z85, "Z85"));
		}
	}
	
	public static class PasteAsMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public PasteAsMenuItem(final JHexEditor editor) {
			super("Hex");
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.pasteAsHex();
				}
			});
		}
		public PasteAsMenuItem(final JHexEditor editor, final String charset) {
			super((charset == null) ? "Text" : charset);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (charset == null) editor.pasteAsString();
					else editor.pasteAsString(charset);
				}
			});
		}
		public PasteAsMenuItem(final JHexEditor editor, final ByteDecoder decoder, final String name) {
			super(name);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.pasteDecoded(decoder);
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
