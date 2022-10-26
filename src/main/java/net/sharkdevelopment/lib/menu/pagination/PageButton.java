package net.sharkdevelopment.lib.menu.pagination;

import net.sharkdevelopment.lib.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PageButton extends Button {

  private int mod;
  private PaginatedMenu menu;

  @Override
  public ItemStack getButtonItem(Player player) {
    ItemStack itemStack = new ItemStack(Material.CARPET);
    ItemMeta itemMeta = itemStack.getItemMeta();
    if (this.hasNext(player)) {
      itemMeta.setDisplayName(
        this.mod > 0 ? ChatColor.GREEN + "Next page" : ChatColor.RED + "Previous page"
      );
    } else {
      itemMeta.setDisplayName(
        ChatColor.GRAY + (this.mod > 0 ? "Last page" : "First page")
      );
    }

    itemMeta.setLore(
      Arrays.asList(
        "",
        ChatColor.GREEN + "Right click to",
        ChatColor.GREEN + "jump to a page"
      )
    );
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }

  @Override
  public void clicked(Player player, int i, ClickType clickType, int hb) {
    if (clickType == ClickType.RIGHT) {
      new ViewAllPagesMenu(this.menu).openMenu(player);
      playNeutral(player);
    } else if (this.hasNext(player)) {
      this.menu.modPage(player, this.mod);
      Button.playNeutral(player);
    } else {
      Button.playFail(player);
    }
  }

  private boolean hasNext(Player player) {
    int pg = this.menu.getPage() + this.mod;
    return pg > 0 && this.menu.getPages(player) >= pg;
  }

  public PageButton(int mod, PaginatedMenu menu) {
    this.mod = mod;
    this.menu = menu;
  }
}
