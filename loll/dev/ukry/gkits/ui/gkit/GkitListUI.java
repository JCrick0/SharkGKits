package dev.ukry.gkits.ui.gkit;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.cooldown.CooldownHandler;
import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.profile.Profile;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.utils.time.TimeFormat;
import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GkitListUI extends FastInv {

    public GkitListUI(Category category, BiConsumer<InventoryClickEvent, Gkit> event, Player player) {
        super(category.getInvSize(), Locale.GUI_LIST_TITLE.addReplace(s -> s.replace("%gategory%", category.getName())).getString());
        Profile profile = SharkGKits.getInstance().getProfileHandler().getProfile(player.getUniqueId());
        for(Gkit gkit : SharkGKits.getInstance().getGkitsHandler().getGkits().values().stream().filter(Gkit::isEnabled).filter(g -> g.getCategory().equalsIgnoreCase(category.getName())).collect(Collectors.toList())) {
            ItemStack nIs = gkit.getIcon();
            ItemMeta meta = nIs.getItemMeta();
            if(profile.hasGkit(gkit)) {
                if(CooldownHandler.hasCooldown(player.getUniqueId(), gkit.getName())) {
                    meta.setLore(Locale.NO_AVAILABLE
                            .addReplace(s -> (s.contains("%D") && s.contains("%H") && s.contains("%M") && s.contains("%S")) ?
                                    s.replace("%D", TimeFormat.DD(gkit.getCooldown())
                                            .replace("%H", TimeFormat.HH(gkit.getCooldown()))
                                            .replace("%M", TimeFormat.MM(gkit.getCooldown()))
                                            .replace("%S", TimeFormat.SS(gkit.getCooldown()))) :
                                    (s.contains("%H") && s.contains("%M") && s.contains("%S")) ?
                                            s.replace("%H", TimeFormat.HH1(gkit.getCooldown()))
                                                    .replace("%M", TimeFormat.MM(gkit.getCooldown()))
                                                    .replace("%S", TimeFormat.SS(gkit.getCooldown())) :
                                            (s.contains("%M") && s.contains("%S")) ?
                                                    s.replace("%M", TimeFormat.MM1(gkit.getCooldown()))
                                                            .replace("%S", TimeFormat.SS(gkit.getCooldown())) :
                                                    s.replace("%S", String.valueOf(gkit.getCooldown())))
                            .addReplace(s -> s.replace("%COOLDOWN_LEFT%", CooldownHandler.getCooldown(player.getUniqueId(), gkit.getName())))
                            .getStringList()
                    );
                } else {
                    meta.setLore(
                            Arrays.asList(gkit.getDescription()).stream().map(s ->
                                (s.contains("%D") && s.contains("%H") && s.contains("%M") && s.contains("%S")) ?
                                        s.replace("%D", TimeFormat.DD(gkit.getCooldown())
                                                .replace("%H", TimeFormat.HH(gkit.getCooldown()))
                                                .replace("%M", TimeFormat.MM(gkit.getCooldown()))
                                                .replace("%S", TimeFormat.SS(gkit.getCooldown()))) :
                                        (s.contains("%H") && s.contains("%M") && s.contains("%S")) ?
                                                s.replace("%H", TimeFormat.HH1(gkit.getCooldown()))
                                                        .replace("%M", TimeFormat.MM(gkit.getCooldown()))
                                                        .replace("%S", TimeFormat.SS(gkit.getCooldown())) :
                                                (s.contains("%M") && s.contains("%S")) ?
                                                        s.replace("%M", TimeFormat.MM1(gkit.getCooldown()))
                                                                .replace("%S", TimeFormat.SS(gkit.getCooldown())) :
                                                        s.replace("%S", String.valueOf(gkit.getCooldown()))
                            ).collect(Collectors.toList()));
                }
            } else {
                meta.setLore(Locale.NO_PERMISSION
                        .addReplace(s -> (s.contains("%D") && s.contains("%H") && s.contains("%M") && s.contains("%S")) ?
                                s.replace("%D", TimeFormat.DD(gkit.getCooldown())
                                        .replace("%H", TimeFormat.HH(gkit.getCooldown()))
                                        .replace("%M", TimeFormat.MM(gkit.getCooldown()))
                                        .replace("%S", TimeFormat.SS(gkit.getCooldown()))) :
                                (s.contains("%H") && s.contains("%M") && s.contains("%S")) ?
                                        s.replace("%H", TimeFormat.HH1(gkit.getCooldown()))
                                                .replace("%M", TimeFormat.MM(gkit.getCooldown()))
                                                .replace("%S", TimeFormat.SS(gkit.getCooldown())) :
                                        (s.contains("%M") && s.contains("%S")) ?
                                                s.replace("%M", TimeFormat.MM1(gkit.getCooldown()))
                                                        .replace("%S", TimeFormat.SS(gkit.getCooldown())) :
                                                s.replace("%S", String.valueOf(gkit.getCooldown())))
                        .getStringList()
                );
            }
            nIs.setItemMeta(meta);
            setItem(gkit.getSlot(), nIs, e -> event.accept(e, gkit));
        }
        if(!SharkGKits.getInstance().getConfig().getBoolean("gkits.refill-glass.enabled")) return;
        ItemStack[] contents = getInventory().getContents();
        for(int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if(stack == null) {
                contents[i] = new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name(" ")
                        .data(SharkGKits.getInstance().getConfig().getInt("gkits.refill-glass.data"))
                        .build();
            } else {
                if(stack.getType() == Material.AIR) {
                    contents[i] = new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .name(" ")
                            .data(SharkGKits.getInstance().getConfig().getInt("gkits.refill-glass.data"))
                            .build();
                }
            }
        }
        getInventory().setContents(contents);
    }

    public GkitListUI(Category category, Consumer<Gkit> function) {
        super(category.getInvSize(), Locale.GUI_LIST_TITLE.addReplace(s -> s.replace("%gategory%", category.getName())).getString());
        for(Gkit gkit : SharkGKits.getInstance().getGkitsHandler().getGkits().values().stream().filter(g -> g.getCategory().equalsIgnoreCase(category.getName())).collect(Collectors.toList())) {
            setItem(gkit.getSlot(), gkit.getIcon(), event -> function.accept(gkit));
        }
        if(!SharkGKits.getInstance().getConfig().getBoolean("gkits.refill-glass.enabled")) return;
        ItemStack[] contents = getInventory().getContents();
        for(int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if(stack == null) {
                contents[i] = new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .name(" ")
                        .data(SharkGKits.getInstance().getConfig().getInt("gkits.refill-glass.data"))
                        .build();
            } else {
                if(stack.getType() == Material.AIR) {
                    contents[i] = new ItemBuilder(Material.STAINED_GLASS_PANE)
                            .name(" ")
                            .data(SharkGKits.getInstance().getConfig().getInt("gkits.refill-glass.data"))
                            .build();
                }
            }
        }
        getInventory().setContents(contents);

    }
}