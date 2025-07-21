package panel;

import frame.Frame;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import lib.JButtonTemplate;
import lib.JPanelTemplate;

public class MenuPanel extends JPanelTemplate implements ActionListener {

  private JButtonTemplate newGameBtn;
  private JButtonTemplate howToPlayBtn;
  private JButtonTemplate aboutBtn;
  private JButtonTemplate quitBtn;
  private JButtonTemplate leaderboardBtn;

  public MenuPanel() {
    this.set(0, 0, 800, 800, "null", Color.RED);
    System.out.println(this.getWidth() + "," + this.getHeight());

    newGameBtn = new JButtonTemplate();
    newGameBtn.setIcon(new ImageIcon(getClass().getResource("/img/button/blueLr.png")));
    newGameBtn.set(-80, 100, 400, 100, "   New game", Color.BLACK, Color.WHITE);
    newGameBtn.setFont(new Font("Ink Free", Font.PLAIN, 52));
    newGameBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    newGameBtn.setStyling(false);

    leaderboardBtn = new JButtonTemplate();
    leaderboardBtn.setImageBackground(new ImageIcon(getClass().getResource("/img/button/blueLr.png")).getImage());
    leaderboardBtn.set(-40, 250, 390, 100, "   Leaderboard", Color.BLACK, Color.WHITE);
    leaderboardBtn.setFont(new Font("Ink Free", Font.PLAIN, 48));
    leaderboardBtn.setHorizontalAlignment(SwingConstants.LEFT);
    leaderboardBtn.setStyling(false);

    howToPlayBtn = new JButtonTemplate();
    howToPlayBtn.setImageBackground(new ImageIcon(getClass().getResource("/img/button/blueLr.png")).getImage());
    howToPlayBtn.set(450, 310, 400, 110, "  How to Play", Color.BLACK, Color.WHITE);
    howToPlayBtn.setFont(new Font("Ink Free", Font.PLAIN, 48));
    howToPlayBtn.setHorizontalAlignment(SwingConstants.LEFT);
    howToPlayBtn.setStyling(false);

    aboutBtn = new JButtonTemplate();
    aboutBtn.setImageBackground(new ImageIcon(getClass().getResource("/img/button/blueLr.png")).getImage());
    aboutBtn.set(540, 460, 400, 110, "  About Us", Color.BLACK, Color.WHITE);
    aboutBtn.setFont(new Font("Ink Free", Font.PLAIN, 48));
    aboutBtn.setHorizontalAlignment(SwingConstants.LEFT);
    aboutBtn.setStyling(false);

    quitBtn = new JButtonTemplate();
    quitBtn.setImageBackground(new ImageIcon(getClass().getResource("/img/button/blueLr.png")).getImage());
    quitBtn.set(600, 610, 400, 100, "   Quit", Color.BLACK, Color.WHITE);
    quitBtn.setFont(new Font("Ink Free", Font.PLAIN, 48));
    quitBtn.setHorizontalAlignment(SwingConstants.LEFT);
    quitBtn.setStyling(false);

    newGameBtn.addActionListener(this);
    leaderboardBtn.addActionListener(this);
    howToPlayBtn.addActionListener(this);
    aboutBtn.addActionListener(this);
    quitBtn.addActionListener(this);

    this.add(newGameBtn);
    this.add(leaderboardBtn);
    this.add(howToPlayBtn);
    this.add(aboutBtn);
    this.add(quitBtn);

    JLabel bg = new JLabel();
    bg.setBounds(0, 0, 800, 800);
    bg.setIcon(new ImageIcon(getClass().getResource("/img/bg/1.png")));
    this.add(bg);

    init_cloud();
    runAnimations();
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

  private void runAnimations() {
    Timer clouds = new Timer(60, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
        moveCloud(e[0], 1, 800);
        moveCloud(e[1], 2, 800);
        moveCloud(e[2], -1, -e[2].getWidth());
      }
    });

    clouds.start();

    Timer planets = new Timer(130, new ActionListener() {
      double pichia = Math.PI / 5;
      double alpha = Math.PI * 3 / 2;

      @Override
      public void actionPerformed(ActionEvent evt) {
        double y = e[3].getY();
        y = Math.cos(alpha) >= 0 ? y + Math.ceil(Math.cos(alpha + pichia))
            : y + Math.floor(Math.cos(alpha + pichia));
        alpha += pichia;
        e[3].setBounds(e[3].getX(), (int) y, e[3].getWidth(), e[3].getHeight());
      }
    });

    planets.start();
  }

  private void moveCloud(JLabel cloud, int dx, int resetX) {
    int x = cloud.getX();
    x += dx;

    if ((dx > 0 && x >= resetX)) {
      x = -cloud.getWidth();
    } else if (dx < 0 && x <= resetX) {
      x = Frame.WIDTH + cloud.getWidth();
    }

    cloud.setBounds(x, cloud.getY(), cloud.getWidth(), cloud.getHeight());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == newGameBtn) {
      Frame.setInfo_prompt_trigger(true);
    } else if (e.getSource() == leaderboardBtn) {
      Frame.setLeaderboard_trigger(true);
    } else if (e.getSource() == aboutBtn) {
      Frame.setAboutUs_trigger(true);
    } else if (e.getSource() == quitBtn) {
      Frame.setQuitGame_trigger(true);
    } else if (e.getSource() == howToPlayBtn) {
      Frame.setInstruction_trigger(true);
    }
  }
}
