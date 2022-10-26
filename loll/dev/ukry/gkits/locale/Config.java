package dev.ukry.gkits.locale;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.utils.files.ConfigFile;
import lombok.AllArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;

@AllArgsConstructor
public enum Config {

    //Coins System
    COINS_SYSTEM("system.coins"),
    COINS_REWARD("GIVE"),
    COINS_REWARD_AMOUNT(""),
    COINS_LOST_AMOUNT(""),

    CATEGORIES("gkits.categories"),

    DROP("gkits.drop"),

    GUI_CATEGORIES_ROWS("gkits.rows"),
 /*   GUI_CATEGORIES_ICON_DATA("GUI.CATEGORIES.ICON.DATA"),
    GUI_LIST_ROWS("GUI.LIST.CATEGORIES.ROWS")
  */
    ;

    private String path;
    private final FileConfiguration mainConfig = SharkGKits.getInstance().getConfig();

    public String getAsString() {
        return mainConfig.getString(path);
    }

    public <T> T getAsObject(Class<T> clazz) {
        return (T) mainConfig.get(path);
    }

    public double getAsDouble() {
        return mainConfig.getDouble(path);
    }

    public boolean getAsBoolean() {
        return mainConfig.getBoolean(path);
    }

    public int getAsInt() {
        return mainConfig.getInt(path);
    }
}