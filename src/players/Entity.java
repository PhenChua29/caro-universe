package players;

import javax.swing.ImageIcon;

/**
 * A general player in the game.
 */
public class Entity {
  private String name;
  private Genders gender;
  private ImageIcon avatar;

  public Entity(String name, Genders gender, ImageIcon avatar) {
    this.name = name;
    this.gender = gender;
    this.avatar = avatar;
  }

  public Entity() {
    this.name = "ABCD";
    this.gender = Genders.MALE;
    this.avatar = new ImageIcon(getClass().getResource("/img/move1.png"));

    if (avatar.getIconWidth() <= 0 || avatar.getIconHeight() <= 0) {
      System.err.println("Failed to load image: /img/move1.png");
    } else {
      System.out.println("Image loaded successfully.");
    }
  }

  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public Genders getGender() { return gender; }

  public void setGender(Genders gender) { this.gender = gender; }

  public ImageIcon getAvatar() { return avatar; }

  public void setAvatar(ImageIcon avatar) { this.avatar = avatar; }
}
