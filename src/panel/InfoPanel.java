package panel;

import constants.Difficulty;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
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

  private static Timer swingTimer;
  private static int seconds;
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

    if (swingTimer != null && swingTimer.isRunning()) {
      swingTimer.stop();
    }

    GamePanel.timerStartHandler();
    timerLbl.setVisible(true);

    seconds = countDownTime;

    swingTimer = new Timer(1000, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
	if (stopTimer) {
	  swingTimer.stop();
	  return;
	}

	timerLbl.setText("Timer: " + seconds + "s");
	timerLbl.setForeground(seconds <= 20 ? Color.RED : Color.WHITE);

	if (seconds == 0) {
	  swingTimer.stop();
	  GamePanel.makeRandomMove();
	}

	seconds--;
      }
    });

    swingTimer.setInitialDelay(0);
    swingTimer.start();
  }

  public static void resetTimerThread(boolean isToggleVisibility) {
    stopTimer = true;
    timerLbl.setVisible(!isToggleVisibility);

    if (swingTimer != null && swingTimer.isRunning()) {
      swingTimer.stop();
    }

    startTimerThread();
  }
}
