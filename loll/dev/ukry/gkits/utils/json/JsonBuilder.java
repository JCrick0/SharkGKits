package dev.ukry.gkits.utils.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonBuilder {

    private final JsonObject json = new JsonObject();

    public JsonBuilder addProperty(String property, String value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Number value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Boolean value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, Character value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonBuilder add(String property, JsonElement element) {
        this.json.add(property, element);
        return this;
    }

    public JsonObject toObject() {
        return this.json;
    }

    @Override
    public String toString() {
        return toObject().toString();
    }
}
