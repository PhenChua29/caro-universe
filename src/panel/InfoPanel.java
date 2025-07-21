package panel;

import constants.Difficulty;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lib.JButtonTemplate;

import lib.JPanelTemplate;
import object.Bot;
import object.Player;

public class InfoPanel extends JPanelTemplate {

  private PlayerInfoPanel playerInfo;
  private PlayerInfoPanel botInfo;
  private static JLabel label;
  private JButtonTemplate undoButton;
  private static JLabel timerLabel;

  private static Thread timerThread;
  private static volatile boolean stopTimer = false;

  private static final int BASE_COUNT_DOWN_TIME = 60;
  private static int countDownTime;

  public InfoPanel(Player player, Bot bot) {
    countDownTime = BASE_COUNT_DOWN_TIME;

    playerInfo = new PlayerInfoPanel(player, false);
    botInfo = new PlayerInfoPanel(bot, true);

    undoButton = new JButtonTemplate();
    undoButton.setIcon(new ImageIcon(getClass().getResource("/img/button/restore48.png")));
    undoButton.setFocusPainted(false);
    undoButton.addActionListener((ActionEvent e) -> GamePanel.undoLastMove());
    undoButton.setStyling(false);
    undoButton.setPreferredSize(new Dimension(48, 53));

    JPanel buttonWrapper = new JPanel();
    buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.Y_AXIS));
    buttonWrapper.setOpaque(false);
    buttonWrapper.add(Box.createVerticalGlue());
    buttonWrapper.add(undoButton);
    buttonWrapper.add(Box.createVerticalGlue());
    buttonWrapper.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

    label = new JLabel("");
    label.setFont(new Font("Ink Free", Font.BOLD, 52));
    label.setForeground(Color.white);
    label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    timerLabel = new JLabel("");
    timerLabel.setFont(new Font("Arial", Font.PLAIN, 24));
    timerLabel.setForeground(Color.WHITE);
    timerLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
    labelPanel.setOpaque(false);
    labelPanel.add(label);
    labelPanel.add(timerLabel);
    labelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 52));

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.setOpaque(false);
    centerPanel.add(buttonWrapper, BorderLayout.WEST);
    centerPanel.add(labelPanel, BorderLayout.CENTER);

    set(0, 0, 800, 100, Color.red);
    setLayout(new BorderLayout());
    setOpaque(false);

    add(playerInfo, BorderLayout.WEST);
    add(botInfo, BorderLayout.EAST);
    add(centerPanel, BorderLayout.CENTER);

    startTimerThread();
  }

  public static void setCountDownTime(Difficulty difficulty) {
    switch (difficulty) {
      case EASY:
	countDownTime = BASE_COUNT_DOWN_TIME;
	break;
      case MEDIUM:
	countDownTime = BASE_COUNT_DOWN_TIME - BASE_COUNT_DOWN_TIME / 3;
	break;
      case HARD:
	countDownTime = BASE_COUNT_DOWN_TIME / 3;
    }
  }

  public void update() {
    playerInfo.update();
    botInfo.update();
  }

  public static void setLabelText(String text) {
    label.setText(text);
  }

  private static void startTimerThread() {
    stopTimer = false;
    timerThread = new Thread(() -> {
      try {
	Thread.sleep(300);
      } catch (InterruptedException e) {
	return;
      }

      if (stopTimer) {
	return;
      }

      SwingUtilities.invokeLater(() -> GamePanel.timerStartHandler());

      int seconds = countDownTime;
      timerLabel.setVisible(true);

      while (seconds >= 0 && !stopTimer) {
	final int currentSec = seconds;

	SwingUtilities.invokeLater(() -> {
	  timerLabel.setText("Timer: " + currentSec + "s");
	  timerLabel.setForeground(currentSec <= 20 ? Color.RED : Color.WHITE);
	});

	if (seconds == 0) {
	  SwingUtilities.invokeLater(() -> GamePanel.makeRandomMove());
	  break;
	}

	try {
	  Thread.sleep(1000);
	} catch (InterruptedException e) {
	  return;
	}

	seconds--;
      }
    });
    timerThread.start();
  }

  public static void resetTimerThread(boolean isToogleVisibility) {
    stopTimer = true;
    timerLabel.setVisible(!isToogleVisibility);

    if (timerThread != null && timerThread.isAlive()) {
      timerThread.interrupt();
    }

    startTimerThread();
  }
}
