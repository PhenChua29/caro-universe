package panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import frame.frame;
import players.Genders;
import players.Player;

public class InfoPromptPanel extends JPanel implements ActionListener {

  private JTextField nameField;
  private JTextField matchCountField;

  private JRadioButton maleRadio;
  private JRadioButton femaleRadio;
  private JButton confirmButton;
  private ButtonGroup genderGroup;
  private Player player;

  public InfoPromptPanel(Player player) {
    this.player = player;
    init();
    initComponents();
    initBackground();
  }

  private void init() {
    this.setLayout(null);
    this.setVisible(true);
    this.setBounds(0, 0, 800, 800);
  }
  
  public void reset() {
    nameField.setText("");
    genderGroup.clearSelection();
    matchCountField.setText("");
  }

  private void initComponents() {
    JPanel content = new JPanel();

    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setOpaque(false);
    content.setSize(500, 400);
    int x = (800 - content.getWidth()) / 2;
    content.setLocation(x, 150);

    JLabel nameLabel = createLabel("Enter your name:", 36);
    content.add(nameLabel);
    content.add(Box.createVerticalStrut(8));

    JPanel nameFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    nameFieldPanel.setOpaque(false);
    nameField = new JTextField(20);
    nameField.setFont(new Font("Arial", Font.PLAIN, 18));
    nameField.setPreferredSize(new Dimension(200, 30));
    nameFieldPanel.add(nameField);
    content.add(nameFieldPanel);
    content.add(Box.createVerticalStrut(8));

    JLabel genderLabel = createLabel("Select your gender:", 36);
    content.add(genderLabel);
    content.add(Box.createVerticalStrut(8));

    maleRadio = createRadioButton("Male");
    femaleRadio = createRadioButton("Female");

    genderGroup = new ButtonGroup();
    genderGroup.add(maleRadio);
    genderGroup.add(femaleRadio);

    JPanel genderPanel = new JPanel();
    genderPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
    genderPanel.setOpaque(false);
    genderPanel.add(maleRadio);
    genderPanel.add(femaleRadio);
    content.add(genderPanel);
    content.add(Box.createVerticalStrut(8));

    JLabel matchLabel = createLabel("Enter number of matches:", 30);
    content.add(matchLabel);
    content.add(Box.createVerticalStrut(8));

    JPanel matchFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    matchFieldPanel.setOpaque(false);
    matchCountField = new JTextField(5);
    matchCountField.setFont(new Font("Arial", Font.PLAIN, 18));
    matchCountField.setPreferredSize(new Dimension(60, 30));
    matchFieldPanel.add(matchCountField);
    content.add(matchFieldPanel);
    content.add(Box.createVerticalStrut(12));

    JPanel confirmPanel = new JPanel();
    confirmPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    confirmPanel.setOpaque(false);
    confirmButton = new JButton("Confirm");
    confirmButton.setMaximumSize(new Dimension(150, 40));
    confirmButton.addActionListener(this);
    confirmPanel.add(confirmButton);
    content.add(confirmPanel);

    this.add(content);
  }

  private void initBackground() {
    JLabel imgBg = new JLabel();
    imgBg.setIcon(new ImageIcon(getClass().getResource("/img/1.png")));
    imgBg.setBounds(0, 0, 800, 800);
    this.add(imgBg);
    this.setComponentZOrder(imgBg,
	    this.getComponentCount() - 1); // Send to back
  }

  private JLabel createLabel(String text, int fontSize) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Arial", Font.BOLD, fontSize));
    label.setForeground(Color.WHITE);
    label.setAlignmentX(Component.LEFT_ALIGNMENT);
    return label;
  }

  private JRadioButton createRadioButton(String text) {
    JRadioButton radio = new JRadioButton(text);
    radio.setFont(new Font("Arial", Font.BOLD, 16));
    radio.setOpaque(false);
    radio.setForeground(Color.WHITE);
    return radio;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String name = nameField.getText().trim();
    Genders gender = maleRadio.isSelected() ? Genders.MALE
	    : femaleRadio.isSelected() ? Genders.FEMALE
	    : null;

    if (name.isEmpty() && gender == null) {
      JOptionPane.showMessageDialog(
	      this, "Please enter your name and select a gender.",
	      "Missing Information", JOptionPane.WARNING_MESSAGE);
      return;
    }

    if (name.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Name field cannot be empty.",
	      "Missing Name",
	      JOptionPane.WARNING_MESSAGE);
      return;
    }

    if (gender == null) {
      JOptionPane.showMessageDialog(this, "Please select a gender.",
	      "Missing Gender",
	      JOptionPane.WARNING_MESSAGE);
      return;
    }

    String matchText = matchCountField.getText().trim();
    int matchCount = 0;

    try {
      matchCount = Integer.parseInt(matchText);
      if (matchCount <= 0 || matchCount % 2 == 0) {
	throw new NumberFormatException();
      }
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this,
	      "Please enter a valid odd number greater than 0 for number of matches.",
	      "Invalid Match Count",
	      JOptionPane.WARNING_MESSAGE);
      return;
    }

    player.setName(name);
    player.setGender(gender);
    GamePanel.setTotalMatches(matchCount);
    GamePanel.initNewGame();
    frame.setNewGame_trigger(true);
  }
}
