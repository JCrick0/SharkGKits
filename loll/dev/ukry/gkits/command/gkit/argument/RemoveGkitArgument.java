package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.handler.GkitsHandler;
import dev.ukry.gkits.locale.Locale;
import org.bukkit.entity.Player;

public class RemoveGkitArgument {

    public static boolean execute(Player player, String[] args, String label) {
        GkitsHandler handler = SharkGKits.getInstance().getGkitsHandler();
        if(handler.getGkit(args[1]) != null) {
            handler.removeGkit(args[1]);
            Locale.DELETED
                    .setPlayer(player)
                    .addReplace(s -> s.replace("%prefix%", Locale.PREFIX.getString()))
                    .addReplace(s -> s.replace("%gkit%", args[1]))
                    .send();
            return true;
        } else {
            Locale.NO_EXIST
                    .setPlayer(player)
                    .addReplace(s -> s.replace("%prefix%", Locale.PREFIX.getString()))
                    .addReplace(s -> s.replace("%gkit%", args[1]))
                    .send();
            return false;
        }
    }
}