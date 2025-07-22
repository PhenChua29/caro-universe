package frame;

import constants.PanelType;
import lib.JFrameTemplate;
import object.Bot;
import object.Player;

import java.awt.Color;
import java.awt.Toolkit;

import panel.AboutUsPanel;
import panel.EndGamePanel;
import panel.InGamePanel;
import panel.InfoPromptPanel;
import panel.InstructionPanel;
import panel.LeaderboardPanel;
import panel.MenuPanel;
import panel.LoadingPanel;

public class Frame extends JFrameTemplate {

  public static final int WIDTH = 800;
  private static Frame instance;

  private MenuPanel menuPanel;
  private InGamePanel inGamePanel;
  private EndGamePanel endGamePanel;
  private InstructionPanel instructionPanel;
  private AboutUsPanel aboutUsPanel;
  private InfoPromptPanel infoPromptPanel;
  private LeaderboardPanel leaderboardPanel;
  private LoadingPanel loadingPanel;

  private Player currentPlayer;
  private Bot bot;

  private PanelType currentPanel = null;

  public Frame() {
    instance = this;

    currentPlayer = new Player();
    bot = new Bot();
    bot.loadRandomMoveExcept(currentPlayer.loadRandomMove());

    set(0, 0, WIDTH, 800, "BorderLayout", Color.BLACK);
    setResizable(false);
    setTitle("Caro Universe");
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/elements/ico.png")));
    
    initLoadingScreen();
  }

  private void initLoadingScreen() {
    // Show loading screen briefly at startup
    loadingPanel = new LoadingPanel();
    setContentPane(loadingPanel);
    repaint();
    validate();

    javax.swing.Timer startupDelay = new javax.swing.Timer(LoadingPanel.LOADING_TIME, e -> {
      initPanels();
      switchPanel(PanelType.MENU);
    });
    startupDelay.setRepeats(false);
    startupDelay.start();
  }

  private void initPanels() {
    menuPanel = new MenuPanel();
    infoPromptPanel = new InfoPromptPanel(currentPlayer, bot);
    inGamePanel = new InGamePanel(currentPlayer, bot);
    endGamePanel = new EndGamePanel();
    instructionPanel = new InstructionPanel();
    aboutUsPanel = new AboutUsPanel();
    leaderboardPanel = new LeaderboardPanel();

    add(menuPanel);
    add(infoPromptPanel);
    add(inGamePanel);
    add(endGamePanel);
    add(instructionPanel);
    add(aboutUsPanel);
    add(leaderboardPanel);
  }

  public static void switchPanel(PanelType panel) {
    if (instance != null) {
      instance._switchPanel(panel);
    }
  }

  private void _switchPanel(PanelType panel) {
    if (panel == currentPanel) {
      return;
    }

    switch (panel) {
      case MENU:
	setContentPane(menuPanel);
	break;
      case INFO_PROMPT:
	infoPromptPanel.reset();
	setContentPane(infoPromptPanel);
	break;
      case IN_GAME:
	inGamePanel.update();
	setContentPane(inGamePanel);
	break;
      case END_GAME:
	setContentPane(endGamePanel);
	break;
      case INSTRUCTION:
	setContentPane(instructionPanel);
	break;
      case ABOUT_US:
	setContentPane(aboutUsPanel);
	break;
      case LEADERBOARD:
	setContentPane(leaderboardPanel);
	break;
      case QUIT:
	dispose();
	System.exit(0);
	return;
    }

    currentPanel = panel;
    repaint();
    validate();
  }
}
