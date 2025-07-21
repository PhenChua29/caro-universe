package panel;

import constants.Difficulty;
import data.Record;
import frame.Frame;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import lib.JButtonTemplate;
import lib.JPanelTemplate;
import object.Bot;
import object.Move;
import object.Player;
import data.RecordManager;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class GamePanel extends JPanelTemplate implements ActionListener {

  public static final int BOARD_SIZE = 5;
  public static final int PLAYER = 1;
  public static final int BOT = 2;

  private static final JButtonTemplate[] button = new JButtonTemplate[BOARD_SIZE * BOARD_SIZE];
  private static boolean humanTurn;
  private static int player_win = 0;
  private static Random randomizer;
  private static int[][] playing_history;

  private static Player player;
  private static Bot bot;

  private static int totalMatches;
  private static int matchCounts;

  private static RecordManager recordManager;

  private static final int UNDO_PENALTY = -1;
  private static final int UNDO_LIMIT = 3;
  private static int undoCount;
  private static Stack<Move> moveHistory;

  // Init
  public GamePanel(final Player player, final Bot bot) {
    GamePanel.player = player;
    GamePanel.bot = bot;
    recordManager = new RecordManager();
    matchCounts = 1;
    undoCount = 0;
    playing_history = new int[BOARD_SIZE][BOARD_SIZE];
    moveHistory = new Stack<>();

    initgamePanel();
    initTurn();
  }

  private void initgamePanel() {
    this.set(0, 100, 800, 700, Color.GREEN);
    this.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
    this.setOpaque(false);

    for (int i = 0; i < button.length; ++i) {
      button[i] = new JButtonTemplate();
      button[i].setLocation(800 / BOARD_SIZE, 700 / BOARD_SIZE);
      button[i].setFont(new Font("Ink Free", Font.ITALIC, 75));
      button[i].setFocusable(false);
      button[i].addActionListener(this);
      button[i].setForeground(Color.WHITE);
      button[i].setStyling(false);

      final boolean debug = false;
      if (debug) {
	button[i].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
	button[i].setStyling(debug);
      }
      this.add(button[i]);
    }
  }

  private static void initTurn() {
    randomizer = new Random();
    humanTurn = (randomizer.nextInt(2) == 1);
  }

  public static void initNewGame() {
    for (int i = 0; i < button.length; ++i) {
      playing_history[i / BOARD_SIZE][i % BOARD_SIZE] = 0;
      button[i].setText("");
      button[i].setIcon(null);
    }
    moveHistory.clear();
    bot.loadRandomMoveExcept(player.loadRandomMove());
    player_win = 0;
    undoCount = 0;
    matchCounts = 1;
    updateRoundText();
    resetScores();
    initTurn();
  }

  public static void resetScores() {
    player.setScores(0);
    bot.setScores(0);
  }

  public static void updateRoundText() {
    if (shouldPlayNextMatch()) {
      InfoPanel.setLabelText(String.format("Round %d", matchCounts));
    } else {
      InfoPanel.setLabelText(String.format("Final round!", matchCounts));
    }
  }

  public static void nextMatch() {
    for (int i = 0; i < button.length; ++i) {
      playing_history[i / BOARD_SIZE][i % BOARD_SIZE] = 0;
      button[i].setText("");
      button[i].setIcon(null);
    }
    moveHistory.clear(); // 5. Clear history for the next match
    bot.loadRandomMoveExcept(player.loadRandomMove());
    player_win = 0;
    undoCount = 0;
    ++matchCounts;
    updateRoundText();
    initTurn();
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if (!humanTurn || player_win != 0) {
      return;
    }

    for (int i = 0; i < button.length; ++i) {
      final boolean isEmpty = (button[i].getText() == null || button[i].getText().isEmpty()) && button[i].getIcon() == null;
      final boolean isSource = e.getSource() == button[i];

      if (isSource && isEmpty) {
	moveHistory.push(new Move(i / BOARD_SIZE, i % BOARD_SIZE));
	drawMove(button[i]);
	playing_history[i / BOARD_SIZE][i % BOARD_SIZE] = (humanTurn) ? 1 : 2;
	checkWinner();
	changeTurn();
	break;
      }
    }
  }

  private static void drawMove(final JButton b) {
    b.setIcon((humanTurn) ? player.getMove() : bot.getMove());
  }

  private static void changeTurn() {
    humanTurn = !humanTurn;
    InfoPanel.resetTimerThread(false);
  }

  private static void handleBotTurn() {
    if (player_win != 0) {
      return;
    }

    try {
      final Move botMove = bot.makeMove(playing_history);

      if (botMove != null) {
	final int row = botMove.getRow();
	final int col = botMove.getCol();

	moveHistory.push(botMove);
	playing_history[row][col] = 2;
	button[row * BOARD_SIZE + col].setIcon(bot.getMove());

	checkWinner();
	changeTurn();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void checkWinner() {
    if (player_win != 0) {
      return;
    }

    if (horizontalCheck() == 1 || verticalCheck() == 1 || XCheck() == 1) {
      if (player_win == 1) {
	player.increaseScore();
      } else {
	bot.increaseScore();
      }

      player.increaseTotalGames();
      recordManager.update(player.getRecord());

      if (shouldPlayNextMatch()) {
	if (humanTurn) {
	  EndGamePanel.setText(String.format("Player \"%s\" win this round!", player.getName()));
	} else {
	  EndGamePanel.setText("Bot win this round!");
	}
	EndGamePanel.setMode(EndGamePanel.BTN_MODE.NEXT_MATCH);
      } else {
	if (humanTurn) {
	  EndGamePanel.setText(String.format("Player \"%s\" win!", player.getName()));
	} else {
	  EndGamePanel.setText("Bot win!");
	}
	EndGamePanel.setMode(EndGamePanel.BTN_MODE.NEW_MATCH);
      }
      Frame.setEndGame_trigger(true);
      return;
    }

    if (isFullBoard()) {
      player.increaseScore();
      player.increaseTotalGames();
      bot.increaseScore();
      recordManager.update(player.getRecord());
      Frame.setEndGame_trigger(true);

      if (shouldPlayNextMatch()) {
	EndGamePanel.setText("We have a tie in this round!");
	EndGamePanel.setMode(EndGamePanel.BTN_MODE.NEXT_MATCH);
      } else {
	EndGamePanel.setText("We have a tie!");
	EndGamePanel.setMode(EndGamePanel.BTN_MODE.NEW_MATCH);
      }
    }
  }

  public static void undoLastMove() {
    if (!humanTurn || player_win != 0 || moveHistory.size() < 2) {
      JOptionPane.showMessageDialog(null, "Cannot undo at this time.", "Undo Unavailable", JOptionPane.WARNING_MESSAGE);
      return;
    }

    if (undoCount >= UNDO_LIMIT) {
      JOptionPane.showMessageDialog(null, String.format("You cannot undo more than %s time%s in a round.", Math.abs(UNDO_LIMIT), Math.abs(UNDO_LIMIT) > 1 ? "s" : ""), "Undo Unavailable", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int totalScores = player.getRecord().getTotalScore();

    if (totalScores + UNDO_PENALTY < 0) {
      JOptionPane.showMessageDialog(null, String.format("You must have at least %s score%s to undo.", Math.abs(UNDO_PENALTY), Math.abs(UNDO_PENALTY) > 1 ? "s" : ""), "Undo Unavailable", JOptionPane.WARNING_MESSAGE);
      return;
    }

    int response = JOptionPane.showConfirmDialog(
	    null,
	    "Are you sure you want to undo your last turn?\nThis will incur a " + UNDO_PENALTY + " point penalty.",
	    "Confirm Undo",
	    JOptionPane.YES_NO_OPTION,
	    JOptionPane.QUESTION_MESSAGE
    );

    if (response == JOptionPane.YES_OPTION) {
      Move botMove = moveHistory.pop();
      int row = botMove.getRow();
      int col = botMove.getCol();
      playing_history[row][col] = 0;
      button[row * BOARD_SIZE + col].setIcon(null);

      Move playerMove = moveHistory.pop();
      row = playerMove.getRow();
      col = playerMove.getCol();
      playing_history[row][col] = 0;
      button[row * BOARD_SIZE + col].setIcon(null);

      Record r = player.getRecord();
      r.setTotalScore(r.getTotalScore() + UNDO_PENALTY);
      InGamePanel.update();
      ++undoCount;
    }
  }

  private static boolean isTie() {
    return player.getScores() == bot.getScores();
  }

  private static boolean shouldPlayNextMatch() {
    return isTie() || matchCounts < (int) (Math.ceil((Double.valueOf(totalMatches) / 2)));
  }

  private static int horizontalCheck() {
    for (int i = 0; i < BOARD_SIZE; ++i) {
      for (int j = 0; j < BOARD_SIZE - 1; j++) {
	if (playing_history[i][j] == 0 || playing_history[i][j + 1] == 0) {
	  break;
	}
	if (playing_history[i][j] != playing_history[i][j + 1]) {
	  break;
	}
	if (j == BOARD_SIZE - 2) {
	  player_win = playing_history[i][j];
	  return 1;
	}
      }
    }
    return 0;
  }

  private static int verticalCheck() {
    for (int i = 0; i < BOARD_SIZE; ++i) {
      for (int j = 0; j < BOARD_SIZE - 1; j++) {
	if (playing_history[j][i] == 0 || playing_history[j + 1][i] == 0) {
	  break;
	}
	if (playing_history[j][i] != playing_history[j + 1][i]) {
	  break;
	}
	if (j == BOARD_SIZE - 2) {
	  player_win = playing_history[j][i];
	  return 1;
	}
      }
    }
    return 0;
  }

  private static int XCheck() {
    for (int i = 1; i < BOARD_SIZE; i++) {
      if (playing_history[i][i] == 0
	      || playing_history[i - 1][i - 1] != playing_history[i][i]) {
	break;
      }
      if (i == BOARD_SIZE - 1) {
	player_win = playing_history[i][i];
	return 1;
      }
    }

    for (int i = 1; i < BOARD_SIZE; i++) {
      int prevRow = i - 1;
      int prevCol = BOARD_SIZE - i;
      int currRow = i;
      int currCol = BOARD_SIZE - 1 - i;

      if (playing_history[currRow][currCol] == 0
	      || playing_history[prevRow][prevCol] != playing_history[currRow][currCol]) {
	break;
      }

      if (i == BOARD_SIZE - 1) {
	player_win = playing_history[currRow][currCol];
	return 1;
      }
    }

    return 0;
  }

  public static boolean isFullBoard() {
    int count = 0;
    for (int i = 0; i < BOARD_SIZE; ++i) {
      for (int j = 0; j < BOARD_SIZE; ++j) {
	count += playing_history[i][j] != 0 ? 1 : 0;
      }
    }
    return count == BOARD_SIZE * BOARD_SIZE;
  }

  // Getter & Setter
  public static boolean isHumanTurn() {
    return humanTurn;
  }

  public static int getTotalMatches() {
    return totalMatches;
  }

  public static void setTotalMatches(int totalMatches) {
    GamePanel.totalMatches = totalMatches;
  }

  public static int getMatchCounts() {
    return matchCounts;
  }

  public static void setMatchCounts(int matchCounts) {
    GamePanel.matchCounts = matchCounts;
  }

  public static void makeRandomMove() {
    if (!humanTurn || player_win != 0) {
      return;
    }

    Move m = getRandomMove();

    moveHistory.push(m);
    drawMove(button[m.getRow() * BOARD_SIZE + m.getCol()]);
    playing_history[m.getRow()][m.getCol()] = PLAYER;
    checkWinner();
    changeTurn();
  }

  private static Move getRandomMove() {
    final int size = GamePanel.BOARD_SIZE;
    ArrayList<Integer> emptyCells = new ArrayList<>();

    for (int i = 0; i < size * size; i++) {
      if (playing_history[i / size][i % size] == 0) {
	emptyCells.add(i);
      }
    }

    if (emptyCells.isEmpty()) {
      return null;
    }

    int index = emptyCells.get(new Random().nextInt(emptyCells.size()));
    return new Move(index / size, index % size);
  }

  public static void timerStartHandler() {
    if (!humanTurn) {
      new Thread(() -> {
	try {
	  Difficulty difficulty = bot.getDifficulty();
	  float factor = 1;

	  switch (difficulty) {
	    case EASY:
	      factor = 0.5f;
	      break;
	    case MEDIUM:
	      factor = 1;
	    case HARD:
	      factor = 1.5f;
	  }

	  Thread.sleep((long) (1000 * factor));
	} catch (Exception e) {
	  e.printStackTrace();
	}

	SwingUtilities.invokeLater(() -> {
	  handleBotTurn();
	});
      }).start();
    }
  }
}
