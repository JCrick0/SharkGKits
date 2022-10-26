package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.ui.categories.CategoriesListUI;
import dev.ukry.gkits.ui.gkit.GkitListUI;
import dev.ukry.gkits.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveArgument {

    public static boolean execute(Player player, String[] args, String label) {
        if(args.length != 2) {
            player.sendMessage(CC.RED + "Usage: /" + label + " give <player>");
            return false;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if(target == null) {
            player.sendMessage(CC.RED + "That player not exist or no is online!");
        } else {
            new CategoriesListUI(category -> new GkitListUI(category, (event, gkit) -> {
                for (ItemStack stack : gkit.getArmorContents()) {
                    if (stack == null || stack.getType() == Material.AIR) continue;
                    int size = target.getInventory().getSize();
                    if (size < 54) {
                        target.getInventory().addItem(stack);
                    } else {
                        target.getWorld().dropItem(target.getLocation().add(0, 1, 0), stack);
                    }
                    player.sendMessage(CC.GREEN + "Successfully gived!");
                }
            }, player).open(player)).open(player);
        }
        return true;
    }
}