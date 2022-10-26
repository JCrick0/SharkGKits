package net.sharkdevelopment.lib.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MenuUpdateTask implements Runnable {

  public void run() {
    Menu.currentlyOpenedMenus.forEach((key, value) -> {
      Player player = Bukkit.getPlayer(key);
      if (player != null && value.isAutoUpdate()) {
        value.openMenu(player);
      }
    });
  }
}
