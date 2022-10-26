package dev.ukry.gkits.locale;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.utils.CC;
import dev.ukry.gkits.utils.files.ConfigFile;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
public enum Locale {

    //Main Section
    PREFIX("gkits.prefix"),

    /* Gkits - Commands */
    //Buy Argument
    BUY_ARGUMENT_SUCCESSFULLY("gkits.buy.successfully"),
    BUY_ARGUMENT_NO_MONEY("gkits.buy.no-money"),
    BUY_ARGUMENT_NO_EXIST("gkits.buy.no-exist"),
    //Create Category Argument
    CREATE_CATEGORY_ARGUMENT_SUCCESSFULLY("gkits.create-category.successfully"),
    CREATE_CATEGORY_ARGUMENT_EXIST("gkits.create-category.exist"),
    //Create Gkit Argument
    CREATE_GKIT_ARGUMENT_SUCCESSFULLY("gkits.create-kit.successfully"),
    CREATE_GKIT_ARGUMENT_EXIST("gkits.create-kit.exist"),
    //Edit Category Argument
    EDIT_CATEGORY_ARGUMENT_NO_EXIST(""),
    //Edit Gkit Argument
    EDIT_GKIT_ARGUMENT(""),
    //Give Argument
    GIVE_ARGUMENT(""),
    //List Argument
    LIST_ARGUMENT(""),
    //Reload Argument
    RELOAD_ARGUMENT(""),

    //Commands
    ALREADY_EXIST("gkits.already-exist"),

    //Successfully
    SUCCESSFULLY_CREATED("gkits.created"),
    DELETED("gkits.deleted"),


    SUCCESSFULLY_EQUIPED("gkits.equiped"),
    NO_EXIST("gkits.no-exist"),
    CATEGORY_NOT_FOUND("gkits.category-not-found"),
    NO_FOUNDS("gkits.coins.no-founds"),
    SUCCESSFULLY_BUYED("gkits.coins.successfully"),

    //Gui´s Section
    GUI_CATEGORIES_TITLE("gkits.gui.categories.title"),
    GUI_LIST_TITLE("gkits.gui.list.title"),
    GUI_PREVIEW_TITLE("gkits.gui.preview.title"),

    AVAILABLE("gkits.lore.available"),
    NO_AVAILABLE("gkits.lore.no-available"),
    NO_PERMISSION("gkits.lore.no-permission"),

    COINS_BALANCE_MESSAGE("gkits.coins.balance"),

    COINS_ADD_MESSAGE("gkits.coins."),
    COINS_LOST_MESSAGE("gkits.coins."),
    COINS_NO_CANT_LOST_MESSAGE("gkits.coins."),

    NO_HAVE_OWN_GKIT("gkits.no-have"),

    INVENTORY_FULL("gkits.inventory-full");

    private final String path;
    private final ConfigFile config = SharkGKits.getInstance().getMessagesConfig();
    private Player player;
    private List<Function<? super String, ? super String>> replaces;
    private Predicate<? super Player> predicate;
    private String toSend;

    public List<String> getStringList() {
        List<String> toReturn = CC.translate(config.getStringList(path));
        if(replaces != null && !replaces.isEmpty()) {
            for(int i = 0; i < toReturn.size(); i++) {
                int finalI = i;
                replaces.forEach(r -> toReturn.set(finalI, (String) r.apply(toReturn.get(finalI))));
            }
        }
        return toReturn;
    }

    public String getString() {
        final String[] toReturn = {CC.translate(config.getString(path))};
        if(replaces != null && !replaces.isEmpty()) {
            replaces.forEach(r -> toReturn[0] = (String) r.apply(toReturn[0]));
        }
        return toReturn[0];
    }

    public Locale addPrefix() {
        addReplace(s -> s.replace("%prefix%", Locale.PREFIX.getString()));
        return this;
    }

    public Locale setPlayer(Player iPlayer) {
        player = iPlayer;
        return this;
    }

    public Locale filter(Predicate<? super Player> iPredicate) {
        predicate = iPredicate;
        return this;
    }

    public Locale addReplace(Function<? super String, ? super String> replace) {
        if(replaces == null) replaces = new ArrayList<>();
        replaces.add(replace);
        return this;
    }

    public void send() {
        boolean send = true;
        if(predicate != null) {
            if(!predicate.test(player)) send = false;
        }
        if(!send) return;
        toSend = CC.translate(config.getString(path));
        if (replaces != null && !replaces.isEmpty()) {
            replaces.forEach(f -> toSend = (String) f.apply(toSend));
        }
        player.sendMessage(toSend);
    }
}