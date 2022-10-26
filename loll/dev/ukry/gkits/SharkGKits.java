package dev.ukry.gkits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.ukry.gkits.command.CoinsCommand;
import dev.ukry.gkits.command.EnchantsCommand;
import dev.ukry.gkits.command.gkit.GKitsCommand;
import dev.ukry.gkits.command.gkit.GkitCommand;
import dev.ukry.gkits.cooldown.CooldownHandler;
import dev.ukry.gkits.handler.GkitsHandler;
import dev.ukry.gkits.manager.listener.CategoriesEditListener;
import dev.ukry.gkits.manager.listener.EnchantsListener;
import dev.ukry.gkits.manager.listener.GkitEditListener;
import dev.ukry.gkits.manager.listener.profile.CoinsListener;
import dev.ukry.gkits.manager.listener.profile.ProfileListener;
import dev.ukry.gkits.profile.Profile;
import dev.ukry.gkits.profile.ProfileHandler;
import dev.ukry.gkits.serializable.Category;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.manager.storage.StorageSystem;
import dev.ukry.gkits.manager.storage.h2.H2System;
import dev.ukry.gkits.utils.CC;
import dev.ukry.gkits.utils.SharkLicenses;
import dev.ukry.gkits.utils.command.Command;
import dev.ukry.gkits.utils.files.ConfigFile;
import dev.ukry.gkits.utils.gson.GsonListener;
import dev.ukry.gkits.utils.gson.ItemStackAdapter;
import dev.ukry.gkits.utils.gson.PotionEffectAdapter;
import dev.ukry.gkits.utils.time.TimeCounter;
import fr.mrmicky.fastinv.FastInvManager;
import fr.mrmicky.fastinv.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;

@Getter
public class SharkGKits extends JavaPlugin {

    @Getter private static SharkGKits instance;
    public static Gson GSON;
    private ConfigFile dataConfig, categoriesConfig, gkitsConfig, messagesConfig, profilesConfig;
    private GkitsHandler gkitsHandler;
    private ProfileHandler profileHandler;
    private StorageSystem storageSystem;

    @Override
    public void onEnable() {
        SharkGKits.instance = this;
        GSON = new GsonBuilder().registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter()).setPrettyPrinting().serializeNulls().create();

        this.saveDefaultConfig();
        this.setupStorage();
        this.dataConfig = new ConfigFile(this, "data");
        this.categoriesConfig = new ConfigFile(this, "categories");
        this.gkitsConfig = new ConfigFile(this, "gkits");
        this.messagesConfig = new ConfigFile(this, "messages");
        this.profilesConfig = new ConfigFile(this, "profiles");

        this.listeners();

        //Register inventories API
        FastInvManager.register(this);

        //Init all handlers
        dev.ukry.gkits.utils.item.ItemBuilder.registerGlow();
        (gkitsHandler = new GkitsHandler()).init();
        (profileHandler = new ProfileHandler()).init();
        new CooldownHandler();
        if(Category.valueOf("DEFAULT") == null) {
            new Category("DEFAULT", true, 0, 45, new ItemBuilder(Material.BOOK).name("DEFAULT").build(), false).save();
        }
        this.commands();
    }

    public void commands() {
        Arrays.asList(new GkitCommand(), new GKitsCommand(), new EnchantsCommand(), new CoinsCommand()).forEach(Command::registerCommand);
    }

    private void listeners() {
        Arrays.asList(new GsonListener(),new CoinsListener(), new ProfileListener(), new CategoriesEditListener(), new EnchantsListener(), new GkitEditListener()).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onDisable() {
        if(gkitsHandler != null) gkitsHandler.stop();
        if(profileHandler != null) profileHandler.stop();
        if(CooldownHandler.inited) CooldownHandler.stop();
    }

    private void setupStorage() {
        String storageType = getConfig().getString("system.storage");
        TimeCounter counter = new TimeCounter();
        if(storageType.equalsIgnoreCase("H2")) {
            counter.update();
            ConfigurationSerialization.registerClass(Gkit.class);
            ConfigurationSerialization.registerClass(Profile.class);
            storageSystem = new H2System();
        } else {
            CC.log(CC.RED, "The storage type " + storageType + " not is valid!");
            System.exit(0);
        }
    }
}