package object;

import enums.EntityType;
import enums.Genders;
import data.Record;
import data.RecordManager;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 * The human player.
 */
public class Player extends Entity {

  private Record record;

  public Player(String name, Genders gender, ImageIcon avatar, ImageIcon move, Record record) {
    super(name, gender, avatar, move, EntityType.PLAYER);
    this.record = record;
  }

  public Player() {
    super(EntityType.PLAYER);
    record = new Record(getName());
    loadRandomAvatar();
  }

  @Override
  public void setName(String name) {
    super.setName(name);
    loadData();
  }

  @Override
  public void setScores(int score) {
    int difference = score - getScores();
    super.setScores(getScores() + difference);
    record.setTotalScore(record.getTotalScore() + difference);
  }

  @Override
  public void increaseScore() {
    super.increaseScore();
    record.setTotalScore(record.getTotalScore() + 1);
  }

  public void increaseTotalGames() {
    record.setTotalGames(record.getTotalGames() + 1);
  }

  public Record getRecord() {
    return record;
  }

  public void setRecord(Record record) {
    this.record = record;
  }

  public void loadData() {
    RecordManager rm = new RecordManager();
    Record r = rm.getRecord(getName());

    if (r == null) {
      r = new Record(getName());
    }

    record = r;
  }

  public void loadRandomAvatar() {
    Random randomizer = new Random();
    final int num = randomizer.nextInt(3) + 1;
    String prefix = getGender() == Genders.MALE ? "m" : "f";
    String filePath = String.format("/img/avatar/%s%s.png", prefix, num);
    ImageIcon icon = new ImageIcon(getClass().getResource(filePath));
    setAvatar(icon);
  }
}
