package net.smootheez.elytracontrol.config.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.smootheez.elytracontrol.config.Option;

public class BooleanOption extends Option.BaseOption<Boolean> {
    public BooleanOption(String key, boolean defaultValue) {
        super(key, defaultValue);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(get());
    }

    @Override
    public void fromJson(JsonElement json) throws JsonParseException {
        if (json.isJsonPrimitive()){
            set(json.getAsBoolean());
        } else {
            throw new JsonParseException("Json must be primitive");
        }
    }
}
