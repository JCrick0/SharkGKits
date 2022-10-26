package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.utils.CC;
import org.bukkit.entity.Player;

public class ReloadArgument {

    public static boolean execute(Player player, String[] args, String label) {
        SharkGKits.getInstance().reloadConfig();
        SharkGKits.getInstance().getCategoriesConfig().reload();
        SharkGKits.getInstance().getGkitsConfig().reload();
        SharkGKits.getInstance().getMessagesConfig().reload();
        SharkGKits.getInstance().getProfilesConfig().reload();
        SharkGKits.getInstance().getDataConfig().reload();
        player.sendMessage(CC.GREEN + "Reloaded!");
        return false;
    }
}