package constants;

/**
 * The gamme's difficulty, which control how the bot reacts.
 */
public enum Difficulty {
  EASY,
  MEDIUM,
  HARD;

  @Override
  public String toString() {
    return this.name().charAt(0) + this.name().substring(1).toLowerCase();
  }
}
