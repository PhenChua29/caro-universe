package panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class LoadingPanel extends JPanel {

  private static final int BAR_WIDTH = 500;
  private static final int BAR_HEIGHT = 30;
  private static final int INDICATOR_WIDTH = 50;
  public static final int LOADING_TIME = 1500;

  private BufferedImage backgroundImg;
  private BufferedImage indicatorImg;
  private int indicatorX = 0;
  private Timer animationTimer;

  public LoadingPanel() {
    setLayout(null);
    setBackground(Color.BLACK);

    try {
      backgroundImg = ImageIO.read(getClass().getResource("/img/bg/prompt.png"));
      indicatorImg = ImageIO.read(getClass().getResource("/img/elements/sun.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    JLabel loadingLabel = new JLabel("Loading...");
    loadingLabel.setForeground(Color.WHITE);
    loadingLabel.setFont(new Font("Ink Free", Font.BOLD, 36));
    loadingLabel.setSize(300, 40);
    loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
    loadingLabel.setVerticalAlignment(SwingConstants.CENTER);
    loadingLabel.setLocation((800 - loadingLabel.getWidth()) / 2, 250);
    add(loadingLabel);

    startAnimation();
  }

  private void startAnimation() {
    final int LIM_X = BAR_WIDTH - INDICATOR_WIDTH + 24;
    final int DX = LIM_X / 100;

    animationTimer = new Timer(LOADING_TIME / 100, e -> {
      indicatorX += DX * 2;
      if (indicatorX >= LIM_X) {
	indicatorX = LIM_X;
	animationTimer.stop();
      }
      repaint();
    });
    animationTimer.start();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    if (backgroundImg != null) {
      g2d.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), null);
    }

    int barX = (getWidth() - BAR_WIDTH) / 2;
    int barY = getHeight() / 2;

    // Draw base of the loading bar
    g2d.setColor(new Color(255, 255, 255, 50));
    g2d.fillRoundRect(barX, barY, BAR_WIDTH, BAR_HEIGHT, 30, 30);

    // Draw progress trail
    g2d.setColor(Color.BLACK);
    g2d.fillRoundRect(barX, barY, indicatorX + INDICATOR_WIDTH / 2, BAR_HEIGHT, 30, 30);

    // Draw the moving indicator image
    if (indicatorImg != null) {
      g2d.drawImage(indicatorImg, barX + indicatorX, barY - 10, INDICATOR_WIDTH, INDICATOR_WIDTH, null);
    }
  }
}
