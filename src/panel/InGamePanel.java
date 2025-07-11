package panel;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import players.Bot;
import players.Player;

/**
 * A panel that contains the main game's interface.
 */
public class InGamePanel extends JPanel {
  private GamePanel gamePanel;
  private InfoPanel infoPanel;

  public InGamePanel(final Player player, final Bot bot) {
    super();

    gamePanel = new GamePanel(player, bot);
    infoPanel = new InfoPanel(player, bot);

    this.setBounds(0, 0, 800, 800);
    this.setLayout(null);

    this.add(infoPanel);
    this.add(gamePanel);
    initBg();
  }

  private void initBg() {
    JPanel bgPanel = new JPanel();
    bgPanel.setBounds(0, 0, 800, 800);

    JLabel imgBg = new JLabel();
    ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/4.png"));
    Image image = imageIcon.getImage();
    image = image.getScaledInstance(800, 800, Image.SCALE_SMOOTH);

    imgBg.setIcon(new ImageIcon(image));
    imgBg.setBounds(0, 0, 800, 800);

    bgPanel.add(imgBg);
    this.add(bgPanel);
    this.setComponentZOrder(bgPanel, this.getComponentCount() - 1);
  }

  public void update() { infoPanel.update(); }
  public void initNewGame() { GamePanel.initNewGame(); }
}
