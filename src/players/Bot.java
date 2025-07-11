package players;

import javax.swing.ImageIcon;

/**
 * The computer controlled player.
 */
public class Bot extends Entity {
  public Bot() {
    super();

    this.setName("Bot");
    this.setGender(Math.random() < 0.5 ? Genders.MALE : Genders.FEMALE);
    this.setAvatar(new ImageIcon(getClass().getResource("/img/move2.png")));
  }

  public Move makeMove(int[][] board) {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j] == 0) {
          return new Move(i, j);
        }
      }
    }

    return null;
  }
}

