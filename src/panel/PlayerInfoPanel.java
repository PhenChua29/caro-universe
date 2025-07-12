package panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import players.Entity;
import players.Genders;

/**
 * A panel that displays player information.
 */
public class PlayerInfoPanel extends JPanel {

  private Entity entity;
  private JLabel namelbl;
  private JLabel scoreslbl;
  private JLabel avatarlbl;

  public PlayerInfoPanel(Entity entity, boolean isMirrored) {
    this.entity = entity;
    initComponents(entity);
    initPanel(isMirrored);
  }

  private void initComponents(Entity entity) {
    namelbl = new JLabel();
    namelbl.setForeground(Color.WHITE);
    namelbl.setFont(new Font("Ink Free", Font.BOLD, 26));

    scoreslbl = new JLabel();
    scoreslbl.setForeground(Color.WHITE);
    scoreslbl.setFont(new Font("Ink Free", Font.BOLD, 13));
    
    avatarlbl = new JLabel();
    avatarlbl.setSize(100, 100);

    update();
  }

  private void initPanel(boolean isMirrored) {
    this.setOpaque(false);
    this.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 4, 4, 4);

    gbc.gridx = isMirrored ? 1 : 0;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    this.add(avatarlbl, gbc);

    gbc.gridheight = 1;
    gbc.insets = new Insets(16, 0, 0, 0);
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.WEST;

    gbc.gridx = isMirrored ? 0 : 1;
    gbc.gridy = 0;
    this.add(namelbl, gbc);

    gbc.insets = new Insets(0, 0, 0, 0);
    gbc.gridx = isMirrored ? 0 : 1;
    gbc.gridy = 1;
    gbc.weighty = 0;
    this.add(scoreslbl, gbc);
  }

  public void update() {
    namelbl.setText(entity.getName());
    scoreslbl.setText("Scores: " + entity.getScores());
    System.out.println("Calling update for " + entity.getName() + " with score " + entity.getScores());
    Image scaledImage = entity.getAvatar().getImage().getScaledInstance(
	    100, 100, Image.SCALE_SMOOTH);
    ImageIcon avatarIcon = new ImageIcon(scaledImage);
    avatarlbl.setIcon(avatarIcon);
  }
}
