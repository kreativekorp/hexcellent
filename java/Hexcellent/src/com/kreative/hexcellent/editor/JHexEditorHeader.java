package com.kreative.hexcellent.editor;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.ByteBufferDocument;
import com.kreative.hexcellent.buffer.ByteBufferSelectionModel;

public class JHexEditorHeader extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private final JHexEditor parent;
	
	public JHexEditorHeader(JHexEditor parent) {
		this.parent = parent;
		this.parent.addHexEditorListener(listener);
	}
	
	private Dimension minimumSize = null;
	private Dimension preferredSize = null;
	
	@Override
	public void setMinimumSize(Dimension minimumSize) {
		this.minimumSize = minimumSize;
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		this.preferredSize = preferredSize;
	}
	
	@Override
	public Dimension getMinimumSize() {
		if (minimumSize != null) return minimumSize;
		Insets i = getInsets();
		FontMetrics fm = getFontMetrics(parent.getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int minimumWidth = cw * 28 + i.left + i.right; // TODO adjust
		int minimumHeight = ch + 5 + i.top + i.bottom;
		return new Dimension(minimumWidth, minimumHeight);
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (preferredSize != null) return preferredSize;
		Insets i = getInsets();
		FontMetrics fm = getFontMetrics(parent.getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int preferredWidth = cw * 76 + i.left + i.right; // TODO adjust
		int preferredHeight = ch + 5 + i.top + i.bottom;
		return new Dimension(preferredWidth, preferredHeight);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON
			);
		}
		
		Insets i = getInsets();
		int fw = getWidth(), w = fw - i.left - i.right;
		int fh = getHeight(), h = fh - i.top - i.bottom;
		
		g.setFont(parent.getFont());
		FontMetrics fm = g.getFontMetrics();
		int ca = fm.getAscent() + 1;
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		
		JHexEditorColors colors = parent.getColors();
		boolean extendBorders = parent.getExtendBorders();
		int ay = extendBorders ? 0 : i.top;
		int ah = extendBorders ? fh : h;
		g.setColor(colors.headerArea);
		if (extendBorders) g.fillRect(0, 0, fw, fh);
		else g.fillRect(i.left, i.top, w, h);
		
		boolean decimalAddresses = parent.getDecimalAddresses();
		long ss = parent.getSelectionMin();
		long se = parent.getSelectionMax();
		long sl = parent.getSelectionLength();
		long length = parent.length();
		int ty = i.top + ((h - 1) - ch) / 2 + ca;
		int tx = i.left + 3;
		g.setColor(colors.headerText);
		
		g.drawString("Sel:", tx, ty);
		
		String sss = addressString(ss, decimalAddresses);
		int sssx = tx + cw * 13 - fm.stringWidth(sss);
		g.drawString(sss, sssx, ty);
		
		g.drawString(":", tx + cw * 13, ty);
		
		String ses = addressString(se, decimalAddresses);
		int sesx = tx + cw * 22 - fm.stringWidth(ses);
		g.drawString(ses, sesx, ty);
		
		g.drawString("Len:", tx + cw * 24, ty);
		
		String sls = addressString(sl, decimalAddresses);
		int slsx = tx + cw * 37 - fm.stringWidth(sls);
		g.drawString(sls, slsx, ty);
		
		g.drawString("/", tx + cw * 37, ty);
		
		String ls = addressString(length, decimalAddresses);
		int lsx = tx + cw * 46 - fm.stringWidth(ls);
		g.drawString(ls, lsx, ty);
		
		g.drawString(parent.isTextActive() ? "TXT" : "HEX", tx + cw * 48, ty);
		g.drawString(parent.isReadOnly() ? "RO" : "RW", tx + cw * 53, ty);
		g.drawString(parent.getOvertype() ? "OVR" : "INS", tx + cw * 57, ty);
		g.drawString(parent.isLittleEndian() ? "LE" : "BE", tx + cw * 62, ty);
		g.drawString(parent.getCharset(), tx + cw * 66, ty);
		
		g.setColor(colors.headerDivider);
		if (extendBorders) g.fillRect(0, fh - 1, fw, 1);
		else g.fillRect(i.left, i.top + h - 1, w, 1);
		g.fillRect(tx + cw * 23, ay, 1, ah);
		g.fillRect(tx + cw * 47, ay, 1, ah);
		g.fillRect(tx + cw * 52, ay, 1, ah);
		g.fillRect(tx + cw * 56, ay, 1, ah);
		g.fillRect(tx + cw * 61, ay, 1, ah);
		g.fillRect(tx + cw * 65, ay, 1, ah);
	}
	
	private String addressString(long address, boolean decimalAddresses) {
		if (decimalAddresses) return Long.toString(address);
		String h = Long.toHexString(address).toUpperCase();
		if (h.length() < 8) h = ("00000000" + h).substring(h.length());
		return h;
	}
	
	private final JHexEditorListener listener = new JHexEditorListener() {
		@Override public void dataInserted(ByteBuffer buffer, long offset, int length) {}
		@Override public void dataOverwritten(ByteBuffer buffer, long offset, int length) {}
		@Override public void dataRemoved(ByteBuffer buffer, long offset, long length) {}
		@Override public void selectionChanged(ByteBufferSelectionModel sm, long start, long end) { repaint(); }
		@Override public void documentChanged(JHexEditor editor, ByteBufferDocument document) { repaint(); }
		@Override public void colorsChanged(JHexEditor editor, JHexEditorColors colors) { repaint(); }
		@Override public void editorStatusChanged(JHexEditor editor) { repaint(); }
	};
}
