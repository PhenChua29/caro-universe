package panel;

import constants.EntityType;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import object.Entity;
import object.Player;

/**
 * A panel that displays player information.
 */
public class PlayerInfoPanel extends JPanel {

  private Entity entity;
  private JLabel namelbl;
  private JLabel scoreslbl;
  private JLabel totalScoreslbl;
  private JLabel avatarlbl;
  private JLabel movelbl;

  public PlayerInfoPanel(Entity entity, boolean isMirrored) {
    this.entity = entity;
    initComponents();
    initPanel(isMirrored);
  }

  private void initComponents() {
    namelbl = new JLabel();
    namelbl.setForeground(Color.WHITE);
    namelbl.setFont(new Font("Ink Free", Font.BOLD, 26));

    scoreslbl = new JLabel();
    scoreslbl.setForeground(Color.WHITE);
    scoreslbl.setFont(new Font("Ink Free", Font.BOLD, 13));

    if (entity.getEntityType() == EntityType.PLAYER) {
      totalScoreslbl = new JLabel();
      totalScoreslbl.setForeground(Color.WHITE);
      totalScoreslbl.setFont(new Font("Ink Free", Font.BOLD, 13));
    }

    avatarlbl = new JLabel();
    avatarlbl.setSize(100, 100);

    movelbl = new JLabel();
    movelbl.setSize(25, 25);

    update();
  }

  private void initPanel(boolean isMirrored) {
    this.setOpaque(false);
    this.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 4, 4, 4);

    gbc.gridx = isMirrored ? 2 : 0;
    gbc.gridy = 0;
    gbc.gridheight = 4;
    this.add(avatarlbl, gbc);

    gbc.gridheight = 1;
    gbc.insets = new Insets(16, 0, 0, 8);
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.WEST;

    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.ipady = 4;
    this.add(namelbl, gbc);

    gbc.gridx = isMirrored ? 0 : 2;
    gbc.gridy = 0;
    this.add(movelbl, gbc);

    gbc.insets = new Insets(0, 0, 0, 0);
    gbc.gridx = isMirrored ? 0 : 1;
    gbc.gridy = 1;
    gbc.gridwidth = 2;

    if (entity.getEntityType() == EntityType.PLAYER) {
      this.add(totalScoreslbl, gbc);
      gbc.gridy = 2;
      this.add(scoreslbl, gbc);
    } else {
      this.add(scoreslbl, gbc);
    }
  }

  public void update() {
    namelbl.setText(entity.getName());

    if (entity.getEntityType() == EntityType.PLAYER) {
      int totalScores = ((Player) entity).getRecord().getTotalScore();
      totalScoreslbl.setText("Total: " + totalScores);
    }

    scoreslbl.setText("Scores: " + entity.getScores());

    Image scaledImage = entity.getAvatar().getImage().getScaledInstance(
	    100, 100, Image.SCALE_SMOOTH);
    ImageIcon icon = new ImageIcon(scaledImage);
    avatarlbl.setIcon(icon);
    scaledImage = entity.getMove().getImage().getScaledInstance(
	    25, 25, Image.SCALE_SMOOTH);
    icon = new ImageIcon(scaledImage);
    movelbl.setIcon(icon);
  }
}
