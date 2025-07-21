package data;

/**
 * A class representing a player's record in the game.
 */
public class Record implements Comparable<Record> {

  private int totalScore;
  private String playerName;
  private int totalGames;
  
  public Record(String playerName, int totalScore, int totalGames) {
    this.playerName = playerName;
    this.totalScore = totalScore;
    this.totalGames = totalGames;
  }
  
  public Record(String name) {
    this(name , 0, 0);
  }
  
  public int getTotalScore() {
    return totalScore;
  }
  
  public void setTotalScore(int totalScore) {
    this.totalScore = totalScore;
  }
  
  public String getPlayerName() {
    return playerName;
  }
  
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }
  
  public int getTotalGames() {
    return totalGames;
  }
  
  public void setTotalGames(int totalGames) {
    this.totalGames = totalGames;
  }

  @Override
  public int compareTo(Record r) {
    return Integer.compare(this.totalScore, r.getTotalScore());
  }
}
