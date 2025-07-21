package panel;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import lib.JPanelTemplate;
import object.Bot;
import object.Player;

/**
 * A panel that contains the main game's interface.
 */
public class InGamePanel extends JPanel {

  private final GamePanel gamePanel;
  private static InfoPanel infoPanel;

  public InGamePanel(final Player player, final Bot bot) {
    super();

    gamePanel = new GamePanel(player, bot);
    infoPanel = new InfoPanel(player, bot);

    setBounds(0, 0, 800, 800);
    setLayout(null);

    add(infoPanel);
    add(gamePanel);
    initBg();
  }

  private void initBg() {
    JPanelTemplate bgPanel = new JPanelTemplate();
    bgPanel.set(0, 0, 800, 800, "null", new Color(6, 5, 55));
    bgPanel.setOpaque(true);

    JLabel imgBg = new JLabel();
    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/bg/b5r.png"));
    imgBg.setIcon(imageIcon);
    imgBg.setBounds(0, 100, 800, 700);

    bgPanel.add(imgBg, BorderLayout.CENTER);
    this.add(bgPanel);
    this.setComponentZOrder(bgPanel, this.getComponentCount() - 1);
  }

  public static void update() {
    infoPanel.update();
  }

  public static void initNewGame() {
    GamePanel.initNewGame();
    InfoPanel.resetTimerThread(true);
  }

  public static void nextMatch() {
    GamePanel.nextMatch();
    InfoPanel.resetTimerThread(true);
  }
}
