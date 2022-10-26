package dev.ukry.gkits.manager.storage.h2;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.profile.Profile;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.manager.storage.StorageSystem;
import dev.ukry.gkits.utils.files.ConfigFile;

public class H2System implements StorageSystem {

    private final ConfigFile gkitsConfig = SharkGKits.getInstance().getGkitsConfig();
    private final ConfigFile profilesConfig = SharkGKits.getInstance().getProfilesConfig();

    @Override
    public void save() {
        gkitsConfig.save();
        profilesConfig.save();
    }

    @Override
    public void reload() {
        profilesConfig.reload();
    }

    @Override
    public Profile loadProfile(String uuid) {
        return (Profile) profilesConfig.getConfiguration().get("PROFILES." + uuid);
    }

    @Override
    public void saveProfile(Profile profile) {
        profilesConfig.getConfiguration().set("PROFILES." + profile.getUuid().toString(), profile);
        profilesConfig.save();
        profilesConfig.reload();
    }

    @Override
    public void removeProfile(Profile profile) {
        profilesConfig.getConfiguration().set("PROFILES." + profile.getUuid().toString(), null);
        profilesConfig.save();
        profilesConfig.reload();
    }

    @Override
    public void updateProfile(String key, Profile value) {
        profilesConfig.getConfiguration().set("PROFILES." + key, value);
        profilesConfig.save();
        profilesConfig.reload();
    }

    @Override
    public Gkit loadGkit(String name) {
        return (Gkit) gkitsConfig.getConfiguration().get("GKITS." + name);
    }

    @Override
    public void saveGkit(Gkit gkit) {
        gkitsConfig.getConfiguration().set("GKITS." + gkit.getName(), gkit);
        gkitsConfig.save();
        gkitsConfig.reload();
    }

    @Override
    public void removeGkit(Gkit gkit) {
        gkitsConfig.getConfiguration().set("GKITS." + gkit.getName(), null);
        gkitsConfig.save();
        gkitsConfig.reload();
    }

    @Override
    public void updateGkit(String key, Gkit value) {
        gkitsConfig.getConfiguration().set("GKITS." + key, value);
        gkitsConfig.save();
        gkitsConfig.reload();
    }
}