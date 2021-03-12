package com.kreative.hexcellent.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import com.kreative.hexcellent.buffer.ArrayByteBuffer;
import com.kreative.hexcellent.buffer.BitTransform;
import com.kreative.hexcellent.buffer.ByteBuffer;
import com.kreative.hexcellent.buffer.ByteBufferDocument;
import com.kreative.hexcellent.buffer.ByteBufferHistory;
import com.kreative.hexcellent.buffer.ByteBufferListener;
import com.kreative.hexcellent.buffer.ByteBufferSelectionListener;
import com.kreative.hexcellent.buffer.ByteBufferSelectionModel;
import com.kreative.hexcellent.buffer.ByteTransform;
import com.kreative.hexcellent.buffer.CompositeByteBuffer;
import com.kreative.hexcellent.buffer.IncrementTransform;
import com.kreative.hexcellent.buffer.RandomTransform;
import com.kreative.hexcellent.buffer.ReverseTransform;
import com.kreative.hexcellent.buffer.RotateTransform;

public class JHexEditor extends JComponent implements Scrollable {
	private static final long serialVersionUID = 1L;
	
	private static final Font defaultFont = new Font("Monospaced", Font.PLAIN, 12);
	
	private ByteBufferDocument document;
	private JHexEditorColors colors = JHexEditorColors.AQUA;
	private boolean extendBorders = false;
	private boolean decimalAddresses = false;
	private String charset = "ISO-8859-1";
	private final boolean[] charsetPrintable = new boolean[256];
	private final String[] charsetStrings = new String[256];
	private boolean textActive = false;
	private boolean readOnly = false;
	private boolean overtype = false;
	private boolean enableShortcutKeys = true;
	private boolean enableTransformKeys = true;
	private boolean littleEndian = false;
	private final List<JHexEditorListener> listeners = new ArrayList<JHexEditorListener>();
	
	public JHexEditor() {
		this(new ByteBufferDocument(new CompositeByteBuffer()));
	}
	
	public JHexEditor(byte[] data) {
		this(new ByteBufferDocument(new CompositeByteBuffer(new ArrayByteBuffer(data))));
	}
	
	public JHexEditor(ByteBuffer buffer) {
		this(new ByteBufferDocument(new CompositeByteBuffer(buffer)));
	}
	
