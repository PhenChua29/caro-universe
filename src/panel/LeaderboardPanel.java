package panel;

import constants.PanelType;
import data.Record;
import data.RecordManager;
import frame.Frame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class LeaderboardPanel extends JPanel implements ActionListener {

  private final int TABLE_WIDTH = 600;
  private final int TABLE_HEIGHT = 285;

  private Image backgroundImage;
  private JButton returnButton;
  private JTable leaderboardTable;
  private DefaultTableModel tableModel;

  public LeaderboardPanel() {
    backgroundImage = new ImageIcon(getClass().getResource("/img/bg/prompt.png")).getImage();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(false);
    setPreferredSize(new Dimension(800, 800));
    add(Box.createRigidArea(new Dimension(0, 14)));

    // Header
    JPanel headerPanel = new JPanel(null);
    headerPanel.setOpaque(false);
    headerPanel.setPreferredSize(new Dimension(800, 100));
    headerPanel.setMaximumSize(new Dimension(800, 100));

    // Return Button
    returnButton = new JButton(new ImageIcon(getClass().getResource("/img/button/home.png")));
    returnButton.setBounds(24, 20, 66, 72);
    returnButton.setContentAreaFilled(false);
    returnButton.setBorderPainted(false);
    returnButton.setFocusPainted(false);
    returnButton.addActionListener(this);
    headerPanel.add(returnButton);

    JLabel title = new JLabel("Leaderboard", SwingConstants.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 30));
    title.setForeground(Color.WHITE);
    title.setBounds(0, 30, 800, 40);
    headerPanel.add(title);

    add(headerPanel);
    add(Box.createRigidArea(new Dimension(0, 56)));

    // Table setup
    String[] columns = {"Rank", "Player Name", "Total Score", "Games Played"};
    tableModel = new DefaultTableModel(columns, 0) {
      public boolean isCellEditable(int row, int column) {
	return false;
      }
    };

    leaderboardTable = new JTable(tableModel) {
      @Override
      public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
	Component c = super.prepareRenderer(renderer, row, column);
	if (c instanceof JComponent) {
	  ((JComponent) c).setOpaque(true);
	  if (row == 0) {
	    c.setForeground(Color.decode("#fdba14"));
	    c.setFont(new Font("Arial", Font.BOLD, 14));
	  } else if (row == 1) {
	    c.setForeground(Color.decode("#ef4a4a"));
	    c.setFont(new Font("Arial", Font.BOLD, 14));
	  } else if (row == 2) {
	    c.setForeground(Color.GREEN);
	    c.setFont(new Font("Arial", Font.BOLD, 14));
	  } else {
	    c.setForeground(Color.WHITE);
	  }

	  if (row % 2 != 0) {
	    c.setBackground(new Color(240, 240, 240, 50));
	  } else {
	    ((JComponent) c).setOpaque(false);
	  }
	}
	return c;
      }
    };

    leaderboardTable.setOpaque(false);
    leaderboardTable.setForeground(Color.WHITE);
    leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 14));
    leaderboardTable.setRowHeight(28);
    leaderboardTable.setShowGrid(false);
    leaderboardTable.setIntercellSpacing(new Dimension(0, 1));

    leaderboardTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
    leaderboardTable.getTableHeader().setReorderingAllowed(false);
    leaderboardTable.getTableHeader().setOpaque(false);

    // Center alignment for numeric values and rank
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    leaderboardTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    leaderboardTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    leaderboardTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

    // Add padding to player name
    DefaultTableCellRenderer leftPadRenderer = new DefaultTableCellRenderer() {
      @Override
      public void setValue(Object value) {
	setText("    " + value);
      }
    };
    leaderboardTable.getColumnModel().getColumn(1).setCellRenderer(leftPadRenderer);

    leaderboardTable.setRowSelectionAllowed(false);
    leaderboardTable.setColumnSelectionAllowed(false);
    leaderboardTable.setCellSelectionEnabled(false);
    leaderboardTable.setFocusable(false);
    leaderboardTable.setEnabled(false);

    // Panel wrapping the table for background styling
    JPanel tableContainer = new JPanel(new BorderLayout());
    tableContainer.setOpaque(false);
    tableContainer.setBorder(new EmptyBorder(10, 24, 10, 10));
    tableContainer.setMaximumSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT + 40));
    tableContainer.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT + 40));

    JScrollPane scrollPane = new JScrollPane(leaderboardTable);
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setBorder(null);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setPreferredSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));
    scrollPane.setMaximumSize(new Dimension(TABLE_WIDTH, TABLE_HEIGHT));

    tableContainer.add(scrollPane);
    add(tableContainer);

    loadRecords();
  }

  private void loadRecords() {
    RecordManager manager = new RecordManager();
    ArrayList<Record> records = manager.getData();
    tableModel.setRowCount(0);

    int maxEntries = Math.min(records.size(), 10);
    for (int i = 0; i < maxEntries; i++) {
      Record r = records.get(records.size() - 1 - i);
      String rank = "#" + (i + 1);
      tableModel.addRow(new Object[]{rank, r.getPlayerName(), r.getTotalScore(), r.getTotalGames()});
    }
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
