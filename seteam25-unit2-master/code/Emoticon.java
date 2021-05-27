import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;
import java.text.*;

public class Emoticon {
	private JFrame win;
	private int emoticonId;
	public Emoticon( int emoticon_id ) {
		emoticonId = emoticon_id;
		win = new JFrame("Emoticon");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);
		win.setSize(512,512);
		if(emoticonId == 0) {
			win.add(new JLabel(new ImageIcon("happy.jpg")));
		} else {
			win.add(new JLabel(new ImageIcon("sad.png")));
		}
		win.show();
		try {
			Thread.sleep(500);				// pinsetter is where delay will be in a real game
		} catch (Exception e) {}
		win.hide();
	}

}