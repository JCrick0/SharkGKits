package net.sharkdevelopment.gkits.commands;

import com.google.common.collect.Maps;
import net.sharkdevelopment.gkits.managers.enchants.CustomEnchant;
import net.sharkdevelopment.lib.CC;
import net.sharkdevelopment.lib.command.BaseCommand;
import net.sharkdevelopment.lib.maker.ItemMaker;
import net.sharkdevelopment.lib.menu.Button;
import net.sharkdevelopment.lib.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class CustomEnchantCommand extends BaseCommand {

    public CustomEnchantCommand() {
        super("customenchant", "shark.command.customenchant");
        this.setAliases(
                Arrays.asList(
                        "ce",
                        "enchantcustom",
                        "enchantc"
                )
        );
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("&cUsage: /customenchant give <player>");
                return;
            }
            switch (args[0].toLowerCase()) {
                case "give":
                    if (args.length == 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        assert target != null;

                        new Menu() {
                            @Override
                            public String getTitle(Player player) {
                                return CC.translate("&aSelect customenchant to give");
                            }

                            @Override
                            public Map<Integer, Button> getButtons(Player player) {
                                Map<Integer, Button> buttons = Maps.newHashMap();

                                int i = 0;
                                for (CustomEnchant customEnchant : CustomEnchant.getCustomEnchants()) {
                                    buttons.put(i, new Button() {
                                        @Override
                                        public ItemStack getButtonItem(Player player) {
                                            return customEnchant.getItemStack();
                                        }

                                        @Override
                                        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                                            ItemStack item = customEnchant.getItemStack().clone();
                                            ItemMeta meta = item.getItemMeta();
                                            meta.setLore(
                                                    Arrays.asList(
                                                            "&7&m--------------------",
                                                            "&fThis is a custom enchant, " +
                                                                    "right click to armor piece to enchant with this!",
                                                            "&7&m--------------------"
                                                    )
                                            );
                                            item.setItemMeta(meta);
                                            target.getInventory().addItem(item);
                                        }
                                    });
                                    i++;
                                }
                                return buttons;
                            }
                        }.openMenu(player);
                    }
                    break;
            }
        } else {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(CC.translate("&cPlayer doesn't exists or isn't online!"));
                        return;
                    }

                    CustomEnchant enchant = CustomEnchant.getByName(args[2]);
                    if (enchant == null) {
                        sender.sendMessage(CC.translate("&cNo enchant found with that name!"));
                        return;
                    }

                    target.getInventory().addItem(
                            ItemMaker.of(Material.ENCHANTED_BOOK)
                                    .displayName("&bCustom Enchant &7(" + enchant.getName() + ")")
                                    .lore(Collections.singletonList(enchant.getName()))
                                    .build()
                    );
                    sender.sendMessage(CC.translate("&aSuccessful gived custom enchant book to &7''&f" + target.getName() + "&7''"));
                }
            }
        }
    }
}
