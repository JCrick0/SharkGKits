package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.handler.GkitsHandler;
import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.ui.categories.CategoriesListUI;
import dev.ukry.gkits.ui.gkit.GkitListUI;
import dev.ukry.gkits.ui.gkit.edit.GKitEditUI;
import dev.ukry.gkits.utils.CC;
import org.bukkit.entity.Player;

public class EditGkitArgument {

    public static boolean execute(Player player, String[] args, String label) {
        GkitsHandler handler = SharkGKits.getInstance().getGkitsHandler();
        if(args.length == 1) {
            new CategoriesListUI(category -> new GkitListUI(category, gkit -> new GKitEditUI(gkit).open(player)).open(player)).open(player);
            return true;
        } else if(args.length == 2) {
            String gkitName = args[1];
            Gkit gkit = handler.getGkit(gkitName);
            if(gkit != null) {
                new GKitEditUI(gkit).open(player);
                return true;
            } else {
                Locale.NO_EXIST
                        .setPlayer(player)
                        .addReplace(s -> s.replace("%prefix%", Locale.PREFIX.getString()))
                        .addReplace(s -> s.replace("%gkit%", gkitName))
                        .send();
                return false;
            }
        } else {
            player.sendMessage(CC.YELLOW + "/" + label + " edit <nothing/name>");
            return false;
        }
    }
}