package panel;

import enums.EntityType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import object.Entity;
import object.Player;

/**
 * A panel that displays player information.
 */
public class PlayerInfoPanel extends JPanel {
  
  private final Entity entity;
  private JLabel nameLbl;
  private JLabel scoresLbl;
  private JLabel totalScoresLbl;
  private JLabel avatarLbl;
  private JLabel moveLbl;
  
  public PlayerInfoPanel(Entity entity, boolean isMirrored) {
    this.entity = entity;
    setPreferredSize(new Dimension(230, 100));
    
    initComponents();
    initPanel(isMirrored);
  }
  
  private void initComponents() {
    int fixedWidth = 100;
    
    nameLbl = new JLabel();
    nameLbl.setForeground(Color.WHITE);
    nameLbl.setFont(new Font("Ink Free", Font.BOLD, 18));
    nameLbl.setPreferredSize(new Dimension(fixedWidth, 30));
    nameLbl.setHorizontalAlignment(JLabel.LEFT);
    
    scoresLbl = new JLabel();
    scoresLbl.setForeground(Color.WHITE);
    scoresLbl.setFont(new Font("Ink Free", Font.BOLD, 13));
    scoresLbl.setPreferredSize(new Dimension(fixedWidth, 20));
    
    if (entity.getEntityType() == EntityType.PLAYER) {
      totalScoresLbl = new JLabel();
      totalScoresLbl.setForeground(Color.WHITE);
      totalScoresLbl.setFont(new Font("Ink Free", Font.BOLD, 13));
      totalScoresLbl.setPreferredSize(new Dimension(fixedWidth, 20));
    }
    
    avatarLbl = new JLabel();
    avatarLbl.setPreferredSize(new Dimension(100, 100));
    
    moveLbl = new JLabel();
    moveLbl.setPreferredSize(new Dimension(25, 25));
    
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
    this.add(avatarLbl, gbc);
    
    gbc.gridheight = 1;
    gbc.insets = new Insets(16, 0, 0, 8);
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.WEST;
    
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.ipady = 4;
    this.add(nameLbl, gbc);
    
    gbc.gridx = isMirrored ? 0 : 2;
    gbc.gridy = 0;
    this.add(moveLbl, gbc);
    
    gbc.insets = new Insets(0, 0, 0, 0);
    gbc.gridx = isMirrored ? 0 : 1;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    
    if (entity.getEntityType() == EntityType.PLAYER) {
      this.add(totalScoresLbl, gbc);
      gbc.gridy = 2;
      this.add(scoresLbl, gbc);
    } else {
      this.add(scoresLbl, gbc);
    }
  }
  
  public void update() {
    String name = entity.getName();
    FontMetrics fm = nameLbl.getFontMetrics(nameLbl.getFont());
    int maxWidth = nameLbl.getPreferredSize().width;
    
    String clippedName = name;
    while (fm.stringWidth(clippedName + "...") > maxWidth && clippedName.length() > 0) {
      clippedName = clippedName.substring(0, clippedName.length() - 1);
    }
    if (!clippedName.equals(name)) {
      clippedName += "...";
    }
    nameLbl.setText(clippedName);
    
    if (entity.getEntityType() == EntityType.PLAYER) {
      int totalScores = ((Player) entity).getRecord().getTotalScore();
      totalScoresLbl.setText("Total: " + totalScores);
    }
    
    scoresLbl.setText("Scores: " + entity.getScores());
    
    Image scaledImage = entity.getAvatar().getImage().getScaledInstance(
	    100, 100, Image.SCALE_SMOOTH);
    ImageIcon icon = new ImageIcon(scaledImage);
    avatarLbl.setIcon(icon);
    scaledImage = entity.getMove().getImage().getScaledInstance(
	    25, 25, Image.SCALE_SMOOTH);
    icon = new ImageIcon(scaledImage);
    moveLbl.setIcon(icon);
  }
}
