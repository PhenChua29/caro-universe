package panel;

import java.awt.BorderLayout;
import java.awt.Color;

import lib.JPanelTemplate;
import players.Bot;
import players.Player;

public class InfoPanel extends JPanelTemplate {
  private PlayerInfoPanel playerInfo;
  private PlayerInfoPanel botInfo;

  public InfoPanel(Player player, Bot bot) {
    playerInfo = new PlayerInfoPanel(player, false);
    botInfo = new PlayerInfoPanel(bot, true);

    this.set(0, 0, 800, 100, new Color(131, 206, 255));
    this.setLayout(new BorderLayout());
    this.setOpaque(false);

    this.add(playerInfo, BorderLayout.WEST);
    this.add(botInfo, BorderLayout.EAST);
  }

  public void update() {
    playerInfo.update();
    botInfo.update();
  }
}
