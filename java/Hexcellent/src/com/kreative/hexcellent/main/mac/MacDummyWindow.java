package com.kreative.hexcellent.main.mac;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import com.kreative.hexcellent.main.FileMenu;

public class MacDummyWindow extends JFrame {
	public static final long serialVersionUID = 1L;
	
	public MacDummyWindow() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new FileMenu.NewMenuItem());
		fileMenu.add(new FileMenu.OpenMenuItem());
		JMenuBar mb = new JMenuBar();
		mb.add(fileMenu);
		setJMenuBar(mb);
		
		setUndecorated(true);
		setResizable(false);
		setMinimumSize(new Dimension(0,0));
		setPreferredSize(new Dimension(0,0));
		setMaximumSize(new Dimension(0,0));
		setSize(new Dimension(0,0));
		setLocation(-1000000, -1000000);
		setVisible(true);
	}
}
