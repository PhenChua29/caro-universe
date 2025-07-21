package object;

import constants.Difficulty;
import constants.EntityType;
import constants.Genders;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.ImageIcon;
import panel.GamePanel;

public class Bot extends Entity {

  private Difficulty difficulty;
  private static final int MAX_DEPTH = 3;

  public Bot() {
    super(EntityType.BOT);
    this.setName("Bot");
    this.setGender(Math.random() < 0.5 ? Genders.MALE : Genders.FEMALE);
    loadRandomAvatar();
    difficulty = Difficulty.EASY;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  public void loadRandomAvatar() {
    Random randomizer = new Random();
    final int num = randomizer.nextInt(3) + 1;
    String filePath = String.format("/img/avatar/b%s.png", num);
    ImageIcon icon = new ImageIcon(getClass().getResource(filePath));
    setAvatar(icon);
  }

  public Move makeMove(int[][] board) throws Exception {
    switch (difficulty) {
      case EASY:
	return getEasyMove(board);
      case MEDIUM:
	return getMediumMove(board);
      case HARD:
	return getHardMove(board);
      default:
	throw new Exception("Unknown difficulty!");
    }
  }

  private Move getEasyMove(int[][] board) {
    final int size = GamePanel.BOARD_SIZE;
    ArrayList<Integer> emptyCells = new ArrayList<>();

    for (int i = 0; i < size * size; i++) {
      if (board[i / size][i % size] == 0) {
	emptyCells.add(i);
      }
    }

    if (emptyCells.isEmpty()) {
      return null;
    }

    int index = emptyCells.get(new Random().nextInt(emptyCells.size()));
    return new Move(index / size, index % size);
  }

  private Move getMediumMove(int[][] board) {
    final int size = GamePanel.BOARD_SIZE;
    ArrayList<Integer> emptyCells = new ArrayList<>();

    for (int i = 0; i < size * size; i++) {
      if (board[i / size][i % size] == 0) {
	emptyCells.add(i);
      }
    }

    if (emptyCells.isEmpty()) {
      return null;
    }

    HashMap<Integer, List<Move>> playerThreats = findStreaks(board, size, GamePanel.PLAYER);
    for (int i = 4; i >= 3; i--) {
      if (playerThreats.containsKey(i)) {
	List<Move> blocks = playerThreats.get(i);
	return blocks.get(new Random().nextInt(blocks.size()));
      }
    }

    HashMap<Integer, List<Move>> botStreaks = findStreaks(board, size, GamePanel.BOT);
    if (botStreaks.isEmpty()) {
      int index = emptyCells.get(new Random().nextInt(emptyCells.size()));
      return new Move(index / size, index % size);
    }

    int maxStreak = Collections.max(botStreaks.keySet());
    List<Move> candidates = botStreaks.get(maxStreak);
    return candidates.get(new Random().nextInt(candidates.size()));
  }

  private HashMap<Integer, List<Move>> findStreaks(int[][] board, int size, int targetValue) {
    HashMap<Integer, List<Move>> moves = new HashMap<>();
    checkDirection(board, size, moves, 0, 1, targetValue);   // Horizontal
    checkDirection(board, size, moves, 1, 0, targetValue);   // Vertical
    checkDirection(board, size, moves, 1, 1, targetValue);   // Diagonal "\"
    checkDirection(board, size, moves, 1, -1, targetValue);  // Diagonal "/"
    return moves;
  }

  private void checkDirection(
	  int[][] board,
	  int size,
	  Map<Integer, List<Move>> viableMoves,
	  int deltaRow,
	  int deltaCol,
	  int targetValue
  ) {
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
	if (board[row][col] != targetValue) {
	  continue;
	}

	int currentRow = row, currentCol = col, count = 1;
	while (true) {
	  int nextRow = currentRow + deltaRow;
	  int nextCol = currentCol + deltaCol;
	  if (nextRow < 0 || nextRow >= size || nextCol < 0 || nextCol >= size || board[nextRow][nextCol] != targetValue) {
	    break;
	  }
	  ++count;
	  currentRow = nextRow;
	  currentCol = nextCol;
	}

	if (count < 2) {
	  continue;
	}

	int beforeRow = row - deltaRow, beforeCol = col - deltaCol;
	if (beforeRow >= 0 && beforeRow < size && beforeCol >= 0 && beforeCol < size && board[beforeRow][beforeCol] == 0) {
	  viableMoves.computeIfAbsent(count, k -> new ArrayList<>()).add(new Move(beforeRow, beforeCol));
	}

	int afterRow = currentRow + deltaRow, afterCol = currentCol + deltaCol;
	if (afterRow >= 0 && afterRow < size && afterCol >= 0 && afterCol < size && board[afterRow][afterCol] == 0) {
	  viableMoves.computeIfAbsent(count, k -> new ArrayList<>()).add(new Move(afterRow, afterCol));
	}

	row = currentRow;
	col = currentCol;
      }
    }
  }

  private Move getHardMove(int[][] board) {
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;

    for (int r = 0; r < GamePanel.BOARD_SIZE; r++) {
      for (int c = 0; c < GamePanel.BOARD_SIZE; c++) {
	if (board[r][c] == 0) {
	  board[r][c] = GamePanel.BOT;
	  int score = minimax(board, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
	  board[r][c] = 0;
	  if (score > bestScore) {
	    bestScore = score;
	    bestMove = new Move(r, c);
	  }
	}
      }
    }

    return bestMove != null ? bestMove : getEasyMove(board);
  }

  private int minimax(int[][] board, int depth, boolean isMax, int alpha, int beta) {
    int score = evaluate(board);
    if (Math.abs(score) >= 100000 || isBoardFull(board) || depth >= MAX_DEPTH) {
      return score;
    }

    if (isMax) {
      int best = Integer.MIN_VALUE;
      
      for (int r = 0; r < GamePanel.BOARD_SIZE; r++) {
	for (int c = 0; c < GamePanel.BOARD_SIZE; c++) {
	  if (board[r][c] == 0) {
	    board[r][c] = GamePanel.BOT;
	    best = Math.max(best, minimax(board, depth + 1, false, alpha, beta));
	    board[r][c] = 0;
	    alpha = Math.max(alpha, best);
	    if (beta <= alpha) {
	      return best;
	    }
	  }
	}
      }
      return best;
    } else {
      int best = Integer.MAX_VALUE;
      
      for (int r = 0; r < GamePanel.BOARD_SIZE; r++) {
	for (int c = 0; c < GamePanel.BOARD_SIZE; c++) {
	  if (board[r][c] == 0) {
	    board[r][c] = GamePanel.PLAYER;
	    best = Math.min(best, minimax(board, depth + 1, true, alpha, beta));
	    board[r][c] = 0;
	    beta = Math.min(beta, best);
	    if (beta <= alpha) {
	      return best;
	    }
	  }
	}
      }
      return best;
    }
  }

  private boolean isBoardFull(int[][] board) {
    for (int[] row : board) {
      for (int cell : row) {
	if (cell == 0) {
	  return false;
	}
      }
    }
    return true;
  }

  private int evaluate(int[][] board) {
    return evaluateLines(board, GamePanel.BOT) - evaluateLines(board, GamePanel.PLAYER);
  }

  private int evaluateLines(int[][] board, int player) {
    int score = 0;
    int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

    for (int r = 0; r < GamePanel.BOARD_SIZE; r++) {
      for (int c = 0; c < GamePanel.BOARD_SIZE; c++) {
	for (int[] dir : directions) {
	  int i = 0;
	  for (; i < 5; i++) {
	    int nr = r + dir[0] * i;
	    int nc = c + dir[1] * i;
	    if (nr < 0 || nr >= GamePanel.BOARD_SIZE || nc < 0 || nc >= GamePanel.BOARD_SIZE || board[nr][nc] != player) {
	      break;
	    }
	  }
	  if (i == 5) {
	    return 100000; // Win
	  }
	  if (i >= 2) {
	    score += Math.pow(10, i);
	  }
	}
      }
    }

    return score;
  }
}
