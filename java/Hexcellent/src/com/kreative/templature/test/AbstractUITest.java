package com.kreative.templature.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import com.kreative.templature.template.BufferedCountedInputStream;
import com.kreative.templature.template.BufferedCountedOutputStream;
import com.kreative.templature.template.Closure;
import com.kreative.templature.template.TemplateItem;
import com.kreative.templature.template.TemplateItemInstance;
import com.kreative.templature.template.ui.swing.SwingComponentFactory;

public abstract class AbstractUITest {
	public abstract String getTitle();
	public abstract TemplateItem createItem();
	
	public final JFrame createFrame() throws IOException {
		final BufferedCountedInputStream in = new BufferedCountedInputStream(System.in);
		final TemplateItemInstance inst = createItem().read(new Closure(null), in);
		final Component comp = inst.createComponent(new SwingComponentFactory());
		
		final JPanel p = new JPanel(new BorderLayout());
		p.add(comp, BorderLayout.PAGE_START);
		p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		final JScrollPane s = new JScrollPane(
			p,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
		);
		
		final JFrame f = new JFrame(getTitle());
		f.setContentPane(s);
		f.setSize(600, 400);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					BufferedCountedOutputStream out = new BufferedCountedOutputStream(System.out);
					inst.write(out);
					out.flush();
					out.close();
				} catch (IOException ee) {
					ee.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		return f;
	}
	
	public final void main() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createFrame().setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
