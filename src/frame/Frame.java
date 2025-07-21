package frame;

import java.awt.Color;
import java.awt.Toolkit;
import lib.JFrameTemplate;
import panel.EndGamePanel;
import panel.InGamePanel;
import panel.InfoPromptPanel;
import panel.MenuPanel;
import panel.AboutUsPanel;
import panel.InstructionPanel;
import panel.LeaderboardPanel;
import object.Bot;
import object.Player;

public class Frame extends JFrameTemplate {

  public static final int WIDTH = 800;

  public static final String menuPanel = "MenuPanel";
  public static final String inGamePanel = "InGamePanel";
  public static final String endGamePanel = "EndGamePanel";
  public static final String instructionPanel = "InstructionPanel";
  public static final String aboutUsPanel = "AboutUsPanel";
  public static final String infoPromptPanel = "InfoPromptPanel";
  public static final String leaderboardPanel = "LeaderboardPanel";

  private static boolean newGame_trigger = false;
  private static boolean quitGame_trigger = false;
  private static boolean instruction_trigger = false;
  private static boolean aboutUs_trigger = false;
  private static boolean endGame_trigger = false;
  private static boolean menu_trigger = false;
  private static boolean info_prompt_trigger = false;
  private static boolean leaderboard_trigger = false;

  private MenuPanel MenuPanel;
  private InGamePanel InGamePanel;
  private EndGamePanel EndGamePanel;
  private InstructionPanel InstructionPanel;
  private AboutUsPanel AboutUsPanel;
  private InfoPromptPanel InfoPromptPanel;
  private LeaderboardPanel LeaderboardPanel;

  private Player currentPlayer;
  private Bot bot;

  public Frame() {
    currentPlayer = new Player();
    bot = new Bot();
    bot.loadRandomMoveExcept(currentPlayer.loadRandomMove());

    set(0, 0, WIDTH, 800, "BorderLayout", Color.BLACK);
    setResizable(false);
    setTitle("Caro Universe");
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/elements/ico.png")));
    initPanels();
  }

  private void initPanels() {
    MenuPanel = new MenuPanel();
    this.add(MenuPanel);

    InfoPromptPanel = new InfoPromptPanel(currentPlayer, bot);
    this.add(InfoPromptPanel);

    InGamePanel = new InGamePanel(currentPlayer, bot);
    this.add(InGamePanel);

    EndGamePanel = new EndGamePanel();
    this.add(EndGamePanel);

    InstructionPanel = new InstructionPanel();
    this.add(InstructionPanel);

    AboutUsPanel = new AboutUsPanel();
    this.add(AboutUsPanel);

    LeaderboardPanel = new LeaderboardPanel();
    this.add(LeaderboardPanel);

    switchPanel(menuPanel);
  }

  public void switchPanel() {
    if (newGame_trigger) {
      InGamePanel.update();
      System.out.println("Switched into " + inGamePanel);
      switchPanel(inGamePanel);
      setNewGame_trigger(false);

      try {
	Thread.sleep(1000);
      } catch (Exception e) {
	System.out.println(e);
	e.printStackTrace();
      }
      return;
    }

    if (info_prompt_trigger) {
      InfoPromptPanel.reset();
      switchPanel(infoPromptPanel);
      System.out.println("Switched into " + infoPromptPanel);
      setInfo_prompt_trigger(false);
      return;
    }

    if (instruction_trigger) {
      System.out.println("Switched into " + instructionPanel);
      switchPanel(instructionPanel);
      setInstruction_trigger(false);
      return;
    }

    if (aboutUs_trigger) {
      switchPanel(aboutUsPanel);
      System.out.println("Switched into " + aboutUsPanel);
      setAboutUs_trigger(false);
      return;
    }

    if (endGame_trigger) {
      System.out.println("Switched into " + endGamePanel);
      switchPanel(endGamePanel);
      setEndGame_trigger(false);
      return;
    }

    if (leaderboard_trigger) {
      System.out.println("Switched into " + leaderboardPanel);
      switchPanel(leaderboardPanel);
      setLeaderboard_trigger(false);
      return;
    }

    if (menu_trigger) {
      System.out.println("Switched into " + menuPanel);
      switchPanel(menuPanel);
      setMenu_trigger(false);
      return;
    }

    if (quitGame_trigger) {
      System.out.println("Quitted Game");
      setQuitGame_trigger(false);
      this.dispose();
      System.exit(0);
    }
  }

  private void switchPanel(String panel) {
    System.out.println(panel);

    if (panel.equals(menuPanel)) {
      setContentPane(MenuPanel);
    } else if (panel.equals(infoPromptPanel)) {
      setContentPane(InfoPromptPanel);
    } else if (panel.equals(inGamePanel)) {
      setContentPane(InGamePanel);
    } else if (panel.equals(endGamePanel)) {
      setContentPane(EndGamePanel);
    } else if (panel.equals(aboutUsPanel)) {
      setContentPane(AboutUsPanel);
    } else if (panel.equals(instructionPanel)) {
      setContentPane(InstructionPanel);
    } else if (panel.equals(leaderboardPanel)) {
      setContentPane(LeaderboardPanel);
    }

    repaint();
    validate();
  }

  public static boolean isStateChange() {
    return (newGame_trigger || info_prompt_trigger || quitGame_trigger
	    || aboutUs_trigger || endGame_trigger || menu_trigger || instruction_trigger || leaderboard_trigger);
  }

  public static boolean isNewGame_trigger() {
    return newGame_trigger;
  }

  public static void setNewGame_trigger(boolean newGame_state) {
    newGame_trigger = newGame_state;
  }

  public static boolean isQuitGame_trigger() {
    return quitGame_trigger;
  }

  public static void setQuitGame_trigger(boolean QuitGame_trigger) {
    quitGame_trigger = QuitGame_trigger;
  }

  public static boolean isInstruction_trigger() {
    return instruction_trigger;
  }

  public static void setInstruction_trigger(boolean instruction_trigger_state) {
    instruction_trigger = instruction_trigger_state;
  }

  public static boolean isAboutUs_trigger() {
    return aboutUs_trigger;
  }

  public static void setAboutUs_trigger(boolean AboutUs_trigger) {
    aboutUs_trigger = AboutUs_trigger;
  }

  public static boolean isEndGame_trigger() {
    return endGame_trigger;
  }

  public static void setEndGame_trigger(boolean EndGame_trigger) {
    endGame_trigger = EndGame_trigger;
  }

  public static boolean isMenu_trigger() {
    return menu_trigger;
  }

  public static void setMenu_trigger(boolean Menu_trigger) {
    menu_trigger = Menu_trigger;
  }

  public static void setInfo_prompt_trigger(boolean Info_prompt_trigger) {
    info_prompt_trigger = Info_prompt_trigger;
  }

  public static boolean isLeaderboard_trigger() {
    return leaderboard_trigger;
  }

  public static void setLeaderboard_trigger(boolean Leaderboard_trigger) {
    leaderboard_trigger = Leaderboard_trigger;
  }
}
