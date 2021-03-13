package com.kreative.hexcellent.main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.ByteBufferDocument;
import com.kreative.hexcellent.buffer.ByteBufferSelectionModel;
import com.kreative.hexcellent.editor.JHexEditor;
import com.kreative.hexcellent.editor.JHexEditorColors;
import com.kreative.hexcellent.editor.JHexEditorListener;

public class OptionsMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	private final DecimalAddressesMenuItem decimalAddresses;
	private final OvertypeModeMenuItem overtypeMode;
	private final PowerKeysMenuItem powerKeys;
	private final LittleEndianMenuItem littleEndian;
	
	public OptionsMenu(final EditorFrame f, final JHexEditor editor) {
		super("Options");
		add(new FontMenuItem(f, editor));
		add(new CharsetMenuItem(f, editor));
		add(new ColorsMenuItem(f, editor));
		addSeparator();
		add(decimalAddresses = new DecimalAddressesMenuItem(editor));
		add(overtypeMode = new OvertypeModeMenuItem(editor));
		add(powerKeys = new PowerKeysMenuItem(editor));
		add(littleEndian = new LittleEndianMenuItem(editor));
		addSeparator();
		add(new RevertToDefaultMenuItem(f));
		add(new SetAsDefaultMenuItem(f));
		
		editor.addHexEditorListener(new JHexEditorListener() {
			@Override
			public void editorStatusChanged(JHexEditor editor) {
				decimalAddresses.setSelected(editor.getDecimalAddresses());
				overtypeMode.setSelected(editor.getOvertype());
				powerKeys.setSelected(editor.getEnableTransformKeys());
				littleEndian.setSelected(editor.isLittleEndian());
			}
			@Override public void dataInserted(ByteBuffer buffer, long offset, int length) {}
			@Override public void dataOverwritten(ByteBuffer buffer, long offset, int length) {}
			@Override public void dataRemoved(ByteBuffer buffer, long offset, long length) {}
			@Override public void selectionChanged(ByteBufferSelectionModel sm, long start, long end) {}
			@Override public void documentChanged(JHexEditor editor, ByteBufferDocument document) {}
			@Override public void colorsChanged(JHexEditor editor, JHexEditorColors colors) {}
		});
	}
	
	private static class FontMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public FontMenuItem(final EditorFrame f, final JHexEditor editor) {
			super("Font...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, SwingUtils.SHORTCUT_KEY));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Font font = new FontDialog(f, editor.getFont()).showDialog();
					if (font != null) {
						editor.setFont(font);
						f.pack();
					}
				}
			});
		}
	}
	
	private static class CharsetMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public CharsetMenuItem(final EditorFrame f, final JHexEditor editor) {
			super("Encoding...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String cs = new CharsetDialog(f, editor.getCharset()).showDialog();
					if (cs != null) editor.setCharset(cs);
				}
			});
		}
	}
	
	private static class ColorsMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public ColorsMenuItem(final EditorFrame f, final JHexEditor editor) {
			super("Color Scheme...");
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, SwingUtils.SHORTCUT_KEY | KeyEvent.SHIFT_MASK));
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JHexEditorColors c = new ColorSchemeDialog(f, editor.getColors()).showDialog();
					if (c != null) editor.setColors(c);
				}
			});
		}
	}
	
	private static class DecimalAddressesMenuItem extends JCheckBoxMenuItem {
		private static final long serialVersionUID = 1L;
		public DecimalAddressesMenuItem(final JHexEditor editor) {
			super("Decimal Addresses");
			setSelected(editor.getDecimalAddresses());
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.setDecimalAddresses(isSelected());
				}
			});
		}
	}
	
	private static class OvertypeModeMenuItem extends JCheckBoxMenuItem {
		private static final long serialVersionUID = 1L;
		public OvertypeModeMenuItem(final JHexEditor editor) {
			super("Overtype Mode");
			setSelected(editor.getOvertype());
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.setOvertype(isSelected());
				}
			});
		}
	}
	
	private static class PowerKeysMenuItem extends JCheckBoxMenuItem {
		private static final long serialVersionUID = 1L;
		public PowerKeysMenuItem(final JHexEditor editor) {
			super("Power Keys");
			setSelected(editor.getEnableTransformKeys());
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.setEnableTransformKeys(isSelected());
				}
			});
		}
	}
	
	private static class LittleEndianMenuItem extends JCheckBoxMenuItem {
		private static final long serialVersionUID = 1L;
		public LittleEndianMenuItem(final JHexEditor editor) {
			super("Little Endian");
			setSelected(editor.isLittleEndian());
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editor.setLittleEndian(isSelected());
				}
			});
		}
	}
	
	private static class RevertToDefaultMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public RevertToDefaultMenuItem(final EditorFrame f) {
			super("Revert To Default");
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Main.revertOptions(f);
				}
			});
		}
	}
	
	private static class SetAsDefaultMenuItem extends JMenuItem {
		private static final long serialVersionUID = 1L;
		public SetAsDefaultMenuItem(final EditorFrame f) {
			super("Set As Default");
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Main.saveOptions(f);
				}
			});
		}
	}
}
