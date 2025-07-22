package panel;

import constants.PanelType;
import frame.Frame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

public class InstructionPanel extends JPanel {

  private JLabel imageLabel;
  private JButton nextButton, backButton, returnButton;
  private ArrayList<ImageIcon> images;
  private int currentIndex = 0;
  private JLayeredPane layeredPane;

  public InstructionPanel() {
    setPreferredSize(new Dimension(800, 800));
    setLayout(new BorderLayout());

    layeredPane = new JLayeredPane();
    layeredPane.setPreferredSize(new Dimension(800, 800));
    this.add(layeredPane, BorderLayout.CENTER);

    init_background();
    init_images();
    init_controls();
    init_keybindings();
  }

  private void init_background() {
    JLabel imgBg = new JLabel(new ImageIcon(getClass().getResource("/img/bg/3.png")));
    imgBg.setBounds(0, 0, 800, 800);
    layeredPane.add(imgBg, JLayeredPane.DEFAULT_LAYER);
  }

  private void init_images() {
    images = new ArrayList<>();

    for (int i = 1; i <= 5; i++) {
      try {
	BufferedImage img = ImageIO.read(getClass().getResource("/img/instructions/" + i + ".png"));
	images.add(new ImageIcon(img));
      } catch (IOException | IllegalArgumentException ex) {
	JOptionPane.showMessageDialog(this, "Failed to load instruction images!");
	ex.printStackTrace();
	return;
      }
    }

    imageLabel = new JLabel();
    imageLabel.setBounds(0, 0, 800, 800);
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    layeredPane.add(imageLabel, JLayeredPane.PALETTE_LAYER);

    updateImage();
  }

  private void init_controls() {
    // Back Button on the far left
    backButton = new JButton(new ImageIcon(getClass().getResource("/img/button/left.png")));
    backButton.setBounds(12, 370, 66, 72);
    backButton.setContentAreaFilled(false);
    backButton.setBorderPainted(false);
    backButton.setFocusPainted(false);
    backButton.addActionListener(e -> showPreviousImage());
    layeredPane.add(backButton, JLayeredPane.MODAL_LAYER);

    // Next Button on the far right
    nextButton = new JButton(new ImageIcon(getClass().getResource("/img/button/right.png")));
    nextButton.setBounds(722, 370, 66, 72);
    nextButton.setContentAreaFilled(false);
    nextButton.setBorderPainted(false);
    nextButton.setFocusPainted(false);
    nextButton.addActionListener(e -> showNextImage());
    layeredPane.add(nextButton, JLayeredPane.MODAL_LAYER);

    returnButton = new JButton(new ImageIcon(getClass().getResource("/img/button/home.png")));
    returnButton.setBounds(12, 710, 66, 72);
    returnButton.setContentAreaFilled(false);
    returnButton.setBorderPainted(false);
    returnButton.setFocusPainted(false);
    returnButton.addActionListener(e -> {
      Frame.switchPanel(PanelType.MENU);

      new Timer(600, evt -> {
	resetToFirstImage();
	((Timer) evt.getSource()).stop();
      }).start();
    });

    layeredPane.add(returnButton, JLayeredPane.MODAL_LAYER);

    updateButtons();
  }

  private void init_keybindings() {
    InputMap im = layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = layeredPane.getActionMap();

    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "goLeft");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "goRight");

    am.put("goLeft", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
	showPreviousImage();
      }
    });

    am.put("goRight", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
	showNextImage();
      }
    });
  }

  private void showNextImage() {
    if (currentIndex < images.size() - 1) {
      currentIndex++;
      updateImage();
    }
    updateButtons();
  }

  private void showPreviousImage() {
    if (currentIndex > 0) {
      currentIndex--;
      updateImage();
    }
    updateButtons();
  }

  private void updateImage() {
    ImageIcon icon = images.get(currentIndex);
    Image img = icon.getImage().getScaledInstance(800, 800, Image.SCALE_SMOOTH);
    imageLabel.setIcon(new ImageIcon(img));
  }

  private void updateButtons() {
    backButton.setEnabled(currentIndex > 0);
    nextButton.setEnabled(currentIndex < images.size() - 1);
  }

  private void resetToFirstImage() {
    currentIndex = 0;
    updateImage();
    updateButtons();
  }
}
