package net.sharkdevelopment.gkits;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.sharkdevelopment.gkits.listeners.PlayerListener;
import net.sharkdevelopment.gkits.managers.enchants.CustomEnchant;
import net.sharkdevelopment.gkits.managers.event.ArmorListener;
import net.sharkdevelopment.gkits.managers.gkit.Gkit;
import net.sharkdevelopment.gkits.managers.mongo.MongoManager;
import net.sharkdevelopment.gkits.managers.profile.listeners.ProfileListeners;
import net.sharkdevelopment.lib.command.CommandManager;
import net.sharkdevelopment.lib.config.FileConfig;
import net.sharkdevelopment.lib.manager.Manager;
import net.sharkdevelopment.lib.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections8.Reflections;

import java.util.List;
import java.util.stream.Stream;

@Getter
public class SharkGKits extends JavaPlugin {

    @Getter
    private static SharkGKits instance;

    private List<Manager> managerList;

    private MongoManager mongoManager;

    private FileConfig settingsConfig,
            menusConfig,
            messagesConfig;

    @Override
    public void onLoad() {
        managerList = ImmutableList.of(
                CommandManager.INSTANCE);
    }

    @Override
    public void onEnable() {
        instance = this;

        this.registerConfigurations();

        this.mongoManager = new MongoManager(this);

        Gkit.initKits();

        managerList.forEach(manager -> manager.onEnable(this));

        this.registerListeners();
        this.registerCustomEnchants();
    }

    private void registerCustomEnchants() {
        Reflections reflections = new Reflections();
        reflections.getSubTypesOf(CustomEnchant.class)
                .forEach(classz -> {
                    try {
                        CustomEnchant customEnchant = classz.newInstance();
                        customEnchant.enable();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void onDisable() {
        managerList.forEach(manager -> manager.onDisable(this));
        Gkit.getGkits().forEach((name, kit) -> {
            kit.save();
        });
    }

    private void registerConfigurations() {
        this.settingsConfig = new FileConfig(this, "settings.yml");
        this.menusConfig = new FileConfig(this, "menus.yml");
        this.messagesConfig = new FileConfig(this, "messages.yml");
    }

    private void registerListeners() {
        Stream.of(
                new MenuListener(),
                new ProfileListeners(),
                new PlayerListener(),
                new ArmorListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
