package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import frame.Frame;

public class CreditPanel extends JPanel implements ActionListener {

  private JButton returnBtn;

  public CreditPanel() {
    init();
    init_button();
    init_background();
  }

  private void init() {
    this.setLayout(null);
    this.setVisible(true);
    this.setBackground(Color.BLACK);
    this.setOpaque(true);
    this.setBounds(0, 0, 800, 800);
  }

  private void init_button() {
    returnBtn = new JButton();
    returnBtn.setForeground(Color.BLACK);
    returnBtn.addActionListener(this);
    returnBtn.setBounds(30, 22, 60, 40);
    returnBtn.setFocusable(false);
    returnBtn.setFont(new Font("Ink Free", Font.BOLD, 75));

    returnBtn.setBackground(Color.red);
    returnBtn.setOpaque(false);
    returnBtn.setFocusable(false);
    returnBtn.setFocusPainted(false);
    returnBtn.setBorderPainted(false);
    returnBtn.setContentAreaFilled(false);

    this.add(returnBtn, BorderLayout.NORTH);
  }

  private void init_background() {
    JLabel imgBg = new JLabel();
    imgBg.setIcon(new ImageIcon(getClass().getResource("/img/bg/3.png")));
    imgBg.setBounds(0, 0, 800, 800);
    this.add(imgBg);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Frame.setMenu_trigger(true);
  }
}
