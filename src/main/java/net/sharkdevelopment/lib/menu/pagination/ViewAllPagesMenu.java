package net.sharkdevelopment.lib.menu.pagination;

import lombok.NonNull;
import net.sharkdevelopment.lib.menu.Button;
import net.sharkdevelopment.lib.menu.Menu;
import net.sharkdevelopment.lib.menu.buttons.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ViewAllPagesMenu extends Menu {

  @NonNull
  PaginatedMenu menu;

  @Override
  public String getTitle(Player player) {
    return "Jump to page";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    HashMap<Integer, Button> buttons = new HashMap();
    buttons.put(0, new BackButton(this.menu));
    int index = 10;

    for (int i = 1; i <= this.menu.getPages(player); ++i) {
      buttons.put(index++, new JumpToPageButton(i, this.menu, this.menu.getPage() == i));
      if ((index - 8) % 9 == 0) {
        index += 2;
      }
    }

    return buttons;
  }

  @Override
  public boolean isAutoUpdate() {
    return true;
  }

  public ViewAllPagesMenu(@NonNull PaginatedMenu menu) {
    if (menu == null) {
      throw new NullPointerException("menu is marked non-null but is null");
    } else {
      this.menu = menu;
    }
  }

  @NonNull
  public PaginatedMenu getMenu() {
    return this.menu;
  }
}
