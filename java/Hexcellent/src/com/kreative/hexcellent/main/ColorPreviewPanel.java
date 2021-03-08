package com.kreative.hexcellent.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import com.kreative.hexcellent.editor.JHexEditorColors;

public class ColorPreviewPanel extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private static final Font defaultFont = new Font("Monospaced", Font.PLAIN, 12);
	
	private JHexEditorColors colors;
	
	public ColorPreviewPanel() {
		this.colors = JHexEditorColors.AQUA;
		this.setFont(defaultFont);
	}
	
	public ColorPreviewPanel(JHexEditorColors colors) {
		this.colors = colors;
		this.setFont(defaultFont);
	}
	
	public JHexEditorColors getColors() {
		return this.colors;
	}
	
	public void setColors(JHexEditorColors colors) {
		this.colors = colors;
		repaint();
	}
	
	@Override
	public Dimension getMinimumSize() {
		Insets i = getInsets();
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int w = cw * 44 + i.left + i.right;
		int h = ch * 3 + 5 + i.top + i.bottom;
		return new Dimension(w, h);
	}
	
	@Override
	public Dimension getPreferredSize() {
		Insets i = getInsets();
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int w = cw * 44 + i.left + i.right;
		int h = ch * 3 + 5 + i.top + i.bottom;
		return new Dimension(w, h);
	}
	
	@Override
	public Dimension getMaximumSize() {
		Insets i = getInsets();
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int w = cw * 44 + i.left + i.right;
		int h = ch * 3 + 5 + i.top + i.bottom;
		return new Dimension(w, h);
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
		
		g.setFont(getFont());
		FontMetrics fm = g.getFontMetrics();
		int ca = fm.getAscent() + 1;
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int bpr = (w - 11 * cw) / (4 * cw);
		if (bpr < 1) return;
		if (bpr > 4) bpr = 4 * (bpr / 4);
		
		g.setColor(colors.headerArea);
		g.fillRect(i.left, i.top, w, ch + 5);
		
		int hty = i.top + 2 + ca;
		int htx = i.left + 3;
		g.setColor(colors.headerText);
		g.drawString(colors.name, htx, hty);
		
		g.setColor(colors.headerDivider);
		g.fillRect(i.left, i.top + ch + 4, w, 1);
		
		// Background
		int ay = i.top + (ch + 5);
		int ah = h - (ch + 5);
		// Address Area
		int aax = i.left;
		int aaw = cw * 9;
		g.setColor(colors.addressAreaEven);
		g.fillRect(aax, ay, aaw, ah);
		// Hex Area
		int hax = i.left + cw * 9;
		int haw = cw * (bpr * 3 + 1);
		g.setColor(colors.hexAreaEven);
		g.fillRect(hax, ay, haw, ah);
		// Text Area
		int tax = i.left + cw * (bpr * 3 + 10);
		int taw = fw - tax - i.right;
		g.setColor(colors.textAreaEven);
		g.fillRect(tax, ay, taw, ah);
		
		// Body
		int ss = bpr / 2;
		int se = ss + bpr;
		int length = bpr * 2;
		int offset = 0;
		boolean odd = false;
		int ry = i.top + (ch + 5);
		int ty = i.top + (ch + 5) + ca;
		while (offset < length) {
			// Background
			if (odd) {
				// Address Area
				g.setColor(colors.addressAreaOdd);
				g.fillRect(aax, ry, aaw, ch);
				// Hex Area
				g.setColor(colors.hexAreaOdd);
				g.fillRect(hax, ry, haw, ch);
				// Text Area
				g.setColor(colors.textAreaOdd);
				g.fillRect(tax, ry, taw, ch);
			}
			// Highlight
			if (ss != se && ss < offset + bpr && se > offset) {
				int s = Math.max(ss - offset, 0);
				int e = Math.min(se - offset, bpr);
				int hs = i.left + cw * (s * 3 + 9) + cw / 2;
				int he = i.left + cw * (e * 3 + 9) + cw / 2;
				int ds = i.left + cw * (bpr * 3 + 11 + s);
				int de = i.left + cw * (bpr * 3 + 11 + e);
				g.setColor(hexHighlightColor(odd));
				g.fillRect(hs, ry, he - hs, ch);
				g.setColor(textHighlightColor(odd));
				g.fillRect(ds, ry, de - ds, ch);
			}
			// Address
			String addressString = addressString(offset);
			int asx = i.left + cw * 9 - fm.stringWidth(addressString);
			g.setColor(odd ? colors.addressTextOdd : colors.addressTextEven);
			g.drawString(addressString, asx, ty);
			// Data
			int idx = 0;
			int off = offset;
			int hx = i.left + cw * 10;
			int dx = i.left + cw * (bpr * 3 + 11);
			while (idx < bpr && off < length) {
				// Hex
				boolean sel = (off >= ss && off < se);
				g.setColor(hexTextColor(sel, odd));
				g.drawString(DUMMY_HEX[off & 7], hx, ty);
				// Text
				char[] chars = DUMMY_DATA[off & 7].toCharArray();
				g.setColor(textTextColor(sel, odd, !shiftControl(chars)));
				g.drawChars(chars, 0, chars.length, dx, ty);
				idx++;
				off++;
				hx += cw * 3;
				dx += cw;
			}
			offset += bpr;
			odd = !odd;
			ry += ch;
			ty += ch;
		}
		
		// Address Divider
		g.setColor(colors.addressDivider);
		g.fillRect(hax, ay, 1, ah);
		// Hex Dividers
		g.setColor(colors.hexDivider);
		for (int j = 4; j < bpr; j += 4) {
			g.fillRect(i.left + cw * (j * 3 + 9) + cw / 2, ay, 1, ah);
		}
		// Text Divider
		g.setColor(colors.textDivider);
		g.fillRect(tax, ay, 1, ah);
	}
	
	private Color hexHighlightColor(boolean odd) {
		return odd ? colors.hexAreaActiveHighlightOdd : colors.hexAreaActiveHighlightEven;
	}
	
	private Color hexTextColor(boolean sel, boolean odd) {
		if (sel) return odd ? colors.hexTextActiveHighlightOdd : colors.hexTextActiveHighlightEven;
		return odd ? colors.hexTextOdd : colors.hexTextEven;
	}
	
	private Color textHighlightColor(boolean odd) {
		return odd ? colors.textAreaInactiveHighlightOdd : colors.textAreaInactiveHighlightEven;
	}
	
	private Color textTextColor(boolean sel, boolean odd, boolean printable) {
		if (printable) {
			if (sel) return odd ? colors.textPrintableInactiveHighlightOdd : colors.textPrintableInactiveHighlightEven;
			return odd ? colors.textPrintableOdd : colors.textPrintableEven;
		} else {
			if (sel) return odd ? colors.textUnprintableInactiveHighlightOdd : colors.textUnprintableInactiveHighlightEven;
			return odd ? colors.textUnprintableOdd : colors.textUnprintableEven;
		}
	}
	
	private String addressString(long address) {
		String h = Long.toHexString(address).toUpperCase();
		if (h.length() < 8) h = ("00000000" + h).substring(h.length());
		return h + ":";
	}
	
	private boolean shiftControl(char[] chars) {
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] >= 0x00 && chars[i] < 0x20) { chars[i] += 0x40; return true; }
			if (chars[i] == 0x7F) { chars[i] = '?'; return true; }
			if (chars[i] >= 0x80 && chars[i] < 0x9F) { chars[i] -= 0x20; return true; }
			if (chars[i] == 0x9F) { chars[i] = '#'; return true; }
		}
		return false;
	}
	
	private static final String[] DUMMY_HEX = {
		"01", "23", "45", "67",
		"89", "AB", "CD", "EF",
	};
	
	private static final String[] DUMMY_DATA = {
		"\u0001", "\u0023", "\u0045", "\u0067",
		"\u0089", "\u00AB", "\u00CD", "\u00EF",
	};
}
