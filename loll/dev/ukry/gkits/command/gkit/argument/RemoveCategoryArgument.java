package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.utils.CC;
import org.bukkit.entity.Player;

public class RemoveCategoryArgument {

    public static boolean execute(Player player, String[] args, String label) {
        if(args.length == 2) {
            if(args[1].equalsIgnoreCase("default")) {
                player.sendMessage(CC.RED + "You no can remove this category!");
                return false;
            }
            if(Category.valueOf(args[1]) != null) {
                Category.valueOf(args[1]).remove();
                player.sendMessage(CC.GREEN + "Successfully removed!");
            } else {
                Locale.CATEGORY_NOT_FOUND
                        .setPlayer(player)
                        .addReplace(s -> s.replace("%prefix%", Locale.PREFIX.getString()))
                        .send();
            }
            return false;
        } else {
            player.sendMessage(CC.YELLOW + "/" + label + " createcategory <category>");
        }
        return true;
    }
}