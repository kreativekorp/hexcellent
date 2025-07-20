package com.kreative.templature.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.kreative.templature.template.HexDataModel;
import com.kreative.templature.template.ui.swing.HexAreaFactory;
import com.kreative.templature.template.ui.swing.JHexAreaFactory;

public class JHexAreaTest {
	private static byte[] data;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					ByteArrayOutputStream bs = new ByteArrayOutputStream();
					byte[] buf = new byte[65536]; int n;
					while ((n = System.in.read(buf)) >= 0) bs.write(buf, 0, n);
					System.in.close();
					data = bs.toByteArray();
					
					final HexAreaFactory cf = new JHexAreaFactory();
					final Component a = cf.createHexAreaComponent(new HexDataModel() {
						public byte[] getHexValue() { return data; }
						public String getHexEncoding() { return "Windows-1252"; }
						public boolean getHexLittleEndian() { return true; }
						public void setHexValue(byte[] b) { data = b; }
					});
					
					final JPanel p = new JPanel(new BorderLayout());
					p.add(a, BorderLayout.CENTER);
					
					final JFrame f = new JFrame("JHexArea Test");
					f.setContentPane(p);
					f.pack();
					f.setLocationRelativeTo(null);
					f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					f.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							if (data == null) return;
							try {
								System.out.write(data);
								System.out.flush();
								System.out.close();
							} catch (IOException ee) {
								ee.printStackTrace();
							}
							System.exit(0);
						}
					});
					
					f.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
