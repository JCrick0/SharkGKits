package net.sharkdevelopment.gkits.managers.profile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.Setter;
import net.sharkdevelopment.gkits.SharkGKits;
import net.sharkdevelopment.gkits.managers.gkit.Gkit;
import net.sharkdevelopment.lib.Cooldown;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

@Getter
public class Profile {

    @Getter
    private static final Map<UUID, Profile> profiles = Maps.newHashMap();

    private final UUID uuid;

    private final List<Gkit> kits = Lists.newArrayList();

    private final Map<Boolean, Gkit> editingLore = Maps.newHashMap();
    private final Map<Boolean, Gkit> editingContents = Maps.newHashMap();
    private final Map<Boolean, Gkit> editingPermission = Maps.newHashMap();

    private Map<Gkit, Long> kitCooldown = Maps.newHashMap();

    @Setter
    private int coins;

    public Profile(UUID uuid) {
        this.uuid = uuid;

        profiles.put(uuid, this);
        this.load();
    }

    @SuppressWarnings("all")
    public void load() {
        MongoCollection<Document> collection = SharkGKits.getInstance().getMongoManager().getProfiles();
        Document document = collection.find(eq("_id", this.uuid.toString())).first();

        if (document == null) return;

        this.coins = document.getInteger("coins");
        if (document.containsKey("kits")) {
            for (String string : (List<String>) document.get("kits")) {
                this.kits.add(Gkit.getByName(string));
            }
        }

        if (document.containsKey("cooldown")) {
            Document kits = (Document) document.get("cooldown");
            for (Map.Entry entry : kits.entrySet()) {
                Map<Gkit, Long> cooldowns = Maps.newHashMap();

                cooldowns.put(Gkit.getByName((String) entry.getKey()), (Long) entry.getValue());

                this.kitCooldown = cooldowns;
            }
        }
    }

    public void save() {
        Document document = new Document("_id", this.uuid.toString());

        document.put("coins", this.coins);

        if (!kits.isEmpty()) {
            List<String> kits2 = Lists.newArrayList();
            kits.forEach(gkit -> kits2.add(gkit.getName()));
            document.put("kits", kits2);
        }

        Document kits = new Document();

        if (!kitCooldown.isEmpty()) {
            for (Map.Entry<Gkit, Long> entry : this.kitCooldown.entrySet()) {
                kits.put(entry.getKey().getName(), entry.getValue());
            }

            document.put("cooldown", kits);
        }


        Bson filter = eq("_id", this.uuid.toString());
        FindIterable<Document> iterable = SharkGKits.getInstance().getMongoManager().getProfiles().find(filter);

        if (iterable.first() == null) {
            SharkGKits.getInstance().getMongoManager().getProfiles().insertOne(document);
        } else {
            SharkGKits.getInstance().getMongoManager().getProfiles().replaceOne(filter, document);
        }
    }

    public static Profile getProfile(Player player) {
        for (Map.Entry<UUID, Profile> entry : profiles.entrySet()) {
            if (player.getUniqueId().equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Profile getProfile(UUID player) {
        for (Map.Entry<UUID, Profile> entry : profiles.entrySet()) {
            if (player.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
