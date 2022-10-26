package dev.ukry.gkits.handler;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.utils.files.ConfigFile;
import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
public class GkitsHandler {

    private final HashMap<String, Gkit> gkits = new HashMap<>();
    private final ConfigFile gkitsConfig = SharkGKits.getInstance().getGkitsConfig();

    public void init() {
        loadAll();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(SharkGKits.getInstance().getGkitsHandler()::saveAll, 5L, 600, TimeUnit.SECONDS);//60*10 = 600 | 10m
    }

    public Gkit getGkit(String name) {
        return gkits.getOrDefault(name, null);
    }

    public void updateGkit(String name, Gkit newGkit) {
        gkits.replace(name, newGkit);
        SharkGKits.getInstance().getStorageSystem().updateGkit(name, newGkit);
    }

    public void removeGkit(String name) {
        Gkit gkit = gkits.get(name);
        SharkGKits.getInstance().getStorageSystem().removeGkit(gkit);
        gkits.remove(name);
    }

    public void stop() {
        saveAll();
    }

    public void loadAll() {
        for(String s : gkitsConfig.getConfiguration().getConfigurationSection("GKITS").getKeys(false)) {
            gkits.put(s, SharkGKits.getInstance().getStorageSystem().loadGkit(s));
        }
    }

    public void saveAll() {
        gkits.values().forEach(SharkGKits.getInstance().getStorageSystem()::saveGkit);
        gkitsConfig.save();
    }
}