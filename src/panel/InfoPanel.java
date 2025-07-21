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
  private static JLabel mainLbl;
  private JButtonTemplate undoBtn;
  private static JLabel timerLbl;

  private static Thread timerThread;
  private static volatile boolean stopTimer = false;

  private static final int BASE_COUNT_DOWN_TIME = 60;
  private static int countDownTime;

  public InfoPanel(Player player, Bot bot) {
    countDownTime = BASE_COUNT_DOWN_TIME;

    playerInfo = new PlayerInfoPanel(player, false);
    botInfo = new PlayerInfoPanel(bot, true);

    undoBtn = new JButtonTemplate();
    undoBtn.setIcon(new ImageIcon(getClass().getResource("/img/button/restore48.png")));
    undoBtn.setFocusPainted(false);
    undoBtn.addActionListener((ActionEvent e) -> GamePanel.undoLastMove());
    undoBtn.setStyling(false);
    undoBtn.setPreferredSize(new Dimension(48, 53));

    JPanel buttonWrapper = new JPanel();
    buttonWrapper.setLayout(new BoxLayout(buttonWrapper, BoxLayout.Y_AXIS));
    buttonWrapper.setOpaque(false);
    buttonWrapper.add(Box.createVerticalGlue());
    buttonWrapper.add(undoBtn);
    buttonWrapper.add(Box.createVerticalGlue());
    buttonWrapper.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

    mainLbl = new JLabel("");
    mainLbl.setFont(new Font("Ink Free", Font.BOLD, 52));
    mainLbl.setForeground(Color.white);
    mainLbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    timerLbl = new JLabel("");
    timerLbl.setFont(new Font("Arial", Font.PLAIN, 24));
    timerLbl.setForeground(Color.WHITE);
    timerLbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    JPanel labelPanel = new JPanel();
    labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
    labelPanel.setOpaque(false);
    labelPanel.add(mainLbl);
    labelPanel.add(timerLbl);
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
    mainLbl.setText(text);
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
      timerLbl.setVisible(true);

      while (seconds >= 0 && !stopTimer) {
	final int currentSec = seconds;

	SwingUtilities.invokeLater(() -> {
	  timerLbl.setText("Timer: " + currentSec + "s");
	  timerLbl.setForeground(currentSec <= 20 ? Color.RED : Color.WHITE);
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
    timerLbl.setVisible(!isToogleVisibility);

    if (timerThread != null && timerThread.isAlive()) {
      timerThread.interrupt();
    }

    startTimerThread();
  }
}
