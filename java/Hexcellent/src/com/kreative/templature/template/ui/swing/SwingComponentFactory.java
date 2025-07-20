package com.kreative.templature.template.ui.swing;

import java.awt.*;
import java.awt.event.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.*;
import com.kreative.templature.template.*;

public class SwingComponentFactory implements ComponentFactory<Component> {
	private static final int LABEL_GAP = 8;
	private static final int ITEM_GAP = 12;
	private static final int LABEL_WIDTH = 100;
	private static final int TEXTAREA_ROWS = 4;
	private static final int TEXTAREA_COLS = 32;
	
	public Component createBooleanComponent(final BooleanDataModel m) {
		final JCheckBox c = new JCheckBox();
		c.setSelected(m.getBooleanValue());
		c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.setBooleanValue(c.isSelected());
			}
		});
		return leftAlign(c);
	}
	
	public Component createColorComponent(final ColorDataModel m) {
		final int[] rgba = m.getColorValues();
		final int[] rgbaMax = m.getColorMaxValues();
		final ColorEditor c = new ColorEditor(
			rgba[0], rgba[1], rgba[2], rgba[3],
			rgbaMax[0], rgbaMax[1], rgbaMax[2], rgbaMax[3]
		);
		c.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				int a = c.getAlpha();
				m.setColorValues(r, g, b, a);
			}
		});
		return leftAlign(c);
	}
	
	public Component createDateTimeComponent(final DateTimeDataModel m) {
		final SpinnerDateModel model = new SpinnerDateModel(
			m.getDateValue(),
			m.getDateMinValue(),
			m.getDateMaxValue(),
			Calendar.SECOND
		);
		final JSpinner spinner = new JSpinner(model);
		final JSpinner.DateEditor editor = (JSpinner.DateEditor)spinner.getEditor();
		final SimpleDateFormat format = editor.getFormat();
		format.applyPattern(format.toPattern().replaceAll("y+", "yyyy"));
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				m.setDateValue(model.getDate());
			}
		});
		return leftAlign(spinner);
	}
	
	public Component createEnumComponent(final EnumDataModel m) {
		final Map<BigInteger,String> map = m.getEnumValues();
		final JComboBox c = new JComboBox(map.keySet().toArray());
		c.setEditable(false);
		c.setSelectedIndex(-1);
		c.setSelectedItem(m.getEnumValue());
		c.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList l, Object v, int i, boolean s, boolean f) {
				return super.getListCellRendererComponent(l, lookup(v), i, s, f);
			}
			private Object lookup(Object v) {
				if (v == null) return v;
				String name = map.get(v);
				if (name == null) return v;
				return name;
			}
		});
		c.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Object o = c.getSelectedItem();
				if (o instanceof BigInteger) m.setEnumValue((BigInteger)o);
			}
		});
		return c;
	}
	
	public Component createFormComponent(List<String> labels, List<? extends Component> components, int count) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		boolean first = true;
		for (int i = 0; i < count; i++) {
			Component comp = components.get(i);
			if (comp != null) {
				String labelText = labels.get(i);
				if (labelText != null) {
					JLabel label = createLabelComponent(labelText);
					JPanel pp = new JPanel(new BorderLayout(LABEL_GAP, LABEL_GAP));
					pp.add(topAlign(label), BorderLayout.LINE_START);
					pp.add(topAlign(comp), BorderLayout.CENTER);
					comp = pp;
				}
				if (first) first = false;
				else p.add(Box.createRigidArea(new Dimension(ITEM_GAP, ITEM_GAP)));
				p.add(comp);
			}
		}
		return p;
	}
	
	public Component createHexAreaComponent(HexDataModel m) {
		return HexAreaFactory.newInstance().createHexAreaComponent(m);
	}
	
	public Component createListComponent(ListDataModel m) {
		return new ListEditor(this, m);
	}
	
	public Component createPoint2DComponent(final Point2DDataModel m) {
		final int[] xy = m.getPoint2DValues();
		final int[] xyMin = m.getPoint2DMinValues();
		final int[] xyMax = m.getPoint2DMaxValues();
		final Point2DEditor c = new Point2DEditor(
			xy[0], xy[1], xyMin[0], xyMin[1], xyMax[0], xyMax[1]
		);
		c.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int x = c.getPointX();
				int y = c.getPointY();
				m.setPoint2DValues(x, y);
			}
		});
		return leftAlign(c);
	}
	
	public Component createPoint3DComponent(final Point3DDataModel m) {
		final int[] xyz = m.getPoint3DValues();
		final int[] xyzMin = m.getPoint3DMinValues();
		final int[] xyzMax = m.getPoint3DMaxValues();
		final Point3DEditor c = new Point3DEditor(
			xyz[0], xyz[1], xyz[2], xyzMin[0], xyzMin[1], xyzMin[2], xyzMax[0], xyzMax[1], xyzMax[2]
		);
		c.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int x = c.getPointX();
				int y = c.getPointY();
				int z = c.getPointZ();
				m.setPoint3DValues(x, y, z);
			}
		});
		return leftAlign(c);
	}
	
	public Component createRectangleComponent(final RectangleDataModel m) {
		int[] ltrb = m.getRectangleValues();
		int[] ltrbMin = m.getRectangleMinValues();
		int[] ltrbMax = m.getRectangleMaxValues();
		final RectangleEditor c = new RectangleEditor(
			ltrb[0], ltrb[1], ltrb[2], ltrb[3],
			ltrbMin[0], ltrbMin[1], ltrbMin[2], ltrbMin[3],
			ltrbMax[0], ltrbMax[1], ltrbMax[2], ltrbMax[3]
		);
		c.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int l = c.getRectangleLeft();
				int t = c.getRectangleTop();
				int r = c.getRectangleRight();
				int b = c.getRectangleBottom();
				m.setRectangleValues(l, t, r, b);
			}
		});
		return leftAlign(c);
	}
	
	public Component createSwitchComponent(final SwitchDataModel m) {
		final JPanel p = new JPanel(new BorderLayout());
		final ArrayList<Component> cases = new ArrayList<Component>();
		for (int i = 0, k = m.getCaseIndex(), n = m.getCaseCount(); i < n; i++) {
			Component c = m.getCase(i).createComponent(this);
			if (i == k) p.add(c, BorderLayout.CENTER);
			cases.add(c);
		}
		m.addSwitchListener(new SwitchListener() {
			public void switchCase(SwitchDataModel model, int index) {
				if (index >= 0 && index < cases.size()) {
					Component nc = cases.get(index);
					if (nc != null) {
						p.removeAll();
						p.add(nc, BorderLayout.CENTER);
						p.revalidate();
						p.repaint();
						return;
					}
				}
				if (p.getComponentCount() > 0) {
					p.removeAll();
					p.revalidate();
					p.repaint();
				}
			}
		});
		return p;
	}
	
	public Component createTextAreaComponent(final StringDataModel m) {
		final JTextArea c = new JTextArea(m.getStringValue(), TEXTAREA_ROWS, TEXTAREA_COLS);
		c.setLineWrap(true);
		c.setWrapStyleWord(true);
		c.getDocument().addDocumentListener(new DocumentAdapter() {
			public void update(DocumentEvent e) {
				m.setStringValue(c.getText());
			}
		});
		return new JScrollPane(c, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	public Component createTextFieldComponent(final StringDataModel m) {
		final JTextField c = new JTextField(m.getStringValue());
		c.getDocument().addDocumentListener(new DocumentAdapter() {
			public void update(DocumentEvent e) {
				m.setStringValue(c.getText());
			}
		});
		return c;
	}
	
	public Component createTextFieldComponent(final StringDataModel m, final int columns) {
		final JTextField c = new JTextField(m.getStringValue(), columns);
		c.getDocument().addDocumentListener(new DocumentAdapter() {
			public void update(DocumentEvent e) {
				m.setStringValue(c.getText());
			}
		});
		return leftAlign(c);
	}
	
	public Component createHeadingComponent(String s, int level) {
		if (level < 1) level = 1; if (level > 6) level = 6;
		return leftAlign(new JLabel(quoteHTML(s, "<h" + level + ">", "</h" + level + ">")));
	}
	
	public Component createParagraphComponent(String s) {
		return leftAlign(new JLabel(quoteHTML(s, "<p>", "</p>")));
	}
	
	public Component createSeparatorComponent() {
		return new JSeparator();
	}
	
	protected JLabel createLabelComponent(String s) {
		return new JLabel(quoteHTML(s, "<body style=\"width:" + LABEL_WIDTH + "px\">", "</body>"));
	}
	
	protected JPanel leftAlign(Component component) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.LINE_START);
		return panel;
	}
	
	protected JPanel topAlign(Component component) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.PAGE_START);
		return panel;
	}
	
	protected String quoteHTML(String s, String open, String close) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		if (open != null) sb.append(open);
		if (s != null) {
			s = s.replaceAll("\r\n|\r|\n", "\n");
			for (int i = 0, n = s.length(); i < n; i++) {
				char ch = s.charAt(i);
				switch (ch) {
					case 10: sb.append("<br>"); break;
					case '&': sb.append("&amp;"); break;
					case '<': sb.append("&lt;"); break;
					case '>': sb.append("&gt;"); break;
					default: sb.append(ch); break;
				}
			}
		}
		if (close != null) sb.append(close);
		sb.append("</html>");
		return sb.toString();
	}
}
