package com.kreative.hexcellent.buffer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ByteBufferDocument {
	private final ByteBuffer buffer;
	private final ByteBufferSelectionModel selection;
	private final ByteBufferHistory history;
	
	public ByteBufferDocument(ByteBuffer buffer) {
		this.buffer = buffer;
		this.selection = new ByteBufferSelectionModel();
		this.history = new ByteBufferHistory();
	}
	
	public ByteBuffer getByteBuffer() { return buffer; }
	public boolean isEmpty() { return buffer.isEmpty(); }
	public long length() { return buffer.length(); }
	public boolean get(long offset, byte[] dst, int dstOffset, int length) { return buffer.get(offset, dst, dstOffset, length); }
	public ByteBuffer slice(long offset, long length) { return buffer.slice(offset, length); }
	public boolean write(OutputStream out, long offset, long length) throws IOException { return buffer.write(out, offset, length); }
	public long indexOf(byte[] pattern) { return buffer.indexOf(pattern); }
	public long indexOf(byte[] pattern, long index) { return buffer.indexOf(pattern, index); }
	public long lastIndexOf(byte[] pattern) { return buffer.lastIndexOf(pattern); }
	public long lastIndexOf(byte[] pattern, long index) { return buffer.lastIndexOf(pattern, index); }
	public void addByteBufferListener(ByteBufferListener l) { buffer.addByteBufferListener(l); }
	public void removeByteBufferListener(ByteBufferListener l) { buffer.removeByteBufferListener(l); }
	
	public ByteBufferSelectionModel getSelectionModel() { return selection; }
	public long getSelectionStart() { return selection.getSelectionStart(); }
	public long getSelectionEnd() { return selection.getSelectionEnd(); }
	public long getSelectionMin() { return selection.getSelectionMin(); }
	public long getSelectionMax() { return selection.getSelectionMax(); }
	public long getSelectionLength() { return selection.getSelectionLength(); }
	public boolean isMidByte() { return selection.isMidByte(); }
	public void setSelectionStart(long start) { selection.setSelectionStart(start); }
	public void setSelectionEnd(long end) { selection.setSelectionEnd(end); }
	public void setSelectionRange(long start, long end) { selection.setSelectionRange(start, end); }
	public void setMidByte(boolean midbyte) { selection.setMidByte(midbyte); }
	public void selectAll() { selection.setSelectionRange(0, buffer.length()); }
	public void addSelectionListener(ByteBufferSelectionListener l) { selection.addSelectionListener(l); }
	public void removeSelectionListener(ByteBufferSelectionListener l) { selection.removeSelectionListener(l); }
	
	public ByteBufferHistory getHistory() { return history; }
	public boolean canUndo() { return history.canUndo(); }
	public boolean canRedo() { return history.canRedo(); }
	public ByteBufferAction getUndoAction() { return history.getUndoAction(); }
	public ByteBufferAction getRedoAction() { return history.getRedoAction(); }
	public String getUndoActionName() { return history.getUndoActionName(); }
	public String getRedoActionName() { return history.getRedoActionName(); }
	public void undo() { history.undo(); }
	public void redo() { history.redo(); }
	public void clearHistory() { history.clear(); }
	
	public byte[] getSelection() {
		long length = selection.getSelectionLength();
		if (length <= 0 || length >= Integer.MAX_VALUE) return null;
		long offset = selection.getSelectionMin();
		byte[] data = new byte[(int)length];
		buffer.get(offset, data, 0, (int)length);
		return data;
	}
	
	public String getSelectionAsHex() {
		byte[] data = getSelection();
		if (data == null) return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			if (i > 0) sb.append(((i & 0x0F) == 0) ? '\n' : ' ');
			sb.append(HEX[data[i] & 0xFF]);
		}
		return sb.toString();
	}
	
	public String getSelectionAsString(String charset) {
		byte[] data = getSelection();
		if (data == null) return null;
		try { return new String(data, charset); }
		catch (IOException e) { return null; }
	}
	
	public String getSelectionEncoded(ByteEncoder encoder) {
		byte[] data = getSelection();
		if (data == null) return null;
		try { return encoder.encode(data, 0, data.length); }
		catch (IOException e) { return null; }
	}
	
	private class DeleteSelectionAction extends ByteBufferAction {
		private final long selectionStart;
		private final long selectionEnd;
		private final long offset;
		private final byte[] removed;
		public DeleteSelectionAction(String name) {
			super(name);
			this.selectionStart = selection.getSelectionStart();
			this.selectionEnd = selection.getSelectionEnd();
			this.offset = Math.min(selectionStart, selectionEnd);
			int length = (int)Math.abs(selectionStart - selectionEnd);
			buffer.get(offset, this.removed = new byte[length], 0, length);
		}
		@Override
		public void redo() {
			buffer.remove(offset, removed.length);
			selection.setSelectionRange(offset, offset);
		}
		@Override
		public void undo() {
			buffer.insert(offset, removed, 0, removed.length);
			selection.setSelectionRange(selectionStart, selectionEnd);
		}
	}
	
	public boolean deleteSelection(String actionName) {
		long length = selection.getSelectionLength();
		if (length <= 0 || length >= Integer.MAX_VALUE) return false;
		ByteBufferAction a = new DeleteSelectionAction(actionName);
		a.redo();
		history.add(a);
		return true;
	}
	
	private class ReplaceSelectionAction extends ByteBufferAction {
		private final long selectionStart;
		private final long selectionEnd;
		private final long offset;
		private final byte[] removed;
		private final byte[] inserted;
		private final boolean keepSelected;
		public ReplaceSelectionAction(String name, byte[] data, boolean keepSelected) {
			super(name);
			this.selectionStart = selection.getSelectionStart();
			this.selectionEnd = selection.getSelectionEnd();
			this.offset = Math.min(selectionStart, selectionEnd);
			int length = (int)Math.abs(selectionStart - selectionEnd);
			buffer.get(offset, this.removed = new byte[length], 0, length);
			this.inserted = data;
			this.keepSelected = keepSelected;
		}
		@Override
		public void redo() {
			buffer.remove(offset, removed.length);
			buffer.insert(offset, inserted, 0, inserted.length);
			if (keepSelected) selection.setSelectionRange(offset, offset + inserted.length);
			else selection.setSelectionRange(offset + inserted.length, offset + inserted.length);
		}
		@Override
		public void undo() {
			buffer.remove(offset, inserted.length);
			buffer.insert(offset, removed, 0, removed.length);
			selection.setSelectionRange(selectionStart, selectionEnd);
		}
	}
	
	public boolean replaceSelection(String actionName, byte[] data, boolean keepSelected) {
		long length = selection.getSelectionLength();
		if (length >= Integer.MAX_VALUE) return false;
		ByteBufferAction a = new ReplaceSelectionAction(actionName, data, keepSelected);
		a.redo();
		history.add(a);
		return true;
	}
	
	public boolean transformSelection(ByteTransform tx) {
		long length = selection.getSelectionLength();
		if (length <= 0 || length >= Integer.MAX_VALUE) return false;
		long offset = selection.getSelectionMin();
		byte[] data = new byte[(int)length];
		if (!buffer.get(offset, data, 0, (int)length)) return false;
		if (!tx.transform(data, 0, (int)length)) return false;
		ByteBufferAction a = new ReplaceSelectionAction(tx.getName(), data, true);
		a.redo();
		history.add(a);
		return true;
	}
	
	private class ReplaceAllAction extends ByteBufferAction {
		private final long selectionStart;
		private final long selectionEnd;
		private final byte[] pattern;
		private final byte[] replacement;
		private final List<Long> offsets;
		public ReplaceAllAction(byte[] pattern, byte[] replacement) {
			super("Replace All");
			this.selectionStart = selection.getSelectionStart();
			this.selectionEnd = selection.getSelectionEnd();
			this.pattern = pattern;
			this.replacement = replacement;
			this.offsets = new ArrayList<Long>();
		}
		@Override
		public void redo() {
			long ss = selectionStart;
			long se = selectionEnd;
			if (offsets.isEmpty()) {
				long o = buffer.indexOf(pattern);
				while (o >= 0) {
					if (!buffer.remove(o, pattern.length)) break;
					if (!buffer.insert(o, replacement, 0, replacement.length)) break;
					offsets.add(o);
					ss = se = o + replacement.length;
					o = buffer.indexOf(pattern, ss);
				}
			} else {
				for (long o : offsets) {
					if (!buffer.remove(o, pattern.length)) break;
					if (!buffer.insert(o, replacement, 0, replacement.length)) break;
					ss = se = o + replacement.length;
				}
			}
			selection.setSelectionRange(ss, se);
		}
		@Override
		public void undo() {
			for (int i = offsets.size() - 1; i >= 0; i--) {
				long o = offsets.get(i);
				buffer.remove(o, replacement.length);
				buffer.insert(o, pattern, 0, pattern.length);
			}
			selection.setSelectionRange(selectionStart, selectionEnd);
		}
	}
	
	public boolean replaceAll(byte[] pattern, byte[] replacement) {
		if (pattern.length == 0) return false;
		ReplaceAllAction a = new ReplaceAllAction(pattern, replacement);
		a.redo();
		if (a.offsets.isEmpty()) return false;
		history.add(a);
		return true;
	}
	
	public boolean cutAsHex() {
		return copyAsHex() && deleteSelection("Cut");
	}
	
	public boolean cutAsString(String charset) {
		return copyAsString(charset) && deleteSelection("Cut");
	}
	
	public boolean cutEncoded(ByteEncoder encoder) {
		return copyEncoded(encoder) && deleteSelection("Cut");
	}
	
	public boolean copyAsHex() {
		return setClipboardString(getSelectionAsHex());
	}
	
	public boolean copyAsString(String charset) {
		return setClipboardString(getSelectionAsString(charset));
	}
	
	public boolean copyEncoded(ByteEncoder encoder) {
		return setClipboardString(getSelectionEncoded(encoder));
	}
	
	public boolean pasteAsHex() {
		String s = getClipboardString();
		if (s == null) return false;
		byte[] data = decodeHex(s);
		if (data == null) return false;
		return replaceSelection("Paste", data, false);
	}
	
	public boolean pasteAsString(String charset) {
		String s = getClipboardString();
		if (s == null) return false;
		try { return replaceSelection("Paste", s.getBytes(charset), false); }
		catch (IOException e) { return false; }
	}
	
	public boolean pasteDecoded(ByteDecoder decoder) {
		String s = getClipboardString();
		if (s == null) return false;
		try { return replaceSelection("Paste", decoder.decode(s), false); }
		catch (IOException e) { return false; }
	}
	
	private class KeyboardDeleteAction extends ByteBufferAction {
		private final long origin;
		private long offset;
		private byte[] removed;
		public KeyboardDeleteAction(boolean forward) {
			super("Delete");
			this.origin = selection.getSelectionStart();
			this.offset = forward ? origin : (origin - 1);
			this.removed = new byte[1];
			buffer.get(offset, removed, 0, 1);
		}
		public boolean prepend() {
			if (selection.getSelectionStart() != this.offset) return false;
			if (selection.getSelectionEnd() != this.offset) return false;
			this.offset--;
			//
			byte[] newRemoved = new byte[removed.length + 1];
			buffer.get(offset, newRemoved, 0, 1);
			for (int i = 0; i < removed.length; i++) newRemoved[i + 1] = removed[i];
			//
			this.removed = newRemoved;
			return true;
		}
		public boolean append() {
			if (selection.getSelectionStart() != this.offset) return false;
			if (selection.getSelectionEnd() != this.offset) return false;
			//
			byte[] newRemoved = new byte[removed.length + 1];
			for (int i = 0; i < removed.length; i++) newRemoved[i] = removed[i];
			buffer.get(offset, newRemoved, removed.length, 1);
			//
			this.removed = newRemoved;
			return true;
		}
		@Override
		public void redo() {
			buffer.remove(offset, removed.length);
			selection.setSelectionRange(offset, offset);
		}
		@Override
		public void undo() {
			buffer.insert(offset, removed, 0, removed.length);
			selection.setSelectionRange(origin, origin);
		}
	}
	
	public boolean deleteBackward() {
		if (selection.getSelectionLength() > 0) {
			return deleteSelection("Delete");
		} else {
			long offset = selection.getSelectionStart();
			if (offset <= 0) return false;
			ByteBufferAction a = history.getUndoAction();
			if (!(a instanceof KeyboardDeleteAction && ((KeyboardDeleteAction)a).prepend())) {
				history.add(new KeyboardDeleteAction(false));
			}
			buffer.remove(offset - 1, 1);
			selection.setSelectionRange(offset - 1, offset - 1);
			return true;
		}
	}
	
	public boolean deleteForward() {
		if (selection.getSelectionLength() > 0) {
			return deleteSelection("Delete");
		} else {
			long offset = selection.getSelectionStart();
			if (offset >= buffer.length()) return false;
			ByteBufferAction a = history.getUndoAction();
			if (!(a instanceof KeyboardDeleteAction && ((KeyboardDeleteAction)a).append())) {
				history.add(new KeyboardDeleteAction(true));
			}
			buffer.remove(offset, 1);
			selection.setSelectionRange(offset, offset);
			return true;
		}
	}
	
	private class KeyboardInsertAction extends ByteBufferAction {
		private final long selectionStart;
		private final long selectionEnd;
		private final long offset;
		private final byte[] removed;
		private byte[] inserted;
		public KeyboardInsertAction(byte[] data) {
			super("Insert");
			this.selectionStart = selection.getSelectionStart();
			this.selectionEnd = selection.getSelectionEnd();
			this.offset = Math.min(selectionStart, selectionEnd);
			int length = (int)Math.abs(selectionStart - selectionEnd);
			buffer.get(offset, this.removed = new byte[length], 0, length);
			this.inserted = data;
		}
		public boolean append(byte[] data) {
			if (selection.getSelectionStart() != offset + inserted.length) return false;
			if (selection.getSelectionEnd() != offset + inserted.length) return false;
			//
			byte[] newInserted = new byte[inserted.length + data.length];
			for (int i = 0; i < inserted.length; i++) newInserted[i] = inserted[i];
			for (int i = 0; i < data.length; i++) newInserted[i + inserted.length] = data[i];
			//
			this.inserted = newInserted;
			return true;
		}
		public boolean shiftIn(int nybble) {
			if (selection.getSelectionStart() != offset + inserted.length) return false;
			if (selection.getSelectionEnd() != offset + inserted.length) return false;
			if (selection.isMidByte() && inserted.length > 0) {
				inserted[inserted.length - 1] <<= 4;
				inserted[inserted.length - 1] |= nybble;
				return true;
			}
			return false;
		}
		@Override
		public void redo() {
			buffer.remove(offset, removed.length);
			buffer.insert(offset, inserted, 0, inserted.length);
			selection.setSelectionRange(offset + inserted.length, offset + inserted.length);
		}
		@Override
		public void undo() {
			buffer.remove(offset, inserted.length);
			buffer.insert(offset, removed, 0, removed.length);
			selection.setSelectionRange(selectionStart, selectionEnd);
		}
	}
	
	public boolean insert(byte[] data) {
		long length = selection.getSelectionLength();
		if (length >= Integer.MAX_VALUE) return false;
		ByteBufferAction a = history.getUndoAction();
		if (a instanceof KeyboardInsertAction) {
			KeyboardInsertAction ia = (KeyboardInsertAction)a;
			if (ia.append(data)) {
				long offset = selection.getSelectionStart();
				buffer.insert(offset, data, 0, data.length);
				selection.setSelectionRange(offset + data.length, offset + data.length);
				return true;
			}
		}
		a = new KeyboardInsertAction(data);
		a.redo();
		history.add(a);
		return true;
	}
	
	public boolean insert(int nybble) {
		long length = selection.getSelectionLength();
		if (length >= Integer.MAX_VALUE) return false;
		ByteBufferAction a = history.getUndoAction();
		if (a instanceof KeyboardInsertAction) {
			KeyboardInsertAction ia = (KeyboardInsertAction)a;
			if (ia.shiftIn(nybble)) {
				long offset = selection.getSelectionStart();
				byte[] tmp = new byte[1];
				buffer.get(offset - 1, tmp, 0, 1);
				tmp[0] <<= 4;
				tmp[0] |= nybble;
				buffer.overwrite(offset - 1, tmp, 0, 1);
				selection.setMidByte(false);
				return true;
			}
		}
		if (insert(new byte[]{(byte)nybble})) {
			selection.setMidByte(true);
			return true;
		}
		return false;
	}
	
	private class KeyboardOverwriteAction extends ByteBufferAction {
		private final long selectionStart;
		private final long selectionEnd;
		private final long offset;
		private byte[] removed;
		private byte[] inserted;
		public KeyboardOverwriteAction(byte[] data) {
			super("Overwrite");
			this.selectionStart = selection.getSelectionStart();
			this.selectionEnd = selection.getSelectionEnd();
			this.offset = Math.min(selectionStart, selectionEnd);
			int slength = (int)Math.abs(selectionStart - selectionEnd);
			long tlength = buffer.length() - Math.max(selectionStart, selectionEnd);
			int rlength = slength + (int)Math.min(data.length, tlength);
			buffer.get(offset, this.removed = new byte[rlength], 0, rlength);
			this.inserted = data;
		}
		public boolean append(byte[] data) {
			if (selection.getSelectionStart() != offset + inserted.length) return false;
			if (selection.getSelectionEnd() != offset + inserted.length) return false;
			//
			long tlength = buffer.length() - (offset + inserted.length);
			int ralength = (int)Math.min(data.length, tlength);
			byte[] newRemoved = new byte[removed.length + ralength];
			for (int i = 0; i < removed.length; i++) newRemoved[i] = removed[i];
			buffer.get(offset + inserted.length, newRemoved, removed.length, ralength);
			//
			byte[] newInserted = new byte[inserted.length + data.length];
			for (int i = 0; i < inserted.length; i++) newInserted[i] = inserted[i];
			for (int i = 0; i < data.length; i++) newInserted[i + inserted.length] = data[i];
			//
			this.removed = newRemoved;
			this.inserted = newInserted;
			return true;
		}
		public boolean shiftIn(int nybble) {
			if (selection.getSelectionStart() != offset + inserted.length) return false;
			if (selection.getSelectionEnd() != offset + inserted.length) return false;
			if (selection.isMidByte() && inserted.length > 0) {
				inserted[inserted.length - 1] <<= 4;
				inserted[inserted.length - 1] |= nybble;
				return true;
			}
			return false;
		}
		@Override
		public void redo() {
			buffer.remove(offset, removed.length);
			buffer.insert(offset, inserted, 0, inserted.length);
			selection.setSelectionRange(offset + inserted.length, offset + inserted.length);
		}
		@Override
		public void undo() {
			buffer.remove(offset, inserted.length);
			buffer.insert(offset, removed, 0, removed.length);
			selection.setSelectionRange(selectionStart, selectionEnd);
		}
	}
	
	public boolean overwrite(byte[] data) {
		long length = selection.getSelectionLength();
		if (length >= Integer.MAX_VALUE) return false;
		ByteBufferAction a = history.getUndoAction();
		if (a instanceof KeyboardOverwriteAction) {
			KeyboardOverwriteAction oa = (KeyboardOverwriteAction)a;
			if (oa.append(data)) {
				long offset = selection.getSelectionStart();
				long tlength = buffer.length() - offset;
				int ralength = (int)Math.min(data.length, tlength);
				buffer.remove(offset, ralength);
				buffer.insert(offset, data, 0, data.length);
				selection.setSelectionRange(offset + data.length, offset + data.length);
				return true;
			}
		}
		a = new KeyboardOverwriteAction(data);
		a.redo();
		history.add(a);
		return true;
	}
	
	public boolean overwrite(int nybble) {
		long length = selection.getSelectionLength();
		if (length >= Integer.MAX_VALUE) return false;
		ByteBufferAction a = history.getUndoAction();
		if (a instanceof KeyboardOverwriteAction) {
			KeyboardOverwriteAction oa = (KeyboardOverwriteAction)a;
			if (oa.shiftIn(nybble)) {
				long offset = selection.getSelectionStart();
				byte[] tmp = new byte[1];
				buffer.get(offset - 1, tmp, 0, 1);
				tmp[0] <<= 4;
				tmp[0] |= nybble;
				buffer.overwrite(offset - 1, tmp, 0, 1);
				selection.setMidByte(false);
				return true;
			}
		}
		if (overwrite(new byte[]{(byte)nybble})) {
			selection.setMidByte(true);
			return true;
		}
		return false;
	}
	
	private static boolean setClipboardString(String s) {
		if (s == null) return false;
		Toolkit tk = Toolkit.getDefaultToolkit();
		Clipboard cb = tk.getSystemClipboard();
		StringSelection ss = new StringSelection(s);
		cb.setContents(ss, ss);
		return true;
	}
	
	private static String getClipboardString() {
		try {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Clipboard cb = tk.getSystemClipboard();
			return cb.getData(DataFlavor.stringFlavor).toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	private static boolean isHexDigit(char ch) {
		return (
			(ch >= '0' && ch <= '9') ||
			(ch >= 'A' && ch <= 'F') ||
			(ch >= 'a' && ch <= 'f')
		);
	}
	
	private static int hexDigitValue(char ch) {
		return (
			(ch >= '0' && ch <= '9') ? (ch - '0') :
			(ch >= 'A' && ch <= 'F') ? (ch - 'A' + 10) :
			(ch >= 'a' && ch <= 'f') ? (ch - 'a' + 10) :
			(-1)
		);
	}
	
	private static byte[] decodeHex(String s) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		char[] ch = s.toCharArray(); int i = 0, n = ch.length;
		for (;;) {
			while (i < n && Character.isWhitespace(ch[i])) i++;
			if (i < n) {
				if (isHexDigit(ch[i])) {
					int v = hexDigitValue(ch[i]) << 4; i++;
					if (i < n) {
						if (isHexDigit(ch[i])) {
							v |= hexDigitValue(ch[i]); i++;
							out.write(v);
							continue;
						}
					}
				}
				return null;
			} else {
				return out.toByteArray();
			}
		}
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
