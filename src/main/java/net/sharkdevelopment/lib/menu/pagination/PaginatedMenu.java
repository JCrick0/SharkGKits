package net.sharkdevelopment.lib.menu.pagination;

import net.sharkdevelopment.lib.menu.Button;
import net.sharkdevelopment.lib.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class PaginatedMenu extends Menu {

  private int page = 1;

  public PaginatedMenu() {
    this.setUpdateAfterClick(false);
  }

  @Override
  public String getTitle(Player player) {
    return (
      this.getPrePaginatedTitle(player) + " - " + this.page + "/" + this.getPages(player)
    );
  }

  public final void modPage(Player player, int mod) {
    this.page += mod;
    this.getButtons().clear();
    this.openMenu(player);
  }

  public final int getPages(Player player) {
    int buttonAmount = this.getAllPagesButtons(player).size();
    return buttonAmount == 0
      ? 1
      : (int) Math.ceil((double) buttonAmount / (double) this.getMaxItemsPerPage(player));
  }

  @Override
  public final Map<Integer, Button> getButtons(Player player) {
    int minIndex = (int) (
      (double) (this.page - 1) * (double) this.getMaxItemsPerPage(player)
    );
    int maxIndex = (int) ((double) this.page * (double) this.getMaxItemsPerPage(player));
    HashMap<Integer, Button> buttons = new HashMap();
    buttons.put(0, new PageButton(-1, this));
    buttons.put(8, new PageButton(1, this));

    for (Map.Entry<Integer, Button> entry : this.getAllPagesButtons(player).entrySet()) {
      int ind = entry.getKey();
      if (ind >= minIndex && ind < maxIndex) {
        ind -=
          (int) ((double) this.getMaxItemsPerPage(player) * (double) (this.page - 1)) - 9;
        buttons.put(ind, entry.getValue());
      }
    }

    Map<Integer, Button> global = this.getGlobalButtons(player);
    if (global != null) {
      for (Map.Entry<Integer, Button> gent : global.entrySet()) {
        buttons.put(gent.getKey(), gent.getValue());
      }
    }

    return buttons;
  }

  public int getMaxItemsPerPage(Player player) {
    return 18;
  }

  public Map<Integer, Button> getGlobalButtons(Player player) {
    return null;
  }

  protected void bottomTopButtons(boolean full, Map buttons, ItemStack itemStack) {
    IntStream
      .range(0, this.getSize())
      .filter(slot -> buttons.get(slot) == null)
      .forEach(slot -> {
        if (
          slot < 9 ||
          slot > this.getSize() - 10 ||
          full &&
          (slot % 9 == 0 || (slot + 1) % 9 == 0)
        ) {
          buttons.put(
            slot,
            new Button() {
              @Override
              public ItemStack getButtonItem(Player player) {
                return itemStack;
              }
            }
          );
        }
      });
  }

  public abstract String getPrePaginatedTitle(Player var1);

  public abstract Map<Integer, Button> getAllPagesButtons(Player var1);

  public int getPage() {
    return this.page;
  }
}
