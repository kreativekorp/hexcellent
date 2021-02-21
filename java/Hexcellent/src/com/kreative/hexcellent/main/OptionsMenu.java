package com.kreative.hexcellent.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
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
	
	public OptionsMenu(final JHexEditor editor) {
		super("Options");
		add(decimalAddresses = new DecimalAddressesMenuItem(editor));
		add(overtypeMode = new OvertypeModeMenuItem(editor));
		add(powerKeys = new PowerKeysMenuItem(editor));
		add(littleEndian = new LittleEndianMenuItem(editor));
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
}
