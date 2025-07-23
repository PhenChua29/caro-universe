package object;

/**
 * A move on the board.
 */
public class Move {

  private int row, col, score;

  public Move(int row, int col) {
    this.row = row;
    this.col = col;
    score = 0;
  }

  public Move(int row, int col, int score) {
    this.row = row;
    this.col = col;
    this.score = score;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public int getScore() {
    return score;
  }

}
