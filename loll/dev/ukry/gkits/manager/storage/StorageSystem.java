package dev.ukry.gkits.manager.storage;

import dev.ukry.gkits.profile.Profile;
import dev.ukry.gkits.serializable.Gkit;

public interface StorageSystem {

    void save();

    void reload();

    Profile loadProfile(String uuid);

    void saveProfile(Profile profile);

    void removeProfile(Profile profile);

    void updateProfile(String key, Profile value);

    Gkit loadGkit(String name);

    void saveGkit(Gkit gkit);

    void removeGkit(Gkit gkit);

    void updateGkit(String key, Gkit value);
}