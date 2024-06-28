package net.smootheez.elytracontrol.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public interface Option<T> {
    String getKey();

    T get();

    void set(T value);

    JsonElement toJson();

    void fromJson(JsonElement json) throws JsonParseException;

    abstract class BaseOption<T> implements Option<T>{
        protected final String key;
        protected T value;

        public BaseOption(String key, T defaultValue) {
            this.key = key;
            this.value = defaultValue;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public void set(T value) {
            this.value = value;
        }
    }
}
