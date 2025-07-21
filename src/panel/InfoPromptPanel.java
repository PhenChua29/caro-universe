package panel;

import constants.Difficulty;
import constants.Genders;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import frame.Frame;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import lib.JButtonTemplate;
import object.Bot;
import object.Player;

public class InfoPromptPanel extends JPanel implements ActionListener {

  private JTextField nameField;
  private JComboBox<Integer> matchCountBox;
  private JComboBox<Difficulty> difficultyComboBox;

  private JRadioButton maleRadio;
  private JRadioButton femaleRadio;
  private JButtonTemplate confirmBtn;
  private JButtonTemplate homeBtn;
  private ButtonGroup genderGroup;
  private Player player;
  private Bot bot;

  public InfoPromptPanel(Player player, Bot bot) {
    this.player = player;
    this.bot = bot;
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
    matchCountBox.setSelectedIndex(0);
    difficultyComboBox.setSelectedIndex(0);
  }

  private void initComponents() {
    homeBtn = new JButtonTemplate();
    homeBtn.setIcon(new ImageIcon(getClass().getResource("/img/button/home.png")));
    homeBtn.set(20, 20, 66, 72, "", Color.BLACK, Color.WHITE);
    homeBtn.setFont(new Font("Ink Free", Font.PLAIN, 22));
    homeBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    homeBtn.setStyling(false);
    homeBtn.addActionListener(e -> {
      Frame.setMenu_trigger(true);
    });
    add(homeBtn);

    JPanel content = new JPanel();

    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setOpaque(false);
    content.setSize(500, 450);
    int x = (800 - content.getWidth()) / 2;
    content.setLocation(x, 130);

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

    JLabel matchLabel = createLabel("Choose number of matches:", 30);
    content.add(matchLabel);
    content.add(Box.createVerticalStrut(8));

    JPanel matchFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    matchFieldPanel.setOpaque(false);

    matchCountBox = new JComboBox<>();
    matchCountBox.setFont(new Font("Arial", Font.PLAIN, 18));
    matchCountBox.setPreferredSize(new Dimension(60, 30));

    for (int i = 1; i < 20; i += 2) {
      matchCountBox.addItem(i);
    }

    matchFieldPanel.add(matchCountBox);
    content.add(matchFieldPanel);
    content.add(Box.createVerticalStrut(8));

    JLabel difficultyLabel = createLabel("Choose bot difficulty:", 30);
    content.add(difficultyLabel);
    content.add(Box.createVerticalStrut(8));

    JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    difficultyPanel.setOpaque(false);

    difficultyComboBox = new JComboBox<>(Difficulty.values());
    difficultyComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
    difficultyComboBox.setPreferredSize(new Dimension(100, 30));
    difficultyPanel.add(difficultyComboBox);
    content.add(difficultyPanel);
    content.add(Box.createVerticalStrut(12));

    JPanel confirmPanel = new JPanel();
    confirmPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    confirmPanel.setOpaque(false);

    confirmBtn = new JButtonTemplate();
    confirmBtn.setIcon(new ImageIcon(getClass().getResource("/img/button/confirmBg.png")));
    confirmBtn.set(0, 0, 256, 60, "Confirm", Color.BLACK, Color.WHITE);
    confirmBtn.setMaximumSize(new Dimension(256, 60));
    confirmBtn.setFont(new Font("Ink Free", Font.PLAIN, 28));
    confirmBtn.setHorizontalTextPosition(SwingConstants.CENTER);
    confirmBtn.setStyling(false);

    confirmBtn.addActionListener(this);
    confirmPanel.add(confirmBtn);
    content.add(confirmPanel);

    this.add(content);
  }

  private void initBackground() {
    JLabel imgBg = new JLabel();
    imgBg.setIcon(new ImageIcon(getClass().getResource("/img/bg/prompt.png")));
    imgBg.setBounds(0, 0, 800, 800);
    this.add(imgBg);
    this.setComponentZOrder(imgBg,
	    this.getComponentCount() - 1); // Send to back
  }

  private JLabel createLabel(String text, int fontSize) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Arial", Font.BOLD, fontSize));
    label.setForeground(Color.WHITE);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
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

    int matchCount = (Integer) matchCountBox.getSelectedItem();
    Difficulty difficulty = (Difficulty) difficultyComboBox.getSelectedItem();

    player.setName(name);
    player.setGender(gender);
    player.loadRandomAvatar();

    bot.setDifficulty(difficulty);
    InfoPanel.setCountDownTime(difficulty);
    bot.loadRandomAvatar();
    bot.loadRandomMoveExcept(player.loadRandomMove());

    GamePanel.setTotalMatches(matchCount);
    InGamePanel.initNewGame();
    Frame.setNewGame_trigger(true);
  }
}
