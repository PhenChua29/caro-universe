package players;

import javax.swing.ImageIcon;

/**
 * The human player.
 */
public class Player extends Entity {
  private Record record;

  public Player(String name, Genders gender, ImageIcon avatar, Record record) {
    super(name , gender, avatar);
    this.record = record;
  }

  public Player() {
    super();
    this.record = new Record();
  }

  public Record getRecord() { return record; }

  public void setRecord(Record record) { this.record = record; }
}
