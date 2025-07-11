package lib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class JPanelWLabel extends JPanelTemplate {

	public static JLabel label;

	public void set(int x, int y, int width, int height, String text, Color textColor) {
		this.set(x, y, width, height, Color.YELLOW);
		this.setLayout(new BorderLayout());

		label = new JLabel(text, SwingConstants.CENTER);
		label.setSize(new Dimension(width, height));
		label.setFont(new Font("Ink Free", Font.BOLD, 75));
		label.setForeground(textColor);
		label.setVisible(true);

		this.add(label, BorderLayout.CENTER);
	}

	public void setBackground(ImageIcon background) {
		label.setIcon(background);

	}

	public void setText(String text) {
		label.setText(text);
	}
}
