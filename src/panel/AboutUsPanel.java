package panel;

import enums.PanelType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import frame.Frame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutUsPanel extends JPanel implements ActionListener {

  private Image backgroundImage;
  private JButton returnButton;

  public AboutUsPanel() {

    // Load background image
    backgroundImage = new ImageIcon(getClass().getResource("/img/bg/prompt.png")).getImage();

    //Layout
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(false);
    setPreferredSize(new Dimension(800, 800));
    add(Box.createRigidArea(new Dimension(0, 14)));

    //Header:
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setOpaque(false);
    headerPanel.setMaximumSize(new Dimension(800, 100));

    // Return Button
    returnButton = new JButton(new ImageIcon(getClass().getResource("/img/button/home.png")));
    returnButton.setBounds(0, 0, 60, 40);
    returnButton.setContentAreaFilled(false);
    returnButton.setBorderPainted(false);
    returnButton.setFocusPainted(false);
    returnButton.addActionListener(this);

    JPanel homeWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 10));
    homeWrapper.setOpaque(false);
    homeWrapper.add(returnButton);
    headerPanel.add(homeWrapper, BorderLayout.WEST);

    // Title
    JLabel title = new JLabel("About Us", SwingConstants.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 28));
    title.setForeground(Color.WHITE);
    headerPanel.add(title, BorderLayout.CENTER);
    add(headerPanel);

    // Logo
    ImageIcon logoIcon = new ImageIcon(getClass().getResource("/img/about/LogoFPT.png"));
    int targetWidth = 100;
    int originalWidth = logoIcon.getIconWidth();
    int originalHeight = logoIcon.getIconHeight();
    int scaledHeight = (targetWidth * originalHeight) / originalWidth;

    Image logoImg = logoIcon.getImage().getScaledInstance(targetWidth, scaledHeight, Image.SCALE_SMOOTH);
    JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
    JPanel logoWrapper = new JPanel(new BorderLayout());
    logoWrapper.setOpaque(false);
    logoWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 42));
    logoWrapper.add(logoLabel, BorderLayout.CENTER);
    headerPanel.add(logoWrapper, BorderLayout.EAST);

    //Info
    JLabel info = new JLabel("University: FPT Can Tho | Semester: SU25 | Class: SE1907 | Subject: CSD201");
    info.setFont(new Font("Arial", Font.PLAIN, 18));
    info.setForeground(Color.WHITE);
    info.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(Box.createRigidArea(new Dimension(0, 18)));
    add(info);

    // Central Grid Layout for members
    JPanel gridPanel = new JPanel(new GridBagLayout());
    gridPanel.setMaximumSize(new Dimension(800, 500));
    gridPanel.setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(0, 10, 10, 10); // Add spacing between cells

    // Row 1
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    JLabel mentorHeader = new JLabel("Mentor");
    mentorHeader.setFont(new Font("Arial", Font.BOLD, 20));
    mentorHeader.setForeground(Color.WHITE);
    gridPanel.add(mentorHeader, gbc);

    // Row 2
    gbc.gridy = 1;
    gridPanel.add(createMember("<html><b>Mr. Võ Hồng Khanh</b></html>", "/img/about/mentor.jpg"), gbc);

    // Row 3
    gbc.gridy = 2;
    JLabel groupLabel = new JLabel("Group 2");
    groupLabel.setFont(new Font("Arial", Font.BOLD, 24));
    groupLabel.setForeground(Color.WHITE);
    groupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    gridPanel.add(groupLabel, gbc);

    // Reset span and anchor
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(0, 40, 8, 0);

    // Row 4
    gbc.gridy = 3;
    gbc.gridx = 0;
    gridPanel.add(createMember(
	    "<html><b>Nguyễn Phi Long</b><br><font size='3'>CE170669 - <b><font color='#fdba14'>Leader</font></b></font></html>",
	    "/img/about/long.jpg"
    ), gbc);

    gbc.gridx = 1;
    gridPanel.add(createMember(
	    "<html><b>Trần Đức Toàn</b><br><font size='3'>CE191605 - <b><font color='#fdba14'>Secretary</font></b></font></html>",
	    "/img/about/toan.jpg"
    ), gbc);

    // Row 5
    gbc.gridy = 4;
    gbc.gridx = 0;
    gridPanel.add(createMember(
	    "<html><b>Hoa Hồng Nhung</b><br><font size='3'>CE182244</font></html>",
	    "/img/about/nhung.jpg"
    ), gbc);

    gbc.gridx = 1;
    gridPanel.add(createMember(
	    "<html><b>Nguyễn Thị Mỹ Truyện</b><br><font size='3'>CE181229</font></html>",
	    "/img/about/truyen.jpg"
    ), gbc);

    // Add grid to the panel
    add(gridPanel);
    add(Box.createRigidArea(new Dimension(0, 20)));
  }

  private JPanel createMember(String info, String imgPath) {
    JPanel panel = new JPanel();
    panel.setOpaque(false);
    panel.setLayout(new FlowLayout(FlowLayout.CENTER));

    try {
      int avatarSize = 80;
      int borderSize = 4;
      int totalSize = avatarSize + borderSize * 2;

      ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
      Image image = icon.getImage();

      // Create a transparent BufferedImage with room for border
      BufferedImage finalImage = new BufferedImage(totalSize, totalSize, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2 = finalImage.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // Draw white circular border
      g2.setColor(Color.WHITE);
      g2.fillOval(0, 0, totalSize, totalSize);

      // Set clip to draw avatar as a circle inside the border
      g2.setClip(new Ellipse2D.Float(borderSize, borderSize, avatarSize, avatarSize));
      g2.drawImage(image, borderSize, borderSize, avatarSize, avatarSize, null);

      g2.dispose();

      JLabel imgLabel = new JLabel(new ImageIcon(finalImage));

      JLabel textLabel = new JLabel(info);
      textLabel.setFont(new Font("Arial", Font.PLAIN, 16));
      textLabel.setForeground(Color.WHITE);
      textLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

      panel.add(imgLabel);
      panel.add(textLabel);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return panel;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == returnButton) {
      Frame.switchPanel(PanelType.MENU);
    }
  }
}
