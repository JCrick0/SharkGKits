package net.sharkdevelopment.gkits.managers.gkit;

import com.google.common.collect.Maps;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.lib.CC;
import net.sharkdevelopment.lib.DateUtils;
import net.sharkdevelopment.lib.InventoryUtil;
import net.sharkdevelopment.lib.config.ConfigCursor;
import net.sharkdevelopment.lib.maker.ItemMaker;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

@Getter
@Setter
public class Gkit {

    @Getter
    private static Map<String, Gkit> gkits = Maps.newHashMap();

    private final String name;
    private String displayName;
    private String permission;
    private boolean enabled;
    private long cooldown;
    private int slot;
    private int price;
    private List<String> description;
    private ItemStack icon;
    private ItemStack[] contents;
    private ItemStack[] armorContents;

    public Gkit(String name) {
        this.name = name;

        this.contents = new ItemStack[36];
        this.armorContents = new ItemStack[4];

        gkits.put(name, this);
    }

    public Gkit(
            String name,
            String displayName,
            String permission,
            boolean enabled,
            long cooldown,
            int slot,
            int price,
            List<String> description,
            ItemStack icon,
            ItemStack[] contents,
            ItemStack[] armorContents
    ) {
        this(name);
        this.displayName = displayName;
        this.permission = permission;
        this.enabled = enabled;
        this.cooldown = cooldown;
        this.slot = slot;
        this.price = price;
        this.description = description;
        this.icon = icon;
        this.contents = contents;
        this.armorContents = armorContents;
    }

    public static void initKits() {
        MongoCollection<Document> collection = SharkGKits.getInstance().getMongoManager().getKits();
        collection.find().forEach((Consumer<? super Document>) document -> {
            String name = document.getString("name");
            String displayName = document.getString("displayName");
            String permission = document.getString("permission");
            boolean enabled = document.getBoolean("enabled");
            long cooldown = document.getLong("cooldown");
            int slot = document.getInteger("slot");
            int price = document.getInteger("price");
            List<String> description = document.getList("description", String.class);
            ItemStack icon = InventoryUtil.deserializeItemStack(document.getString("icon"));
            ItemStack[] contents = InventoryUtil.deserializeInventory(document.getString("contents"));
            ItemStack[] armorContents = InventoryUtil.deserializeInventory(document.getString("armorContents"));

            new Gkit(
                    name,
                    displayName,
                    permission,
                    enabled,
                    cooldown,
                    slot,
                    price,
                    description,
                    icon,
                    contents,
                    armorContents
            );
        });
    }

    public static void setDefaultParams(Gkit gkit) {
        ConfigCursor cursor = new ConfigCursor(SharkGKits.getInstance().getSettingsConfig(), "SETTINGS.GKIT-DEFAULT");

        List<Integer> slots = gkits.values().stream().map(Gkit::getSlot).collect(Collectors.toList());

        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i) != null) {
                gkit.setSlot(i);
            }
        }

        gkit.setDisplayName(CC.translate(cursor.getString("DISPLAY-NAME").replace("<gkit-name>", gkit.getName())));
        long duration = DateUtils.formatLong(cursor.getString("COOLDOWN"));
        gkit.setCooldown(duration);
        List<String> description = cursor.getStringList("DESCRIPTION");
        description.replaceAll(desc -> desc.replace("<gkit>", gkit.getName()).replace("<cooldown>", DateUtils.formatDuration(gkit.getCooldown())));
        gkit.setDescription(description);
        gkit.setIcon(ItemMaker.of(Material.valueOf(cursor.getString("ICON"))).displayName(gkit.getDisplayName()).lore(gkit.getDescription()).build());
        gkit.setPermission(cursor.getString("PERMISSION"));
        gkit.setPrice(cursor.getInt("PRICE"));
        gkit.setEnabled(true);
    }

    public void save() {
        Document document = new Document("name", this.name);

        document.put("displayName", this.displayName);
        document.put("permission", this.permission);
        document.put("enabled", this.enabled);
        document.put("cooldown", this.cooldown);
        document.put("slot", this.slot);
        document.put("price", this.price);
        document.put("description", this.description);
        document.put("icon", InventoryUtil.serializeItemStack(this.icon));
        document.put("contents", InventoryUtil.serializeInventory(this.contents));
        document.put("armorContents", InventoryUtil.serializeInventory(this.armorContents));

        Bson filter = eq("name", this.name);
        FindIterable<Document> iterable = SharkGKits.getInstance().getMongoManager().getKits().find(filter);

        if (iterable.first() == null) {
            SharkGKits.getInstance().getMongoManager().getKits().insertOne(document);
        } else {
            SharkGKits.getInstance().getMongoManager().getKits().replaceOne(filter, document);
        }
    }

    public void delete() {
        Bson filter = eq("name", this.name);
        FindIterable<Document> iterable = SharkGKits.getInstance().getMongoManager().getKits().find(filter);

        gkits.remove(name);

        if (iterable.first() != null) {
            SharkGKits.getInstance().getMongoManager().getKits().deleteOne(filter);
        }
    }

    public static Gkit getByName(String name) {
        for (Map.Entry<String, Gkit> stringGkitEntry : gkits.entrySet()) {
            if (stringGkitEntry.getKey().equalsIgnoreCase(name)) {
                return stringGkitEntry.getValue();
            }
        }
        return null;
    }
}
