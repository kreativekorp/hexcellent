package com.kreative.templature.template.ui.swing;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class DocumentAdapter implements DocumentListener {
	public void insertUpdate(DocumentEvent e) { update(e); }
	public void removeUpdate(DocumentEvent e) { update(e); }
	public void changedUpdate(DocumentEvent e) { update(e); }
	public abstract void update(DocumentEvent e);
}
