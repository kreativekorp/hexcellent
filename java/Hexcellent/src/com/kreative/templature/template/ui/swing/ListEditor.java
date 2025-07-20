package com.kreative.templature.template.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import com.kreative.templature.template.ListDataModel;
import com.kreative.templature.template.Template;

public class ListEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final int LABEL_GAP = 8;
	private static final int ITEM_GAP = 12;
	private static final int INDENT = 20;
	
	private final SwingComponentFactory factory;
	private final ListDataModel model;
	private final JLabel countLabel;
	private final JPanel contentPanel;
	private final ArrayList<EntryLabelPanel> entryLabelPanels;
	private final ArrayList<EntryContentPanel> entryContentPanels;
	private int entryCount;
	
	public ListEditor(SwingComponentFactory factory, ListDataModel model) {
		super(new BorderLayout(ITEM_GAP, ITEM_GAP));
		this.factory = factory;
		this.model = model;
		
		String countLabelText = model.getCountLabelText();
		if (countLabelText == null) {
			countLabel = null;
		} else {
			countLabel = new JLabel(model.getCountValueText());
			JLabel label = factory.createLabelComponent(countLabelText);
			JPanel countPanel = new JPanel(new BorderLayout(LABEL_GAP, LABEL_GAP));
			countPanel.add(factory.topAlign(label), BorderLayout.LINE_START);
			countPanel.add(factory.topAlign(countLabel), BorderLayout.CENTER);
			add(countPanel, BorderLayout.PAGE_START);
		}
		
		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		entryLabelPanels = new ArrayList<EntryLabelPanel>();
		entryContentPanels = new ArrayList<EntryContentPanel>();
		entryCount = model.getEntryCount();
		
		for (int i = 0; i < entryCount; i++) {
			EntryLabelPanel elp = new EntryLabelPanel(i);
			EntryContentPanel ecp = new EntryContentPanel(model.getEntry(i));
			contentPanel.add(elp);
			contentPanel.add(ecp);
			entryLabelPanels.add(elp);
			entryContentPanels.add(ecp);
		}
		
		EntryLabelPanel elp = new EntryLabelPanel(entryCount);
		contentPanel.add(elp);
		entryLabelPanels.add(elp);
		
		JPanel indentPanel = new JPanel(new BorderLayout());
		indentPanel.add(Box.createRigidArea(new Dimension(INDENT, INDENT)), BorderLayout.LINE_START);
		indentPanel.add(contentPanel, BorderLayout.CENTER);
		add(indentPanel, BorderLayout.CENTER);
	}
	
	public void addNewEntry(int index) {
		try {
			Template.Instance entry = model.createNewEntry();
			// don't do any of the following if createNewEntry() fails
			requestFocusInWindow();
			if (index < 0) index = 0;
			if (index > entryCount) index = entryCount;
			EntryLabelPanel elp = new EntryLabelPanel(index);
			EntryContentPanel ecp = new EntryContentPanel(entry);
			model.addEntry(index, entry);
			contentPanel.add(elp, index * 2);
			contentPanel.add(ecp, index * 2 + 1);
			entryLabelPanels.add(index, elp);
			entryContentPanels.add(index, ecp);
			entryCount++;
			elp.focus();
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateLabels();
	}
	
	public void removeEntry(int index) {
		if (entryCount > 0) {
			requestFocusInWindow();
			if (index < 0) index = 0;
			if (index > entryCount-1) index = entryCount-1;
			model.removeEntry(index);
			contentPanel.remove(entryLabelPanels.remove(index));
			contentPanel.remove(entryContentPanels.remove(index));
			entryCount--;
			entryLabelPanels.get(index).focus();
		}
		updateLabels();
	}
	
	public void moveUpEntry(int index) {
		if (entryCount > 1 && index > 0) {
			requestFocusInWindow();
			if (index > entryCount-1) index = entryCount-1;
			Template.Instance entry = model.removeEntry(index);
			EntryLabelPanel elp = entryLabelPanels.remove(index);
			EntryContentPanel ecp = entryContentPanels.remove(index);
			contentPanel.remove(elp);
			contentPanel.remove(ecp);
			index--;
			model.addEntry(index, entry);
			contentPanel.add(elp, index * 2);
			contentPanel.add(ecp, index * 2 + 1);
			entryLabelPanels.add(index, elp);
			entryContentPanels.add(index, ecp);
			elp.focus();
		}
		updateLabels();
	}
	
	public void moveDownEntry(int index) {
		if (entryCount > 1 && index < entryCount-1) {
			requestFocusInWindow();
			if (index < 0) index = 0;
			Template.Instance entry = model.removeEntry(index);
			EntryLabelPanel elp = entryLabelPanels.remove(index);
			EntryContentPanel ecp = entryContentPanels.remove(index);
			contentPanel.remove(elp);
			contentPanel.remove(ecp);
			index++;
			model.addEntry(index, entry);
			contentPanel.add(elp, index * 2);
			contentPanel.add(ecp, index * 2 + 1);
			entryLabelPanels.add(index, elp);
			entryContentPanels.add(index, ecp);
			elp.focus();
		}
		updateLabels();
	}
	
	private void updateLabels() {
		if (countLabel != null) {
			countLabel.setText(model.getCountValueText());
		}
		for (int i = 0; i <= entryCount; i++) {
			entryLabelPanels.get(i).setIndex(i);
		}
	}
	
	private class EntryLabelPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private int index;
		private final JLabel label;
		private final JPopupMenu popup;
		private EntryLabelPanel(int initialIndex) {
			super(new BorderLayout());
			index = initialIndex;
			
			label = new JLabel(model.getEntryLabelText(index));
			label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			label.setFocusable(true);
			add(label, BorderLayout.LINE_START);
			
			JMenuItem addMenuItem = new JMenuItem("Insert New Entry");
			JMenuItem removeMenuItem = new JMenuItem("Remove Entry");
			JMenuItem moveUpMenuItem = new JMenuItem("Move Above");
			JMenuItem moveDownMenuItem = new JMenuItem("Move Below");
			
			int mods = getToolkit().getMenuShortcutKeyMask();
			addMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, mods));
			removeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, mods));
			moveUpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, mods));
			moveDownMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, mods));
			
			popup = new JPopupMenu();
			popup.add(addMenuItem);
			popup.add(removeMenuItem);
			popup.add(moveUpMenuItem);
			popup.add(moveDownMenuItem);
			
			label.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
					label.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
				}
				public void focusLost(FocusEvent e) {
					label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
				}
			});
			label.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (label.isFocusOwner()) showPopup();
					else label.requestFocusInWindow();
				}
			});
			label.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
						case KeyEvent.VK_ESCAPE:
						case KeyEvent.VK_CLEAR:
							ListEditor.this.requestFocusInWindow();
							e.consume();
							break;
						case KeyEvent.VK_INSERT:
							addNewEntry(index);
							e.consume();
							break;
						case KeyEvent.VK_DELETE:
							removeEntry(index);
							e.consume();
							break;
						case KeyEvent.VK_K:
							if (e.isControlDown() || e.isMetaDown()) addNewEntry(index);
							e.consume();
							break;
						case KeyEvent.VK_BACK_SPACE:
							if (e.isControlDown() || e.isMetaDown()) removeEntry(index);
							e.consume();
							break;
						case KeyEvent.VK_UP:
						case KeyEvent.VK_KP_UP:
							if (e.isControlDown() || e.isMetaDown()) moveUpEntry(index);
							else if (index > 0) entryLabelPanels.get(index - 1).focus();
							e.consume();
							break;
						case KeyEvent.VK_DOWN:
						case KeyEvent.VK_KP_DOWN:
							if (e.isControlDown() || e.isMetaDown()) moveDownEntry(index);
							else if (index < entryCount) entryLabelPanels.get(index + 1).focus();
							e.consume();
							break;
						case KeyEvent.VK_SPACE:
						case KeyEvent.VK_ENTER:
							if (e.isControlDown() || e.isMetaDown()) addNewEntry(index);
							else showPopup();
							e.consume();
							break;
					}
				}
			});
			
			addMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addNewEntry(index);
				}
			});
			removeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeEntry(index);
				}
			});
			moveUpMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moveUpEntry(index);
				}
			});
			moveDownMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moveDownEntry(index);
				}
			});
		}
		private void focus() {
			label.requestFocusInWindow();
			label.scrollRectToVisible(label.getBounds());
		}
		private void showPopup() {
			popup.show(label, 0, label.getHeight());
		}
		private void setIndex(int newIndex) {
			index = newIndex;
			label.setText(model.getEntryLabelText(index));
		}
	}
	
	private class EntryContentPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private EntryContentPanel(Template.Instance inst) {
			super(new BorderLayout());
			add(Box.createRigidArea(new Dimension(ITEM_GAP, ITEM_GAP)), BorderLayout.PAGE_START);
			add(inst.createComponent(factory), BorderLayout.CENTER);
			add(Box.createRigidArea(new Dimension(ITEM_GAP, ITEM_GAP)), BorderLayout.PAGE_END);
		}
	}
}
