package dev.ukry.gkits.serializable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.ukry.gkits.SharkGKits;
import dev.ukry.gkits.utils.json.JsonBuilder;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@Data
public class Gkit implements ConfigurationSerializable {

    private final String name;
    private boolean enabled;
    private String[] description;
    private String permission;
    private double price;
    private int cooldown;
    private String category;
    private ItemStack icon;
    private int slot;
    private ItemStack[] contents;
    private ItemStack[] armorContents;
    private ItemStack[] displayContents;

    public Gkit setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Gkit setDescription(String[] description) {
        this.description = description;
        return this;
    }

    public Gkit setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public Gkit setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Gkit setCooldown(Integer cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public Gkit setCategory(String category) {
        this.category = category;
        return this;
    }

    public Gkit setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public Gkit setSlot(Integer slot) {
        this.slot = slot;
        return this;
    }

    public Gkit setContents(ItemStack[] contents) {
        this.contents = contents;
        return this;
    }

    public Gkit setArmorContents(ItemStack[] armorContents) {
        this.armorContents = armorContents;
        return this;
    }

    public Gkit setDisplayContents(ItemStack[] displayContents) {
        this.displayContents = displayContents;
        return this;
    }

    public Gkit save() {
        SharkGKits.getInstance().getGkitsHandler().getGkits().replace(name, this);
        return this;
    }

    @Override
    public String toString() {
        return new JsonBuilder()
                .addProperty("name", name)
                .addProperty("enabled", enabled)
                .addProperty("description", String.join(",", description))
                .addProperty("permission", permission)
                .addProperty("price", price)
                .addProperty("cooldown", cooldown)
                .addProperty("category", category)
                .addProperty("icon", SharkGKits.GSON.toJson(icon))
                .addProperty("slot", slot)
                .addProperty("contents", Arrays.stream(contents).map(SharkGKits.GSON::toJson).collect(Collectors.joining(",")))
                .addProperty("armorContents", Arrays.stream(contents).map(SharkGKits.GSON::toJson).collect(Collectors.joining(",")))
                .addProperty("displayContents", Arrays.stream(displayContents).map(SharkGKits.GSON::toJson).collect(Collectors.joining(",")))
                .toString();
    }

    public static Gkit valueOf(String input) {
        JsonObject object = new JsonParser().parse(input).getAsJsonObject();
        return new Gkit(object.get("name").getAsString())
                .setEnabled(object.get("enabled").getAsBoolean())
                .setDescription(Arrays.stream(object.get("description")
                                .getAsString()
                                .split(";;"))
                        .toArray(String[]::new))
                .setPermission(object.get("permission").getAsString())
                .setPrice(object.get("price").getAsDouble())
                .setCooldown(object.get("cooldown").getAsInt())
                .setCategory(object.get("category").getAsString())
                .setIcon(SharkGKits.GSON.fromJson(object.get("icon").getAsString(), ItemStack.class))
                .setSlot(object.get("slot").getAsInt())
                .setContents(Arrays.stream(object.get("contents")
                                .getAsString()
                                .split(";;"))
                        .map(s -> SharkGKits.GSON.fromJson(s, ItemStack.class))
                        .toArray(ItemStack[]::new))
                .setArmorContents(Arrays.stream(object.get("armorContents")
                                .getAsString()
                                .split(";;"))
                        .map(s -> SharkGKits.GSON.fromJson(s, ItemStack.class))
                        .toArray(ItemStack[]::new))
                .setDisplayContents(Arrays.stream(object.get("displayContents")
                                .getAsString()
                                .split(";;"))
                        .map(s -> SharkGKits.GSON.fromJson(s, ItemStack.class))
                        .toArray(ItemStack[]::new));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("enabled", enabled);
        map.put("description", String.join(",", description));
        map.put("permission", permission);
        map.put("price", price);
        map.put("cooldown", cooldown);
        map.put("category", category);
        map.put("icon", SharkGKits.GSON.toJson(icon));
        map.put("slot", slot);
        map.put("contents", Arrays.stream(contents).map(SharkGKits.GSON::toJson).collect(Collectors.joining(";;")));
        map.put("armorContents", Arrays.stream(armorContents).map(SharkGKits.GSON::toJson).collect(Collectors.joining(";;")));
        map.put("displayContents", Arrays.stream(displayContents).map(SharkGKits.GSON::toJson).collect(Collectors.joining(";;")));
        return map;
    }

    public static Gkit deserialize(Map<String, Object> input) {
        String[] cJsons = ((String)input.get("contents")).split(";;");
        ItemStack[] c = new ItemStack[cJsons.length];
        for(int i = 0; i < cJsons.length; i++) {
            String s = cJsons[i];
            if(s.contains("{") || s.contains("}")) {
                c[i] = SharkGKits.GSON.fromJson(s, ItemStack.class);
            } else {
                c[i] = null;
            }
        }
        String[] acJsons = ((String)input.get("armorContents")).split(";;");
        ItemStack[] ac = new ItemStack[acJsons.length];
        for(int i = 0; i < acJsons.length; i++) {
            String s = acJsons[i];
            if(s.contains("{") || s.contains("}")) {
                ac[i] = SharkGKits.GSON.fromJson(s, ItemStack.class);
            } else {
                ac[i] = null;
            }
        }
        String[] dJsons = ((String)input.get("displayContents")).split(";;");
        ItemStack[] dc = new ItemStack[dJsons.length];
        for(int i = 0; i < dJsons.length; i++) {
            String s = dJsons[i];
            if(s.contains("{") || s.contains("}")) {
                dc[i] = SharkGKits.GSON.fromJson(s, ItemStack.class);
            } else {
                dc[i] = null;
            }
        }
        return new Gkit(
                (String) input.get("name"))
                .setEnabled((Boolean) input.get("enabled"))
                .setDescription(((String) input.get("description")).split(","))
                .setPermission((String) input.get("permission"))
                .setPrice((Double) input.get("price"))
                .setCooldown((Integer) input.get("cooldown"))
                .setCategory((String) input.get("category"))
                .setIcon(SharkGKits.GSON.fromJson((String) input.get("icon"), ItemStack.class))
                .setSlot((Integer) input.get("slot"))
                .setContents(c)
                .setArmorContents(ac)
                .setDisplayContents(dc);
    }
}