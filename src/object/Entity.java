package object;

import constants.EntityType;
import constants.Genders;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 * A general player in the game.
 */
public class Entity {

  private String name;
  private Genders gender;
  private ImageIcon avatar;
  private ImageIcon move;
  private int scores;
  private EntityType entityType;

  public Entity(String name, Genders gender, ImageIcon avatar, ImageIcon move, EntityType entityType) {
    this.name = name;
    this.gender = gender;
    this.avatar = avatar;
    this.move = move;
    this.entityType = entityType;
  }

  public Entity(EntityType entityType) {
    name = "ABCD";
    gender = Genders.MALE;
    avatar = new ImageIcon(getClass().getResource("/img/avatar/b1.png"));
    this.entityType = entityType;
    loadRandomMove();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Genders getGender() {
    return gender;
  }

  public void setGender(Genders gender) {
    this.gender = gender;
  }

  public ImageIcon getAvatar() {
    return avatar;
  }

  public void setAvatar(ImageIcon avatar) {
    this.avatar = avatar;
  }

  public int getScores() {
    return scores;
  }

  public void setScores(int scores) {
    this.scores = scores;
  }

  public void increaseScore() {
    setScores(scores + 1);
    System.out.println("Score of " + name + " is: " + scores);
  }

  public ImageIcon getMove() {
    return move;
  }

  public EntityType getEntityType() {
    return entityType;
  }
  
  public int loadRandomMove() {
    Random randomizer = new Random();
    final int num = randomizer.nextInt(5) + 1;
    String filePath = String.format("/img/elements/move%s.png", num);
    ImageIcon icon = new ImageIcon(getClass().getResource(filePath));
    move = icon;
    return num;
  }

  public int loadRandomMoveExcept(int number) {
    Random randomizer = new Random();
    int num = -1;
    
    do {
      num = randomizer.nextInt(5) + 1;
    } while (num == number);
   
    String filePath = String.format("/img/elements/move%s.png", num);
    ImageIcon icon = new ImageIcon(getClass().getResource(filePath));
    move = icon;
    return num;
  }
}
