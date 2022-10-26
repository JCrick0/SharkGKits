package net.sharkdevelopment.gkits.managers.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.sharkdevelopment.gkits.SharkGKits;
import org.bson.Document;

import java.util.Collections;

@Getter
public class MongoManager {

    private final SharkGKits gkits;

    private final MongoClient mongoClient;

    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> profiles;
    private final MongoCollection<Document> kits;

    @SuppressWarnings("all")
    public MongoManager(SharkGKits gkits) {
        this.gkits = gkits;

        if (gkits.getSettingsConfig().getConfig().getBoolean("MONGO.URI.ENABLED")) {
            this.mongoClient = new MongoClient(new MongoClientURI(gkits.getSettingsConfig().getConfig().getString("MONGO.URI.URL")));
            this.mongoDatabase = mongoClient.getDatabase(gkits.getSettingsConfig().getConfig().getString("MONGO.URI.DATABASE"));
        } else {
            String hostname = gkits.getSettingsConfig().getConfig().getString("MONGO.HOST");
            int port = gkits.getSettingsConfig().getConfig().getInt("MONGO.PORT");
            if (gkits.getSettingsConfig().getConfig().getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
                this.mongoClient = new MongoClient(new ServerAddress(hostname, port), Collections.singletonList(
                        MongoCredential.createCredential(
                                gkits.getSettingsConfig().getConfig().getString("MONGO.AUTHENTICATION.USERNAME"),
                                gkits.getSettingsConfig().getConfig().getString("MONGO.AUTHENTICATION.AUTHENTICATION-DATABASE"),
                                gkits.getSettingsConfig().getConfig().getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray())));
            } else {
                this.mongoClient = new MongoClient(new ServerAddress(hostname, port));
            }
            this.mongoDatabase = mongoClient.getDatabase(gkits.getSettingsConfig().getConfig().getString("MONGO.DATABASE"));
        }

        this.profiles = mongoDatabase.getCollection("profiles");
        this.kits = mongoDatabase.getCollection("kits");
    }
}