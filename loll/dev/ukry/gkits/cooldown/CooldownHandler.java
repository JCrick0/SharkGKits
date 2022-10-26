package dev.ukry.gkits.cooldown;

import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.utils.files.ConfigFile;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownHandler {

    public static Map<UUID, Map<String, Long>> map;
    public static boolean inited = false;

    public CooldownHandler() {
        inited = true;
        map = new HashMap<>();
        ConfigFile dataConfig = SharkGKits.getInstance().getDataConfig();
        ConfigurationSection section = dataConfig.getConfiguration().getConfigurationSection("cooldowns");
        if(section == null) return;
        for(String s : section.getKeys(false)) {
            UUID uuid = UUID.fromString(s);
            for(String ss : dataConfig.getConfiguration().getConfigurationSection("cooldowns." + uuid).getKeys(false)) {
                String path = "cooldowns." + uuid + "." + ss;
                Map<String, Long> mp;
                if(map.containsKey(uuid)) {
                    mp = map.get(uuid);
                    mp.put(ss, Long.valueOf(dataConfig.getString(path + ".time")));
                    map.replace(uuid, mp);
                } else {
                    mp = new HashMap<>();
                    mp.put(ss, Long.valueOf(dataConfig.getString(path + ".time")));
                    map.put(uuid, mp);
                }
            }
        }
    }

    public static void stop() {
        ConfigFile dataConfig = SharkGKits.getInstance().getDataConfig();

        for(UUID uuid : map.keySet()) {
            Map<String, Long> mp = map.get(uuid);
            for(String s : mp.keySet()) {
                dataConfig.getConfiguration().set("cooldowns." + uuid.toString() + "." + s + ".time", mp.get(s).toString());
            }
        }
        dataConfig.save();
    }

    public static boolean hasCooldown(UUID uuid, String gkit) {
        if(!map.containsKey(uuid)) return false;
        if(!map.get(uuid).containsKey(gkit)) return false;
        long cooldown = map.get(uuid).get(gkit);
        if(cooldown >= System.currentTimeMillis()) {
            return true;
        } else {
            map.get(uuid).remove(gkit);
            return false;
        }
    }

    public static void setCooldown(UUID uuid, String gkit, Integer seconds) {
        Map<String, Long> mp;
        if(map.containsKey(uuid)) {
            mp = map.get(uuid);
        } else {
            mp = new HashMap<>();
        }
        mp.put(gkit, System.currentTimeMillis() + seconds * 1000L);
        map.put(uuid, mp);
    }

    public static String getCooldown(UUID uuid, String gkit) {
        long reaming = (map.get(uuid).get(gkit) - System.currentTimeMillis()) / 1000L;
        if(reaming > 60) {
            return reaming / 60 + " Minutes";
        } else {
            return reaming + " Seconds";
        }
    }
}