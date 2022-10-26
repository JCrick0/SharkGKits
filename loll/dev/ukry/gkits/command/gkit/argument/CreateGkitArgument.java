package dev.ukry.gkits.command.gkit.argument;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.manager.events.GkitCreationEvent;
import dev.ukry.gkits.handler.GkitsHandler;
import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.utils.CC;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CreateGkitArgument {

    public static boolean execute(Player player, String[] args, String label) {
        GkitsHandler handler = SharkGKits.getInstance().getGkitsHandler();
        if(args.length != 2){
            player.sendMessage(CC.YELLOW + "/" + label + " create <name>");
            return false;
        } else {
            String gkitName = args[1];
            Gkit gkit = handler.getGkit(gkitName);
            if(gkit == null) {
                List<Gkit> gkits = handler
                        .getGkits()
                        .values()
                        .stream()
                        .filter(g -> g.getCategory().equalsIgnoreCase("DEFAULT"))
                        .sorted(Collections.reverseOrder(Comparator.comparingInt(Gkit::getSlot)))
                        .collect(Collectors.toList());
                ItemStack[] displayContents = new ItemStack[54];
                ItemStack[] armorContents = player.getInventory().getArmorContents();
                ItemStack[] contents = player.getInventory().getContents();
                System.arraycopy(contents, 0, displayContents, 0, contents.length);
                for(int i = 0, j = 46; i < 4; i++, j++) {
                    ItemStack a = armorContents[i];
                    if(a == null || a.getType() == Material.AIR) {
                        displayContents[j] = new ItemBuilder(Material.STAINED_GLASS_PANE).data(7).name(" ").build();
                    } else {
                        displayContents[j] = armorContents[i];
                    }
                }
                for(int i = 36; i < 46; i++) {
                    displayContents[i] = new ItemBuilder(Material.STAINED_GLASS_PANE).data(14).name(" ").build();
                }
                displayContents[50] = new ItemBuilder(Material.STAINED_GLASS_PANE).data(14).name(" ").build();
                displayContents[51] = new ItemBuilder(Material.STAINED_GLASS_PANE).data(14).name(" ").build();
                displayContents[53] = new ItemBuilder(Material.STAINED_GLASS_PANE).data(14).name(" ").build();
                displayContents[52] = new ItemBuilder(Material.REDSTONE).name(CC.RED + "Back").build();
                Gkit nGkit = new Gkit(gkitName)
                        .setEnabled(true)
                        .setDescription(Locale.AVAILABLE.addReplace(s -> s.replace("%COOLDOWN%", "600 Seconds")).getStringList().toArray(new String[0]))
                        .setPermission("GKIT." + gkitName)
                        .setPrice(200D)
                        .setCooldown(600)
                        .setCategory("default")
                        .setIcon(new ItemBuilder(Material.PAPER).name(CC.PINK + gkitName + " Kit").build())
                        .setSlot((gkits.isEmpty()) ? 0 : gkits.get(0).getSlot() + 1)
                        .setContents(contents)
                        .setArmorContents(armorContents)
                        .setDisplayContents(displayContents);
                handler.getGkits().put(gkitName, nGkit);
                new GkitCreationEvent(nGkit).call();
                Locale.CREATE_GKIT_ARGUMENT_SUCCESSFULLY
                        .setPlayer(player)
                        .addPrefix()
                        .addReplace(s -> s.replace("%gkit%", gkitName))
                        .send();
                return true;
            } else {
                Locale.CREATE_GKIT_ARGUMENT_EXIST
                        .setPlayer(player)
                        .addPrefix()
                        .addReplace(s -> s.replace("%gkit%", gkitName))
                        .send();
                return false;
            }
        }
    }
}