package dev.ukry.gkits.profile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.locale.Locale;
import dev.ukry.gkits.serializable.Gkit;
import dev.ukry.gkits.utils.json.JsonBuilder;
import lombok.Data;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Profile implements ConfigurationSerializable {

    private final UUID uuid;
    private double coins;
    private Gkit[] gKits = new Gkit[]{};

    public void addCoins(double input, String reason) {
        Locale.COINS_ADD_MESSAGE
                .setPlayer(Bukkit.getPlayer(uuid))
                .filter(Objects::nonNull)
                .addReplace(s -> s.replace("{AMOUNT}", String.valueOf(input)))
                .addReplace(s -> s.replace("{REASON}", reason))
                .send();
        coins = coins + input;
        SharkGKits.getInstance().getProfileHandler().getProfiles().replace(uuid, this);
    }

    public void addGkit(Gkit gkit) {
        gKits = (Gkit[]) ArrayUtils.add(gKits, gkit);
        SharkGKits.getInstance().getProfileHandler().getProfiles().replace(uuid, this);
    }

    public boolean hasGkit(Gkit gkit) {
        return Arrays.asList(gKits).contains(gkit) || Bukkit.getPlayer(uuid).hasPermission(gkit.getPermission());
    }

    public void removeCoins(double input, String reason) {
        if(coins > input) {
            coins = coins - input;
            Locale.COINS_LOST_MESSAGE
                    .setPlayer(Bukkit.getPlayer(uuid))
                    .filter(Objects::nonNull)
                    .addReplace(s -> s.replace("{AMOUNT}", String.valueOf(input)))
                    .addReplace(s -> s.replace("{REASON}", reason))
                    .send();
            SharkGKits.getInstance().getProfileHandler().getProfiles().replace(uuid, this);
        } else {
            Locale.COINS_NO_CANT_LOST_MESSAGE
                    .setPlayer(Bukkit.getPlayer(uuid))
                    .filter(Objects::nonNull)
                    .addReplace(s -> s.replace("{AMOUNT}", String.valueOf(input)))
                    .addReplace(s -> s.replace("{REASON}", reason))
                    .send();
        }
    }

    public Profile setCoins(double coins) {
        this.coins = coins;
        return this;
    }

    public Profile setGkits(Gkit[] gkits) {
        this.gKits = gkits;
        return this;
    }

    @Override
    public String toString() {
        return new JsonBuilder()
                .addProperty("uuid", uuid.toString())
                .addProperty("coins", coins)
                .addProperty("gkits", Arrays.stream(gKits).map(SharkGKits.GSON::toJson).collect(Collectors.joining(",")))
                .toString();
    }

    public static Profile valueOf(String input) {
        JsonObject object = new JsonParser().parse(input).getAsJsonObject();
        return new Profile(UUID.fromString(object.get("uuid").getAsString()))
                .setCoins(object.get("coins").getAsDouble())
                .setGkits(Arrays.stream(object.get("gkits").getAsString().split(",")).map(Gkit::valueOf).toArray(Gkit[]::new));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("coins", coins);
        map.put("gkits", Arrays.stream(gKits).map(Gkit::serialize).collect(Collectors.toList()));
        return map;
    }

    public static Profile deserialize(Map<String, Object> input) {
        return new Profile(UUID.fromString((String)input.get("uuid")))
                .setCoins(
                        (double)input.get("coins")
                )
                .setGkits(
                        ((List<Map<String, Object>>)input.get("gkits")).stream().map(Gkit::deserialize).toArray(Gkit[]::new)
                );
    }
}
