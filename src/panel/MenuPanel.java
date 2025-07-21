package panel;

import frame.frame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import lib.JButtonTemplate;
import lib.JPanelTemplate;

public class MenuPanel extends JPanelTemplate implements ActionListener {

  private JButtonTemplate newGameButton;
  private JButtonTemplate creditButton;
  private JButtonTemplate quitButton;

  public MenuPanel() {
    this.set(0, 0, 800, 800, "null", Color.RED);
    System.out.println(this.getWidth() + "," + this.getHeight());

    newGameButton = new JButtonTemplate();
    newGameButton.setIcon(new ImageIcon(getClass().getResource("/img/button/blueLr.png")));
    newGameButton.set(-80, 100, 400, 100, "   New game", Color.BLACK, Color.WHITE);
    newGameButton.setFont(new Font("Ink Free", Font.PLAIN, 52));
    newGameButton.setHorizontalTextPosition(SwingConstants.CENTER);
    newGameButton.setStyling(false);

    creditButton = new JButtonTemplate();
    creditButton.set(550, 400, 365, 110, "About", Color.BLACK, Color.WHITE);
    creditButton.setHorizontalAlignment(SwingConstants.LEFT);
    creditButton.setStyling(false);

    quitButton = new JButtonTemplate();

    quitButton.setImageBackground(new ImageIcon(getClass().getResource("/img/button/blueLr.png")).getImage());
    quitButton.set(600, 585, 400, 100, "   Quit", Color.BLACK, Color.WHITE);
    quitButton.setFont(new Font("Ink Free", Font.PLAIN, 48));
    quitButton.setHorizontalAlignment(SwingConstants.LEFT);
    quitButton.setStyling(false);

    newGameButton.addActionListener(this);
    creditButton.addActionListener(this);
    quitButton.addActionListener(this);

    this.add(newGameButton);
//    this.add(creditButton);
    this.add(quitButton);

    JLabel bg = new JLabel();
    bg.setBounds(0, 0, 800, 800);
    bg.setIcon(new ImageIcon(getClass().getResource("/img/bg/1.png")));
    this.add(bg);

    init_cloud();
    animate_cloud();
  }

  private JLabel[] e;

  private void init_cloud() {
    e = new JLabel[5];
    e[0] = new JLabel();
    e[0].setBounds(60, 200, 210, 110);
    e[0].setIcon(new ImageIcon(getClass().getResource("/img/elements/c1.png")));
    this.add(e[0]);
    this.setComponentZOrder(e[0], 0);

    e[1] = new JLabel();
    e[1].setBounds(-800, 100, 210, 110);
    e[1].setIcon(new ImageIcon(getClass().getResource("/img/elements/c2.png")));
    this.add(e[1]);
    this.setComponentZOrder(e[1], 0);

    e[2] = new JLabel();
    e[2].setBounds(150, 448, 84, 50);
    e[2].setIcon(new ImageIcon(getClass().getResource("/img/elements/c3.png")));
    this.add(e[2]);
    this.setComponentZOrder(e[2], 0);

    e[3] = new JLabel();
    e[3].setBounds(315, 0, 400, 400);
    e[3].setIcon(new ImageIcon(getClass().getResource("/img/elements/sun.png")));
    this.add(e[3]);
    this.setComponentZOrder(e[3], 3);

    e[4] = new JLabel();
    e[4].setBounds(128, 440, 235, 129);
    e[4].setIcon(new ImageIcon(getClass().getResource("/img/elements/mars.png")));
    this.add(e[4]);
    this.setComponentZOrder(e[4], 3);
  }

  private Thread cloudThread;

  private void animate_cloud() {

    cloudThread = new Thread() {
      public void run() {

	while (true) {
	  if (e[0].getX() == 800) {
	    e[0].setBounds(0 - e[0].getWidth(), e[0].getY(), e[0].getWidth(),
		    e[0].getHeight());
	  }
	  if (e[0].getX() != 800) {
	    e[0].setBounds(e[0].getX() + 1, e[0].getY(), e[0].getWidth(),
		    e[0].getHeight());
	  }

	  if (e[1].getX() == 800) {
	    e[1].setBounds(0 - e[1].getWidth(), e[1].getY(), e[1].getWidth(),
		    e[1].getHeight());
	  }
	  if (e[1].getX() != 800) {
	    e[1].setBounds(e[1].getX() + 2, e[1].getY(), e[1].getWidth(),
		    e[1].getHeight());
	  }

	  if (e[2].getX() == -e[2].getWidth()) {
	    e[2].setBounds(800 + e[2].getWidth(), e[2].getY(), e[2].getWidth(),
		    e[2].getHeight());
	  }
	  if (e[2].getX() != -e[2].getWidth()) {
	    e[2].setBounds(e[2].getX() - 1, e[2].getY(), e[2].getWidth(),
		    e[2].getHeight());
	  }

	  try {
	    Thread.sleep(60);
	  } catch (InterruptedException ex) {
	  }
	}
      }
    };
    cloudThread.start();

    Thread planetThread = new Thread() {
      public void run() {
	double pichia = Math.PI / 5;
	double alpha = Math.PI * 3 / 2;

	while (true) {

	  double y = e[3].getY();
	  y = Math.cos(alpha) >= 0 ? y + Math.ceil(Math.cos(alpha + pichia))
		  : y + Math.floor(Math.cos(alpha + pichia));
	  alpha += pichia;
	  e[3].setBounds(e[3].getX(), (int) y, e[3].getWidth(),
		  e[3].getHeight());

	  try {
	    Thread.sleep(130);
	  } catch (InterruptedException ex) {
	  }
	}
      }
    };
    planetThread.start();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == newGameButton) {
      frame.setInfo_prompt_trigger(true);
    } else if (e.getSource() == creditButton) {
      frame.setCredit_trigger(true);
    } else if (e.getSource() == quitButton) {
      frame.setQuitGame_trigger(true);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponents(g);
  }
}
