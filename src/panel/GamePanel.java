package panel;

import frame.frame;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import lib.JButtonTemplate;
import lib.JPanelTemplate;
import players.Bot;
import players.Move;
import players.Player;

public class GamePanel extends JPanelTemplate implements ActionListener {

  private static JButtonTemplate[] button = new JButtonTemplate[9];
  private static boolean humanTurn;
  private static int player_win = 0;
  private static Random randomizer;
  private static int[][] playing_history = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
  private static ImageIcon ImageX;
  private static ImageIcon ImageO;

  private final Player player;
  private static Bot bot;

  // Init
  public GamePanel(final Player player, final Bot bot) {
    this.player = player;
    GamePanel.bot = bot;

    initgamePanel();
    initTurn();
  }

  private void initgamePanel() {
    this.set(0, 100, 800, 700, Color.GREEN);

    this.setLayout(new GridLayout(3, 3));
    this.setOpaque(false);

    for (int i = 0; i < button.length; ++i) {
      button[i] = new JButtonTemplate();
      button[i].setLocation(800 / 3, 700 / 3);
      button[i].setFont(new Font("Ink Free", Font.ITALIC, 75));
      button[i].setFocusable(false);
      button[i].addActionListener(this);
      button[i].setForeground(Color.WHITE);
      button[i].setStyling(false);

      final boolean debug = false;

      if (debug) {
        button[i].setBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
        button[i].setStyling(debug);
      }

      this.add(button[i]);
    }
  }

  private static void initTurn() {
    randomizer = new Random();
    // Randomize turn
    humanTurn = (randomizer.nextInt(2) == 1);

    // Randomize move's image of X and O
    final int NumX = randomizer.nextInt(5) + 1;
    int NumO;

    do {
      NumO = randomizer.nextInt(5) + 1;
    } while (NumO == NumX);

    final String filePathX = String.format("/img/move%s.png", NumX);
    final String filePathO = String.format("/img/move%s.png", NumO);

    ImageX = new ImageIcon(GamePanel.class.getResource(filePathX));
    ImageO = new ImageIcon(GamePanel.class.getResource(filePathO));

    if (!humanTurn) {
      handleBotTurn();
    }
  }

  public static void initNewGame() {
    for (int i = 0; i < 9; ++i) {
      playing_history[i / 3][i % 3] = 0;
      button[i].setText("");
      button[i].setIcon(null);
    }

    player_win = 0;
    initTurn();
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    System.out.println("The button " + e.getSource() + " is pressed");

    if (!humanTurn) {
      return;
    }

    for (int i = 0; i < 9; ++i) {
      final boolean isEmpty =
          button[i].getText().equals("") && button[i].getIcon() == null;
      final boolean isSource = e.getSource() == button[i];

      if (isSource && isEmpty) {
        draw_XO(button[i]);
        playing_history[i / 3][i % 3] = (humanTurn) ? 1 : 2;
        System.out.println("Changing playing_history[" + (i / 3) + "][" +
                           (i % 3) + "] = " + ((humanTurn) ? 1 : 2));
        checkWinner();
        changeTurn();
        break;
      }
    }
  }

  // Logic methods
  private void draw_XO(final JButton b) {
    b.setIcon((humanTurn) ? ImageX : ImageO);
  }

  private static void changeTurn() {
    humanTurn = !humanTurn;

    if (!humanTurn) {
      handleBotTurn();
    }
  }

  private static void handleBotTurn() {
    final Move botMove = bot.makeMove(playing_history);

    if (botMove != null) {
      final int row = botMove.getRow();
      final int col = botMove.getCol();

      playing_history[row][col] = 2;
      button[row * 3 + col].setIcon(ImageO);

      checkWinner();
      changeTurn();
    }
  }

  private static void checkWinner() {
    if (horizontalCheck() == 1 || verticalCheck() == 1 || XCheck() == 1) {
      frame.setEndGame_trigger(true);
      EndGamePanel.setText("Player " + player_win + " win!");
      System.out.println("Player " + player_win + " win!");
      return;
    }
    if (isFullBoard()) {
      frame.setEndGame_trigger(true);
      EndGamePanel.setText("We have a tie");
      System.out.println("We have a tie");
    }
  }

  private static int horizontalCheck() {
    for (int i = 0; i < 3; ++i) {
      if (playing_history[i][0] != 0) {
        if (playing_history[i][0] == playing_history[i][1] &&
            playing_history[i][1] == playing_history[i][2]) {
          player_win = playing_history[i][0];
          return 1;
        }
      }
    }
    return 0;
  }

  private static int verticalCheck() {
    for (int i = 0; i < 3; ++i) {
      if (playing_history[0][i] != 0) {
        if (playing_history[0][i] == playing_history[1][i] &&
            playing_history[1][i] == playing_history[2][i]) {
          player_win = playing_history[0][i];
          return 1;
        }
      }
    }
    return 0;
  }

  private static int XCheck() {
    final int i = 0;
    int j = 0;
    if (playing_history[i][j] != 0) {
      if (playing_history[i][j] == playing_history[i + 1][j + 1] &&
          playing_history[i + 1][j + 1] == playing_history[i + 2][j + 2]) {
        player_win = playing_history[i][j];
        return 1;
      }
    }
    j = 2;
    if (playing_history[i][j] != 0) {
      if (playing_history[i][j] == playing_history[i + 1][j - 1] &&
          playing_history[i + 1][j - 1] == playing_history[i + 2][j - 2]) {
        player_win = playing_history[i][j];
        return 1;
      }
    }
    return 0;
  }

  public static boolean isFullBoard() {
    int count = 0;
    for (int i = 0; i < playing_history.length; ++i) {
      for (int j = 0; j < playing_history[i].length; ++j) {
        count += playing_history[i][j] != 0 ? 1 : 0;
      }
    }
    return count == 9;
  }

  // Getter
  public static boolean ishumanTurn() { return humanTurn; }
}