	public JHexEditor(ByteBufferDocument document) {
		this.document = document;
		this.document.addByteBufferListener(bufferListener);
		this.document.addSelectionListener(selectionListener);
		this.makeCharset();
		this.setFont(defaultFont);
		this.setFocusable(true);
		this.setRequestFocusEnabled(true);
		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mouseListener);
		this.addKeyListener(keyListener);
	}
	
	// PROPERTY GETTERS AND SETTERS
	
	public ByteBufferDocument getDocument() {
		return this.document;
	}
	
	public void setDocument(ByteBufferDocument document) {
		this.document.removeByteBufferListener(bufferListener);
		this.document.removeSelectionListener(selectionListener);
		this.document = document;
		this.document.addByteBufferListener(bufferListener);
		this.document.addSelectionListener(selectionListener);
		for (JHexEditorListener l : listeners) l.documentChanged(this, document);
		revalidate();
		repaint();
	}
	
	public JHexEditorColors getColors() {
		return this.colors;
	}
	
	public void setColors(JHexEditorColors colors) {
		this.colors = colors;
		for (JHexEditorListener l : listeners) l.colorsChanged(this, colors);
		repaint();
	}
	
	public boolean getExtendBorders() {
		return this.extendBorders;
	}
	
	public void setExtendBorders(boolean extendBorders) {
		this.extendBorders = extendBorders;
		for (JHexEditorListener l : listeners) l.editorStatusChanged(this);
		repaint();
	}
	
	public boolean getDecimalAddresses() {
		return this.decimalAddresses;
	}
	
	public void setDecimalAddresses(boolean decimalAddresses) {
		this.decimalAddresses = decimalAddresses;
		for (JHexEditorListener l : listeners) l.editorStatusChanged(this);
		repaint();
	}
	
	public String getCharset() {
		return this.charset;
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
		this.makeCharset();
		for (JHexEditorListener l : listeners) l.editorStatusChanged(this);
		repaint();
	}
	
	public boolean isTextActive() {
		return this.textActive;
	}
	
	public void setTextActive(boolean textActive) {
		this.textActive = textActive;
		for (JHexEditorListener l : listeners) l.editorStatusChanged(this);
		repaint();
	}
	
	public boolean isReadOnly() {
		return this.readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		for (JHexEditorListener l : listeners) l.editorStatusChanged(this);
	}
	
	public boolean getOvertype() {
		return this.overtype;
	}
	
	public void setOvertype(boolean overtype) {
		this.overtype = overtype;
		for (JHexEditorListener l : listeners) l.editorStatusChanged(this);
	}
	
	public boolean getEnableShortcutKeys() {
		return this.enableShortcutKeys;
	}
	
	public void setEnableShortcutKeys(boolean enableShortcutKeys) {
		this.enableShortcutKeys = enableShortcutKeys;
		for (JHexEditorListener l : listeners) l.editorStatusChanged(this);
	}
	
	public boolean getEnableTransformKeys() {
		return this.enableTransformKeys;
	}
	
	public void setEnableTransformKeys(boolean enableTransformKeys) {
		this.enableTransformKeys = enableTransformKeys;
		for (JHexEditorListener l : listeners) l.editorStatusChanged(this);
	}
	
	public boolean isLittleEndian() {
		return this.littleEndian;
	}
	
	public void setLittleEndian(boolean littleEndian) {
		this.littleEndian = littleEndian;
		for (JHexEditorListener l : listeners) l.editorStatusChanged(this);
	}
	
	public void addHexEditorListener(JHexEditorListener listener) {
		listeners.add(listener);
	}
	
	public void removeHexEditorListener(JHexEditorListener listener) {
		listeners.remove(listener);
	}
	
	// CONVENIENCE METHODS
	
	public ByteBuffer getByteBuffer() { return document.getByteBuffer(); }
	public boolean isEmpty() { return document.isEmpty(); }
	public long length() { return document.length(); }
	public boolean get(long offset, byte[] dst, int dstOffset, int length) { return document.get(offset, dst, dstOffset, length); }
	public ByteBuffer slice(long offset, long length) { return document.slice(offset, length); }
	public boolean write(OutputStream out, long offset, long length) throws IOException { return document.write(out, offset, length); }
	public long indexOf(byte[] pattern) { return document.indexOf(pattern); }
	public long indexOf(byte[] pattern, long index) { return document.indexOf(pattern, index); }
	public long lastIndexOf(byte[] pattern) { return document.lastIndexOf(pattern); }
	public long lastIndexOf(byte[] pattern, long index) { return document.lastIndexOf(pattern, index); }
	
	public ByteBufferSelectionModel getSelectionModel() { return document.getSelectionModel(); }
	public long getSelectionStart() { return document.getSelectionStart(); }
	public long getSelectionEnd() { return document.getSelectionEnd(); }
	public long getSelectionMin() { return document.getSelectionMin(); }
	public long getSelectionMax() { return document.getSelectionMax(); }
	public long getSelectionLength() { return document.getSelectionLength(); }
	public void setSelectionStart(long start) { document.setSelectionStart(start); }
	public void setSelectionEnd(long end) { document.setSelectionEnd(end); }
	public void setSelectionRange(long start, long end) { document.setSelectionRange(start, end); }
	public void selectAll() { document.selectAll(); }
	
	public ByteBufferHistory getHistory() { return document.getHistory(); }
	public boolean canUndo() { return !readOnly && document.canUndo(); }
	public boolean canRedo() { return !readOnly && document.canRedo(); }
	public String getUndoActionName() { return readOnly ? null : document.getUndoActionName(); }
	public String getRedoActionName() { return readOnly ? null : document.getRedoActionName(); }
	public void undo() { if (!readOnly) document.undo(); }
	public void redo() { if (!readOnly) document.redo(); }
	public void clearHistory() { document.clearHistory(); }
	
	public byte[] getSelection() { return document.getSelection(); }
	public String getSelectionAsHex() { return document.getSelectionAsHex(); }
	public String getSelectionAsString() { return document.getSelectionAsString(charset); }
	public boolean deleteSelection(String actionName) { return !readOnly && document.deleteSelection(actionName); }
	public boolean replaceSelection(String actionName, byte[] data, boolean keepSelected) { return !readOnly && document.replaceSelection(actionName, data, keepSelected); }
	public boolean transformSelection(ByteTransform tx) { return !readOnly && document.transformSelection(tx); }
	public boolean replaceAll(byte[] pattern, byte[] replacement) { return !readOnly && document.replaceAll(pattern, replacement); }
	
	// EDIT COMMANDS
	
	public boolean cut() { return textActive ? cutAsString() : cutAsHex(); }
	public boolean cutAsHex() { return !readOnly && document.cutAsHex(); }
	public boolean cutAsString() { return !readOnly && document.cutAsString(charset); }
	public boolean copy() { return textActive ? copyAsString() : copyAsHex(); }
	public boolean copyAsHex() { return document.copyAsHex(); }
	public boolean copyAsString() { return document.copyAsString(charset); }
	public boolean paste() { return textActive ? pasteAsString() : pasteAsHex(); }
	public boolean pasteAsHex() { return !readOnly && document.pasteAsHex(); }
	public boolean pasteAsString() { return !readOnly && document.pasteAsString(charset); }
	
	public String getSelectionRange() {
		long ss = document.getSelectionStart();
		long se = document.getSelectionEnd();
		String sss = decimalAddresses ? Long.toString(ss) : Long.toHexString(ss);
		String ses = decimalAddresses ? Long.toString(se) : Long.toHexString(se);
		return (ss == se) ? sss : (sss + ":" + ses);
	}
	
	public void setSelectionRange(String range) {
		try {
			String[] r = range.split(":", 2);
			String sss = r[0].trim();
			String ses = (r.length > 1) ? r[1].trim() : sss;
			long ss, se;
			
			if (sss.startsWith("0x") || sss.startsWith("0X")) ss = Long.parseLong(sss.substring(2), 16);
			else if (sss.startsWith("$")) ss = Long.parseLong(sss.substring(1), 16);
			else if (sss.startsWith("#")) ss = Long.parseLong(sss.substring(1), 10);
			else ss = Long.parseLong(sss, decimalAddresses ? 10 : 16);
			
			if (ses.startsWith("0x") || ses.startsWith("0X")) se = Long.parseLong(ses.substring(2), 16);
			else if (ses.startsWith("$")) se = Long.parseLong(ses.substring(1), 16);
			else if (ses.startsWith("#")) se = Long.parseLong(ses.substring(1), 10);
			else se = Long.parseLong(ses, decimalAddresses ? 10 : 16);
			
			document.setSelectionRange(ss, se);
		} catch (Exception e) {}
	}
	
	public void showSetSelectionDialog(String title) {
		Object o = JOptionPane.showInputDialog(
			this, "Enter address range:", title,
			JOptionPane.QUESTION_MESSAGE, null, null,
			getSelectionRange()
		);
		if (o != null) {
			setSelectionRange(o.toString());
		}
	}
	
	// POINT/OFFSET TRANSLATION
	
	public static final class PointInfo {
		public final boolean inAddressArea;
		public final boolean inHexArea;
		public final boolean inTextArea;
		public final long offset;
		public final long length;
		public final int rowWidth;
		private PointInfo(boolean aa, boolean ha, boolean ta, long offset, long length, int bpr) {
			this.inAddressArea = aa;
			this.inHexArea = ha;
			this.inTextArea = ta;
			this.offset = offset;
			this.length = length;
			this.rowWidth = bpr;
		}
	}
	
	public PointInfo getInfoAtPoint(int x, int y) {
		Insets i = getInsets();
		int w = getWidth() - i.left - i.right;
		
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int bpr = (w - 11 * cw) / (4 * cw);
		if (bpr < 1) return null;
		if (bpr > 4) bpr = 4 * (bpr / 4);
		
		int hax = i.left + cw * 9;
		int tax = i.left + cw * (bpr * 3 + 10);
		
		long length = document.length();
		long offset = (((y - i.top) / ch) * bpr);
		if (offset < 0) offset = 0;
		if (offset > length) offset = length;
		if (x < hax) return new PointInfo(true, false, false, offset, length, bpr);
		
		if (y >= i.top) {
			int off = (
				(x < tax)
				? ((x - hax + cw) / (cw * 3))
				: ((x - tax - cw / 2) / cw)
			);
			if (off < 0) off = 0;
			if (off > bpr) off = bpr;
			if ((offset += off) > length) offset = length;
		}
		return new PointInfo(false, x < tax, x >= tax, offset, length, bpr);
	}
	
	public Rectangle getRectForOffset(long offset) {
		Insets i = getInsets();
		int w = getWidth() - i.left - i.right;
		
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int bpr = (w - 11 * cw) / (4 * cw);
		if (bpr < 1) return null;
		if (bpr > 4) bpr = 4 * (bpr / 4);
		
		int hax = i.left + cw * 9;
		int tax = i.left + cw * (bpr * 3 + 10);
		
		int y = (int)Math.min(((offset / bpr) * ch) + i.top, Integer.MAX_VALUE);
		int x = (int)((offset % bpr) * cw);
		if (textActive) return new Rectangle(tax + cw + x, y, 2, ch);
		else return new Rectangle(hax + cw / 2 + x * 3, y, 2, ch);
	}
	
	public Rectangle getRectForOffsetLength(long offset, long length) {
		Insets i = getInsets();
		int w = getWidth() - i.left - i.right;
		
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int bpr = (w - 11 * cw) / (4 * cw);
		if (bpr < 1) return null;
		if (bpr > 4) bpr = 4 * (bpr / 4);
		
		int hax = i.left + cw * 9;
		int tax = i.left + cw * (bpr * 3 + 10);
		
		int y1 = (int)Math.min(((offset / bpr) * ch) + i.top, Integer.MAX_VALUE);
		int x1 = (int)((offset % bpr) * cw);
		offset += length;
		int y2 = (int)Math.min(((offset / bpr) * ch) + i.top, Integer.MAX_VALUE);
		int x2 = (int)((offset % bpr) * cw);
		
		int y = Math.min(y1, y2);
		int x = Math.min(x1, x2);
		int bh = Math.max(y1, y2) - y;
		int bw = Math.max(x1, x2) - x;
		if (textActive) return new Rectangle(tax + cw + x, y, bw + 2, bh + ch);
		else return new Rectangle(hax + cw / 2 + x * 3, y, bw + 2, bh + ch);
	}
	
	// SIZING AND SCROLLING
	
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
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int minimumWidth = cw * 28 + i.left + i.right;
		int minimumHeight = ch + i.top + i.bottom;
		return new Dimension(minimumWidth, minimumHeight);
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (preferredSize != null) return preferredSize;
		Insets i = getInsets();
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int minimumHeight = ch + i.top + i.bottom;
		int preferredWidth, bpr;
		
		Container parent = getParent();
		if (parent instanceof JViewport) {
			int h = parent.getHeight();
			if (h > minimumHeight) minimumHeight = h;
			preferredWidth = parent.getWidth();
			int w = preferredWidth - i.left - i.right;
			bpr = (w - 11 * cw) / (4 * cw);
			if (bpr < 1) bpr = 1;
			if (bpr > 4) bpr = 4 * (bpr / 4);
		} else {
			preferredWidth = cw * 76 + i.left + i.right;
			bpr = 16;
		}
		
		long length = document.length();
		long preferredHeight = ch * ((length + bpr - 1) / bpr) + i.top + i.bottom;
		if (preferredHeight < minimumHeight) preferredHeight = minimumHeight;
		if (preferredHeight > Integer.MAX_VALUE) preferredHeight = Integer.MAX_VALUE;
		return new Dimension(preferredWidth, (int)preferredHeight);
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		Insets i = getInsets();
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int preferredWidth = cw * 76 + i.left + i.right;
		int preferredHeight = ch * 16 + i.top + i.bottom;
		return new Dimension(preferredWidth, preferredHeight);
	}
	
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle vr, int or, int dir) {
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		return ch;
	}
	
	@Override
	public int getScrollableBlockIncrement(Rectangle vr, int or, int dir) {
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		return ch * (vr.height / ch);
	}
	
	// RENDERING
	
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
		Rectangle vr = getVisibleRect();
		
		g.setFont(getFont());
		FontMetrics fm = g.getFontMetrics();
		int ca = fm.getAscent() + 1;
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int bpr = (w - 11 * cw) / (4 * cw);
		if (bpr < 1) return;
		if (bpr > 4) bpr = 4 * (bpr / 4);
		byte[] data = new byte[bpr];
		
		// Background
		int ay = extendBorders ? 0 : i.top;
		int ah = extendBorders ? fh : h;
		// Address Area
		int aax = extendBorders ? 0 : i.left;
		int aaw = extendBorders ? (cw * 9 + i.left) : (cw * 9);
		g.setColor(colors.addressAreaEven);
		g.fillRect(aax, ay, aaw, ah);
		// Hex Area
		int hax = i.left + cw * 9;
		int haw = cw * (bpr * 3 + 1);
		g.setColor(colors.hexAreaEven);
		g.fillRect(hax, ay, haw, ah);
		// Text Area
		int tax = i.left + cw * (bpr * 3 + 10);
		int taw = extendBorders ? (fw - tax) : (fw - tax - i.right);
		g.setColor(colors.textAreaEven);
		g.fillRect(tax, ay, taw, ah);
		
		// Body
		long ss = document.getSelectionMin();
		long se = document.getSelectionMax();
		long length = document.length();
		long offset = 0;
		boolean odd = false;
		int ry = i.top;
		int ty = i.top + ca;
		int miny = vr.y - ch;
		int maxy = vr.y + vr.height;
		while (offset < length) {
			if (ry < 0 || ry >= maxy) break;
			if (ry >= miny) {
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
					int s = (int)Math.max(ss - offset, 0);
					int e = (int)Math.min(se - offset, bpr);
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
				document.get(offset, data, 0, (int)Math.min(bpr, length - offset));
				int idx = 0;
				long off = offset;
				int hx = i.left + cw * 10;
				int dx = i.left + cw * (bpr * 3 + 11);
				while (idx < bpr && off < length) {
					boolean sel = (off >= ss && off < se);
					int b = data[idx] & 0xFF;
					g.setColor(hexTextColor(sel, odd));
					g.drawString(HEX[b], hx, ty);
					g.setColor(textTextColor(sel, odd, charsetPrintable[b]));
					g.drawString(charsetStrings[b], dx, ty);
					idx++;
					off++;
					hx += cw * 3;
					dx += cw;
				}
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
		
		// Cursor
		if (ss == se) {
			offset = 0;
			ry = i.top;
			while (offset < length) {
				if (ss == offset) {
					int hs = i.left + cw * 9 + cw * 3 / 4;
					int ds = i.left + cw * (bpr * 3 + 11);
					g.setColor(hexCursorColor());
					g.fillRect(hs, ry, 2, ch);
					g.setColor(textCursorColor());
					g.fillRect(ds, ry, 2, ch);
				} else if (ss == offset + bpr) {
					int hs = i.left + cw * (bpr * 3 + 9) + cw / 4;
					int ds = i.left + cw * (bpr * 4 + 11);
					g.setColor(hexCursorColor());
					g.fillRect(hs, ry, 2, ch);
					g.setColor(textCursorColor());
					g.fillRect(ds, ry, 2, ch);
				} else if (ss > offset && ss < offset + bpr) {
					int s = (int)(ss - offset);
					int hs = i.left + cw * (s * 3 + 9) + cw / 2;
					int ds = i.left + cw * (bpr * 3 + 11 + s);
					g.setColor(hexCursorColor());
					g.fillRect(hs, ry, 2, ch);
					g.setColor(textCursorColor());
					g.fillRect(ds, ry, 2, ch);
				}
				offset += bpr;
				ry += ch;
			}
			if (ss == offset) {
				int hs = i.left + cw * 9 + cw * 3 / 4;
				int ds = i.left + cw * (bpr * 3 + 11);
				g.setColor(hexCursorColor());
				g.fillRect(hs, ry, 2, ch);
				g.setColor(textCursorColor());
				g.fillRect(ds, ry, 2, ch);
			}
		}
	}
	
	private Color hexCursorColor() {
		if (!textActive) return document.isMidByte() ? colors.activeCursorMidByte : colors.activeCursor;
		return document.isMidByte() ? colors.inactiveCursorMidByte : colors.inactiveCursor;
	}
	
	private Color hexHighlightColor(boolean odd) {
		if (!textActive) return odd ? colors.hexAreaActiveHighlightOdd : colors.hexAreaActiveHighlightEven;
		return odd ? colors.hexAreaInactiveHighlightOdd : colors.hexAreaInactiveHighlightEven;
	}
	
	private Color hexTextColor(boolean sel, boolean odd) {
		if (sel && !textActive) return odd ? colors.hexTextActiveHighlightOdd : colors.hexTextActiveHighlightEven;
		if (sel) return odd ? colors.hexTextInactiveHighlightOdd : colors.hexTextInactiveHighlightEven;
		return odd ? colors.hexTextOdd : colors.hexTextEven;
	}
	
	private Color textCursorColor() {
		if (textActive) return document.isMidByte() ? colors.activeCursorMidByte : colors.activeCursor;
		return document.isMidByte() ? colors.inactiveCursorMidByte : colors.inactiveCursor;
	}
	
	private Color textHighlightColor(boolean odd) {
		if (textActive) return odd ? colors.textAreaActiveHighlightOdd : colors.textAreaActiveHighlightEven;
		return odd ? colors.textAreaInactiveHighlightOdd : colors.textAreaInactiveHighlightEven;
	}
	
	private Color textTextColor(boolean sel, boolean odd, boolean printable) {
		if (printable) {
			if (sel && textActive) return odd ? colors.textPrintableActiveHighlightOdd : colors.textPrintableActiveHighlightEven;
			if (sel) return odd ? colors.textPrintableInactiveHighlightOdd : colors.textPrintableInactiveHighlightEven;
			return odd ? colors.textPrintableOdd : colors.textPrintableEven;
		} else {
			if (sel && textActive) return odd ? colors.textUnprintableActiveHighlightOdd : colors.textUnprintableActiveHighlightEven;
			if (sel) return odd ? colors.textUnprintableInactiveHighlightOdd : colors.textUnprintableInactiveHighlightEven;
			return odd ? colors.textUnprintableOdd : colors.textUnprintableEven;
		}
	}
	
	private String addressString(long address) {
		if (decimalAddresses) return address + ":";
		String h = Long.toHexString(address).toUpperCase();
		if (h.length() < 8) h = ("00000000" + h).substring(h.length());
		return h + ":";
	}
	
	private void makeCharset() {
		byte[] tmp = new byte[1];
		for (int i = 0; i < 256; i++) {
			tmp[0] = (byte)i;
			String dataString;
			try { dataString = new String(tmp, charset); }
			catch (Exception e) { dataString = "\uFFFD"; }
			if (dataString.contains("\uFFFD")) {
				charsetPrintable[i] = false;
				charsetStrings[i] = ".";
			} else {
				char[] chars = dataString.toCharArray();
				charsetPrintable[i] = !shiftControl(chars);
				charsetStrings[i] = new String(chars);
			}
		}
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
	
	// EVENT HANDLING
	
	private final ByteBufferListener bufferListener = new ByteBufferListener() {
		@Override
		public void dataInserted(ByteBuffer buffer, long offset, int length) {
			for (JHexEditorListener l : listeners) l.dataInserted(buffer, offset, length);
			revalidate();
			repaint();
		}
		@Override
		public void dataOverwritten(ByteBuffer buffer, long offset, int length) {
			for (JHexEditorListener l : listeners) l.dataOverwritten(buffer, offset, length);
			repaint();
		}
		@Override
		public void dataRemoved(ByteBuffer buffer, long offset, long length) {
			for (JHexEditorListener l : listeners) l.dataRemoved(buffer, offset, length);
			revalidate();
			repaint();
		}
	};
	
	private final ByteBufferSelectionListener selectionListener = new ByteBufferSelectionListener() {
		@Override
		public void selectionChanged(ByteBufferSelectionModel sm, long start, long end) {
			scrollRectToVisible(getRectForOffset(document.getSelectionEnd()));
			for (JHexEditorListener l : listeners) l.selectionChanged(sm, start, end);
			repaint();
		}
	};
	
	private final MouseAdapter mouseListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			requestFocusInWindow();
			PointInfo p = getInfoAtPoint(e.getX(), e.getY());
			if (p != null && (p.inHexArea || p.inTextArea)) {
				if (e.isShiftDown()) document.setSelectionEnd(p.offset);
				else document.setSelectionRange(p.offset, p.offset);
				setTextActive(p.inTextArea);
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			PointInfo p = getInfoAtPoint(e.getX(), e.getY());
			if (p != null && (p.inHexArea || p.inTextArea)) {
				document.setSelectionEnd(p.offset);
				setTextActive(p.inTextArea);
			}
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			Insets i = getInsets();
			FontMetrics fm = getFontMetrics(getFont());
			int cw = fm.stringWidth("0123456789ABCDEF") / 16;
			int hax = i.left + cw * 9;
			int end = getWidth() - i.right;
			if (e.getX() < hax || e.getX() > end) setCursor(null);
			else setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		}
	};
	
	private final KeyAdapter keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.isMetaDown() || e.isControlDown()) {
				if (!enableShortcutKeys) return;
				switch (e.getKeyCode()) {
					case KeyEvent.VK_C:
						if (e.isShiftDown()) copyAsString();
						else if (e.isAltDown()) copyAsHex();
						else copy();
						e.consume();
						return;
					case KeyEvent.VK_A:
						selectAll();
						e.consume();
						return;
					case KeyEvent.VK_J:
						showSetSelectionDialog("Go To Address");
						e.consume();
						return;
				}
				if (readOnly) return;
				switch (e.getKeyCode()) {
					case KeyEvent.VK_Z:
						if (e.isShiftDown()) redo();
						else undo();
						e.consume();
						return;
					case KeyEvent.VK_Y:
						if (e.isShiftDown()) undo();
						else redo();
						e.consume();
						return;
					case KeyEvent.VK_X:
						if (e.isShiftDown()) cutAsString();
						else if (e.isAltDown()) cutAsHex();
						else cut();
						e.consume();
						return;
					case KeyEvent.VK_V:
						if (e.isShiftDown()) pasteAsString();
						else if (e.isAltDown()) pasteAsHex();
						else paste();
						e.consume();
						return;
				}
				return;
			}
			switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
				case KeyEvent.VK_ESCAPE:
					setTextActive(!textActive);
					e.consume();
					return;
				case KeyEvent.VK_INSERT:
					setOvertype(!overtype);
					e.consume();
					return;
				case KeyEvent.VK_LEFT:
					arrowKey(-1, e.isShiftDown());
					e.consume();
					return;
				case KeyEvent.VK_RIGHT:
					arrowKey(+1, e.isShiftDown());
					e.consume();
					return;
				case KeyEvent.VK_UP:
					arrowKey(-getRowWidth(), e.isShiftDown());
					e.consume();
					return;
				case KeyEvent.VK_DOWN:
					arrowKey(+getRowWidth(), e.isShiftDown());
					e.consume();
					return;
				case KeyEvent.VK_PAGE_UP:
					arrowKey(-getPageSize(), e.isShiftDown());
					e.consume();
					return;
				case KeyEvent.VK_PAGE_DOWN:
					arrowKey(+getPageSize(), e.isShiftDown());
					e.consume();
					return;
				case KeyEvent.VK_HOME:
					if (e.isShiftDown()) document.setSelectionEnd(0);
					else document.setSelectionRange(0, 0);
					e.consume();
					return;
				case KeyEvent.VK_END:
					long el = document.length();
					if (e.isShiftDown()) document.setSelectionEnd(el);
					else document.setSelectionRange(el, el);
					e.consume();
					return;
				case KeyEvent.VK_COPY:
					if (e.isShiftDown()) copyAsString();
					else if (e.isAltDown()) copyAsHex();
					else copy();
					e.consume();
					return;
			}
			if (readOnly) return;
			switch (e.getKeyCode()) {
				case KeyEvent.VK_BACK_SPACE:
					document.deleteBackward();
					e.consume();
					return;
				case KeyEvent.VK_DELETE:
					document.deleteForward();
					e.consume();
					return;
				case KeyEvent.VK_UNDO:
					if (e.isShiftDown()) redo();
					else undo();
					e.consume();
					return;
				case KeyEvent.VK_CUT:
					if (e.isShiftDown()) cutAsString();
					else if (e.isAltDown()) cutAsHex();
					else cut();
					e.consume();
					return;
				case KeyEvent.VK_PASTE:
					if (e.isShiftDown()) pasteAsString();
					else if (e.isAltDown()) pasteAsHex();
					else paste();
					e.consume();
					return;
			}
		}
		@Override
		public void keyTyped(KeyEvent e) {
			if (e.isMetaDown() || e.isControlDown()) return;
			char ch = e.getKeyChar();
			if ((ch >= 0x20 && ch < 0x7F) || (ch >= 0xA0 && ch < 0xFFFD)) {
				if (textActive) {
					if (readOnly) return;
					byte[] data;
					try { data = Character.toString(ch).getBytes(charset); }
					catch (Exception ignored) { return; }
					if (overtype) document.overwrite(data);
					else document.insert(data);
					e.consume();
					return;
				} else {
					int d = Character.digit(ch, 16);
					if (d >= 0) {
						if (readOnly) return;
						if (overtype) document.overwrite(d);
						else document.insert(d);
						e.consume();
						return;
					}
					if (!enableTransformKeys) return;
					switch (ch) {
						case 'H': case 'h':
							setDecimalAddresses(!decimalAddresses);
							e.consume();
							return;
						case 'L': case 'l':
							setLittleEndian(!littleEndian);
							e.consume();
							return;
					}
					if (readOnly) return;
					switch (ch) {
						case 'Z': case 'z':
							document.transformSelection(BitTransform.ZERO);
							e.consume();
							return;
						case 'Y': case 'y':
							document.transformSelection(BitTransform.SPACE);
							e.consume();
							return;
						case 'X': case 'x':
							document.transformSelection(BitTransform.ONE);
							e.consume();
							return;
						case 'I': case 'i':
							document.transformSelection(BitTransform.INVERT);
							e.consume();
							return;
						case 'V': case 'v':
							document.transformSelection(RandomTransform.RANDOM);
							e.consume();
							return;
						case 'S': case 's':
							document.transformSelection(ReverseTransform.BYTES);
							e.consume();
							return;
						case 'N': case 'n':
							document.transformSelection(ReverseTransform.NYBBLES);
							e.consume();
							return;
						case 'R': case 'r':
							document.transformSelection(ReverseTransform.BITS);
							e.consume();
							return;
						case '[': case '{':
							document.transformSelection(littleEndian ? RotateTransform.ROL_LE : RotateTransform.ROL_BE);
							e.consume();
							return;
						case ']': case '}':
							document.transformSelection(littleEndian ? RotateTransform.ROR_LE : RotateTransform.ROR_BE);
							e.consume();
							return;
						case ',': case '<':
							document.transformSelection(littleEndian ? RotateTransform.ASL_LE : RotateTransform.ASL_BE);
							e.consume();
							return;
						case '.': case '>':
							document.transformSelection(littleEndian ? RotateTransform.LSR_LE : RotateTransform.LSR_BE);
							e.consume();
							return;
						case '/': case '?':
							document.transformSelection(littleEndian ? RotateTransform.ASR_LE : RotateTransform.ASR_BE);
							e.consume();
							return;
						case '=': case '+':
							document.transformSelection(littleEndian ? IncrementTransform.INC_LE : IncrementTransform.INC_BE);
							e.consume();
							return;
						case '-': case '_':
							document.transformSelection(littleEndian ? IncrementTransform.DEC_LE : IncrementTransform.DEC_BE);
							e.consume();
							return;
					}
				}
			}
		}
	};
	
	private void arrowKey(int dir, boolean shiftKey) {
		long length = document.length();
		long offset = document.getSelectionEnd() + dir;
		if (offset < 0) offset = 0;
		if (offset > length) offset = length;
		if (shiftKey) document.setSelectionEnd(offset);
		else document.setSelectionRange(offset, offset);
	}
	
	private int getRowWidth() {
		Insets i = getInsets();
		int w = getWidth() - i.left - i.right;
		FontMetrics fm = getFontMetrics(getFont());
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int bpr = (w - 11 * cw) / (4 * cw);
		if (bpr < 1) return 0;
		if (bpr > 4) bpr = 4 * (bpr / 4);
		return bpr;
	}
	
	private int getPageSize() {
		Insets i = getInsets();
		int w = getWidth() - i.left - i.right;
		FontMetrics fm = getFontMetrics(getFont());
		int ch = fm.getHeight() + 2;
		int cw = fm.stringWidth("0123456789ABCDEF") / 16;
		int bpr = (w - 11 * cw) / (4 * cw);
		if (bpr < 1) return 0;
		if (bpr > 4) bpr = 4 * (bpr / 4);
		int rows = getVisibleRect().height / ch;
		if (rows < 1) rows = 1;
		return bpr * rows;
	}
	
	private static final String[] HEX = {
		"00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F",
		"10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F",
		"20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F",
		"30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F",
		"40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F",
		"50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F",
		"60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F",
		"70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F",
		"80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F",
		"90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F",
		"A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF",
		"B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF",
		"C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF",
		"D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF",
		"E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF",
		"F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF",
	};
}
