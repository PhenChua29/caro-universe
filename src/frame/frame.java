package frame;

import java.awt.Color;
import lib.JFrameTemplate;
import panel.CreditPanel;
import panel.EndGamePanel;
import panel.InGamePanel;
import panel.InfoPromptPanel;
import panel.MenuPanel;
import players.Bot;
import players.Player;

public class frame extends JFrameTemplate {

  public static final String menuPanel = "MenuPanel";
  public static final String inGamePanel = "InGamePanel";
  public static final String endGamePanel = "EndGamePanel";
  public static final String creditPanel = "CreditPanel";
  public static final String infoPromptPanel = "InfoPromptPanel";

  private static boolean newGame_trigger = false;
  private static boolean quitGame_trigger = false;
  private static boolean credit_trigger = false;
  private static boolean endGame_trigger = false;
  private static boolean menu_trigger = false;
  private static boolean info_prompt_trigger = false;

  private MenuPanel MenuPanel;
  private InGamePanel InGamePanel;
  private EndGamePanel EndGamePanel;
  private CreditPanel CreditPanel;
  private InfoPromptPanel InfoPromptPanel;

  private Player currentPlayer;
  private Bot bot;

  public frame() {
    currentPlayer = new Player();
    bot = new Bot();

    this.set(0, 0, 800, 800, "BorderLayout", Color.BLACK);
    this.setResizable(false);
    this.setTitle("Tic Tac Toe");
    initPanels();
  }

  private void initPanels() {
    MenuPanel = new MenuPanel();
    this.add(MenuPanel);

    InfoPromptPanel = new InfoPromptPanel(currentPlayer);
    this.add(InfoPromptPanel);

    InGamePanel = new InGamePanel(currentPlayer, bot);
    this.add(InGamePanel);

    EndGamePanel = new EndGamePanel();
    this.add(EndGamePanel);

    CreditPanel = new CreditPanel();
    this.add(CreditPanel);

    switchPanel(menuPanel);
  }

  /**
   * Methods for switching panel depends on states.
   */
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

    if (credit_trigger) {
      switchPanel(creditPanel);
      System.out.println("Switched into " + creditPanel);
      setCredit_trigger(false);
      return;
    }

    if (endGame_trigger) {
      System.out.println("Switched into " + endGamePanel);
      switchPanel(endGamePanel);
      setEndGame_trigger(false);
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

  /**
   * A method for switching panel depends on states: new game, credit, quit, end
   * game
   *
   * @param panel A {@code String} that has the JPanel's name(predefined inside
   * of this class).
   */
  private void switchPanel(String panel) {
    System.out.println(panel);

    if (panel.equals(menuPanel)) {
      this.setContentPane(MenuPanel);
    } else if (panel.equals(infoPromptPanel)) {
      this.setContentPane(InfoPromptPanel);
    } else if (panel.equals(inGamePanel)) {
      this.setContentPane(InGamePanel);
    } else if (panel.equals(endGamePanel)) {
      this.setContentPane(EndGamePanel);
    } else if (panel.equals(creditPanel)) {
      this.setContentPane(CreditPanel);
    }

    this.repaint();
    this.validate();
  }

  /**
   * Switch to another panel if state change
   *
   * @return {@code true} if there is a change in an of the states,
   * {@code false} otherwise
   */
  public static boolean isStateChange() {
    return (newGame_trigger || info_prompt_trigger || quitGame_trigger ||
            credit_trigger || endGame_trigger || menu_trigger);
  }

  // GETTERS & SETTERS
  public static boolean isNewGame_trigger() { return newGame_trigger; }

  public static void setNewGame_trigger(boolean newGame_state) {
    newGame_trigger = newGame_state;
  }

  public static boolean isQuitGame_trigger() { return quitGame_trigger; }

  public static void setQuitGame_trigger(boolean QuitGame_trigger) {
    quitGame_trigger = QuitGame_trigger;
  }

  public static boolean isCredit_trigger() { return credit_trigger; }

  public static void setCredit_trigger(boolean Credit_trigger) {
    credit_trigger = Credit_trigger;
    System.out.println("Credit_trigger set:" + isCredit_trigger());
  }

  public static boolean isEndGame_trigger() { return endGame_trigger; }

  public static void setEndGame_trigger(boolean EndGame_trigger) {
    endGame_trigger = EndGame_trigger;
  }

  public static boolean isMenu_trigger() { return menu_trigger; }

  public static void setMenu_trigger(boolean Menu_trigger) {
    menu_trigger = Menu_trigger;
  }

  public static void setInfo_prompt_trigger(boolean Info_prompt_trigger) {
    info_prompt_trigger = Info_prompt_trigger;
  }
}
