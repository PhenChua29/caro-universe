package lib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;

import javax.swing.JFrame;

public class JFrameTemplate extends JFrame {

  // FULL
  public void set(int x, int y, int width, int height, String layoutManager, Color color) {
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.getContentPane().setBackground(color);

    Insets insets = this.getInsets();
    int titleBarHeight = insets.top;

    this.setBounds(x, y, width, height + titleBarHeight - 5);
    this.setLocationRelativeTo(null);

    if ("BorderLayout".equals(layoutManager)) {
      this.setLayout(new BorderLayout());
    } else {
      this.setLayout(null);
    }
  }
  // #2

  public void set(int x, int y, int width, int height, Color color) {
    this.setLayout(null);
    this.setBounds(x, y, width, height);
    this.getContentPane().setBackground(color);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }
}
