package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.utils.CC;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class ListGkitArgument {

    public static boolean execute(Player player, String[] args, String label) {
        player.sendMessage(CC.CHAT_BAR);
        if(player.isOp()) {
            for(Gkit gkit : SharkGKits.getInstance().getGkitsHandler().getGkits().values().stream().filter(g -> !g.isEnabled()).collect(Collectors.toList())) {
                player.sendMessage(CC.RED + gkit.getName());
            }
        }
        for(Gkit gkit : SharkGKits.getInstance().getGkitsHandler().getGkits().values().stream().filter(Gkit::isEnabled).collect(Collectors.toList())) {
            player.sendMessage(CC.AQUA + gkit.getName());
        }
        player.sendMessage(CC.CHAT_BAR);
        return true;
    }
}