package com.kreative.templature.template.ui.swing;

import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import com.kreative.templature.template.HexDataModel;

public class JHexAreaFactory extends HexAreaFactory {
	private static final int HEXAREA_ROWS = 8;
	private static final int HEXAREA_COLS = 48;
	
	public Component createHexAreaComponent(final HexDataModel m) {
		final JHexArea c = new JHexArea(m.getHexValue(), HEXAREA_ROWS, HEXAREA_COLS);
		c.setLineWrap(true);
		c.setWrapStyleWord(true);
		c.getDocument().addDocumentListener(new DocumentAdapter() {
			public void update(DocumentEvent e) {
				byte[] data = c.getData();
				if (data != null) m.setHexValue(data);
			}
		});
		return new JScrollPane(c, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
}
