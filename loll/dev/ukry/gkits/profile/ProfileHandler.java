package dev.ukry.gkits.profile;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.utils.files.ConfigFile;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
public class ProfileHandler {

    private final HashMap<UUID, Profile> profiles = new HashMap<>();
    private final ConfigFile profilesConfig = SharkGKits.getInstance().getProfilesConfig();

    public void init() {
        loadAll();
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(SharkGKits.getInstance().getProfileHandler()::saveAll, 5L, 600, TimeUnit.SECONDS);//60*10 = 600 | 10m
    }

    public Profile getProfile(UUID uuid) {
        return profiles.getOrDefault(uuid, null);
    }

    public void addProfile(Profile profile) {
        profiles.put(profile.getUuid(), profile);
    }

    public void stop() {
        saveAll();
    }

    public void loadAll() {
        ConfigurationSection section = profilesConfig.getConfiguration().getConfigurationSection("PROFILES");
        if(section == null) return;;
        for(String s : section.getKeys(false)) {
            Profile profile = SharkGKits.getInstance().getStorageSystem().loadProfile(s);
            profiles.put(profile.getUuid(), profile);
        }
    }

    public void saveAll() {
        profiles.values().forEach(SharkGKits.getInstance().getStorageSystem()::saveProfile);
        profilesConfig.save();
    }
}