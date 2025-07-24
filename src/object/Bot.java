package object;

import enums.Difficulty;
import enums.EntityType;
import enums.Genders;
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
  private final int WIN = 1_000_000_000;
  private final Map<String, Integer> evalCache;

  public Bot() {
    super(EntityType.BOT);
    this.setName("Bot");
    this.setGender(Math.random() < 0.5 ? Genders.MALE : Genders.FEMALE);
    loadRandomAvatar();
    difficulty = Difficulty.EASY;
    evalCache = new HashMap<>();
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  public void loadRandomAvatar() {
    Random randomizer = new Random();
    final int num = randomizer.nextInt(4) + 1;
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
    final int minThreshold = GamePanel.WIN_CONDITION - 2 < 2 ? 2 : GamePanel.WIN_CONDITION - 2;

    for (int i = GamePanel.WIN_CONDITION - 1; i >= minThreshold; i--) {
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
      }
    }
  }

  private Move getHardMove(int[][] board) {
    evalCache.clear();
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
    int score = evaluateCached(board);

    if (Math.abs(score) >= WIN || isBoardFull(board) || depth >= MAX_DEPTH) {
      return score;
    }

    List<Move> moves = getSortedMoves(board, isMax ? GamePanel.BOT : GamePanel.PLAYER);

    if (isMax) {
      int best = Integer.MIN_VALUE;

      for (Move move : moves) {
	board[move.getRow()][move.getCol()] = GamePanel.BOT;
	best = Math.max(best, minimax(board, depth + 1, false, alpha, beta));
	board[move.getRow()][move.getCol()] = 0;

	alpha = Math.max(alpha, best);
	if (beta <= alpha) {
	  break;
	}
      }
      return best;
    } else {
      int best = Integer.MAX_VALUE;

      for (Move move : moves) {
	board[move.getRow()][move.getCol()] = GamePanel.PLAYER;
	best = Math.min(best, minimax(board, depth + 1, true, alpha, beta));
	board[move.getRow()][move.getCol()] = 0;

	beta = Math.min(beta, best);
	if (beta <= alpha) {
	  break;
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

  private int evaluateCached(int[][] board) {
    String key = boardHash(board);

    if (evalCache.containsKey(key)) {
      return evalCache.get(key);
    }

    int score = evaluate(board);
    evalCache.put(key, score);
    return score;
  }

  private int evaluate(int[][] board) {
    return evaluateLines(board, GamePanel.BOT) - evaluateLines(board, GamePanel.PLAYER);
  }

  private int evaluateLines(int[][] board, int player) {
    int score = 0;

    final int[][] DIRECTIONS = {
      {0, 1}, // Horizontal
      {1, 0}, // Vertical
      {1, 1}, // Diagonal \
      {1, -1} // Diagonal /
    };

    final int WIN_COND = GamePanel.WIN_CONDITION;
    final int SIZE = GamePanel.BOARD_SIZE;
    final int EMPTY = 0;
    final int CENTER = SIZE / 2;
    final float FORGETFULNESS = 0.1f;

    final boolean forgetCenterPos = Math.random() < FORGETFULNESS;
    final boolean forgetWinCondition = Math.random() < FORGETFULNESS;
    final boolean forgetLeapTrap = Math.random() < FORGETFULNESS;
    final boolean forgetDoubleOpenThreat = Math.random() < FORGETFULNESS;

    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {

	if (board[row][col] != player) {
	  continue;
	}

	int cellScore = 0;

	if (!forgetCenterPos) {
	  int centerDistance = Math.abs(row - CENTER) + Math.abs(col - CENTER);
	  cellScore += 10 * (SIZE - centerDistance);
	}

	for (int[] dir : DIRECTIONS) {
	  int streakLength = 0;

	  while (streakLength < WIN_COND) {
	    int r = row + dir[0] * streakLength;
	    int c = col + dir[1] * streakLength;

	    if (!isInBounds(r, c, SIZE) || board[r][c] != player) {
	      break;
	    }
	    streakLength++;
	  }

	  if (!forgetWinCondition && streakLength == WIN_COND) {
	    return WIN;
	  }

	  if (streakLength >= 2) {
	    int beforeRow = row - dir[0];
	    int beforeCol = col - dir[1];
	    int afterRow = row + dir[0] * streakLength;
	    int afterCol = col + dir[1] * streakLength;

	    boolean openStart = isInBounds(beforeRow, beforeCol, SIZE) && board[beforeRow][beforeCol] == EMPTY;
	    boolean openEnd = isInBounds(afterRow, afterCol, SIZE) && board[afterRow][afterCol] == EMPTY;

	    double streakScore = Math.pow(10, streakLength);

	    if (openStart && openEnd) {
	      streakScore *= 2;
	    } else if (!openStart && !openEnd) {
	      streakScore = Math.pow(10, streakLength - 1);
	    }

	    cellScore += streakScore;

	    if (streakLength == WIN_COND - 1 && (openStart || openEnd)) {
	      cellScore += 5000;
	    }
	  }

	  // Leap traps
	  if (!forgetLeapTrap) {
	    int midRowF = row + dir[0];
	    int midColF = col + dir[1];
	    int endRowF1 = row + dir[0] * 2;
	    int endColF1 = col + dir[1] * 2;
	    int endRowF2 = row + dir[0] * 3;
	    int endColF2 = col + dir[1] * 3;

	    if (isInBounds(midRowF, midColF, SIZE) && board[midRowF][midColF] == EMPTY
		    && isInBounds(endRowF1, endColF1, SIZE) && board[endRowF1][endColF1] == player
		    && isInBounds(endRowF2, endColF2, SIZE) && board[endRowF2][endColF2] == player) {
	      cellScore += 800;
	    }

	    int backRow1 = row - dir[0];
	    int backCol1 = col - dir[1];
	    int backRow2 = row - dir[0] * 2;
	    int backCol2 = col - dir[1] * 2;
	    int backRow3 = row - dir[0] * 3;
	    int backCol3 = col - dir[1] * 3;

	    if (isInBounds(backRow1, backCol1, SIZE) && board[backRow1][backCol1] == player
		    && isInBounds(backRow2, backCol2, SIZE) && board[backRow2][backCol2] == EMPTY
		    && isInBounds(backRow3, backCol3, SIZE) && board[backRow3][backCol3] == player) {
	      cellScore += 800;
	    }
	  }

	  // Double open-end threat
	  if (!forgetDoubleOpenThreat) {
	    int bRow = row - dir[0];
	    int bCol = col - dir[1];
	    int aRow1 = row + dir[0];
	    int aCol1 = col + dir[1];
	    int aRow2 = row + dir[0] * 2;
	    int aCol2 = col + dir[1] * 2;
	    int aRow3 = row + dir[0] * 3;
	    int aCol3 = col + dir[1] * 3;

	    if (isInBounds(bRow, bCol, SIZE) && board[bRow][bCol] == EMPTY
		    && isInBounds(aRow1, aCol1, SIZE) && board[aRow1][aCol1] == player
		    && isInBounds(aRow2, aCol2, SIZE) && board[aRow2][aCol2] == EMPTY
		    && isInBounds(aRow3, aCol3, SIZE) && board[aRow3][aCol3] == EMPTY) {
	      cellScore += 3000;
	    }
	  }
	}

	if (row == 0 || col == 0 || row == SIZE - 1 || col == SIZE - 1) {
	  cellScore *= 0.7;
	}

	score += cellScore;
      }
    }

    return score;
  }

  private boolean isInBounds(int row, int col, int size) {
    return 0 <= row && 0 <= col && row < size && col < size;
  }

  private List<Move> getSortedMoves(int[][] board, int player) {
    List<Move> moves = new ArrayList<>();

    for (int r = 0; r < GamePanel.BOARD_SIZE; r++) {
      for (int c = 0; c < GamePanel.BOARD_SIZE; c++) {
	if (board[r][c] == 0) {
	  board[r][c] = player;
	  int score = evaluateCached(board);
	  board[r][c] = 0;

	  Move move = new Move(r, c, score);
	  moves.add(move);
	}
      }
    }

    moves.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
    return moves;
  }

  private String boardHash(int[][] board) {
    StringBuilder sb = new StringBuilder();
    for (int[] row : board) {
      for (int cell : row) {
	sb.append(cell);
      }
    }
    return sb.toString();
  }

}
