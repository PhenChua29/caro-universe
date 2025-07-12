package panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import frame.frame;

public class EndGamePanel extends JPanel implements ActionListener {

  public static enum BTN_MODE {
    NEXT_MATCH,
    NEW_MATCH
  }

  private static BTN_MODE mode;
  private static JButton newGameBtn;
  private static JButton nextMatchBtn;
  private JButton homeBtn;
  private static JLabel textField;

  public EndGamePanel() {
    init();
    init_button();
    init_tilte();
    init_background();
  }

  private void init() {
    this.setLayout(null);
    this.setVisible(true);
    this.setBackground(Color.BLACK);
    this.setOpaque(true);
    this.setBounds(0, 0, 800, 800);

    mode = BTN_MODE.NEW_MATCH;
  }

  private void init_button() {
    newGameBtn = new JButton("New game");
    newGameBtn.setBounds(190, 395, 420, 130);
    initButton(newGameBtn, 75);

    nextMatchBtn = new JButton("Next match");
    nextMatchBtn.setBounds(190, 395, 420, 130);
    initButton(nextMatchBtn, 62);

    homeBtn = new JButton();
    homeBtn.setIcon(new ImageIcon(getClass().getResource("/img/home.png")));
    homeBtn.setBounds(700, 680, 66, 71);
    initButton(homeBtn, 75);
  }

  private void init_background() {
    JLabel imgBg = new JLabel();
    imgBg.setIcon(new ImageIcon(getClass().getResource("/img/2.png")));
    imgBg.setBounds(0, 0, 800, 800);
    this.add(imgBg);
  }

  private void init_tilte() {
    textField = new JLabel("", SwingConstants.CENTER);

    textField.setForeground(new Color(131, 206, 255));
    textField.setBounds(0, 200, 800, 150);
    textField.setFont(new Font("Ink Free", Font.BOLD, 52));
    textField.setBackground(new Color(6, 5, 55));
    textField.setOpaque(false);

    this.add(textField);
  }

  private void initButton(JButton b, int fontSize) {
    b.setForeground(Color.WHITE);
    b.setFocusable(false);
    b.setFont(new Font("Ink Free", Font.BOLD, fontSize));
    b.setOpaque(false);
    b.setFocusPainted(false);
    b.setBorderPainted(false);
    b.setContentAreaFilled(false);
    b.addActionListener(this);
    this.add(b);
  }

  public static void setText(String text) {
    textField.setText(text);
  }

  public static void setMode(BTN_MODE newMode) {
    mode = newMode;

    switch (newMode) {
      case NEXT_MATCH:
	newGameBtn.setVisible(false);
	nextMatchBtn.setVisible(true);
	break;
      case NEW_MATCH:
	newGameBtn.setVisible(true);
	nextMatchBtn.setVisible(false);
	break;
      default:
	throw new IllegalArgumentException("Unknown type of button mode");
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(newGameBtn)) {
      GamePanel.initNewGame();
      System.out.println("New game btn pressed");
      frame.setNewGame_trigger(true);
      return;
    }

    if (e.getSource().equals(nextMatchBtn)) {
      GamePanel.nextMatch();
      System.out.println("Next match btn pressed");
      frame.setNewGame_trigger(true);
      return;
    }

    if (e.getSource().equals(homeBtn)) {
      frame.setMenu_trigger(true);
    }
  }
}
