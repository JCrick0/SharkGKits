package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.cooldown.CooldownHandler;
import dev.ukry.gkits.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ResetCooldownArgument {

    public static boolean execute(Player player, String[] args, String label) {
        if(args.length == 3) {
            if(args[1].equalsIgnoreCase("all")) {
                for(UUID uuid : CooldownHandler.map.keySet()) {
                    if(!CooldownHandler.hasCooldown(uuid, args[2])) continue;
                    CooldownHandler.map.remove(uuid);
                }
                player.sendMessage(CC.GREEN + "Successfully reset to all");
            } else {
                Player target = Bukkit.getPlayerExact(args[1]);
                if(target == null) {
                    player.sendMessage(CC.RED + "That player not exist or no is online!");
                } else {
                    if(CooldownHandler.hasCooldown(target.getUniqueId(), args[2])) {
                        CooldownHandler.map.remove(target.getUniqueId());
                    }
                    player.sendMessage(CC.GREEN + "Successfully reset cooldon to " + args[1]);
                }
            }
        } else {
            player.sendMessage(CC.RED + "Usage: /" + label + " resetcooldown <user/all> <gkit>");
        }
        return true;
    }
}