package net.smootheez.elytracontrol.config;

import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import net.fabricmc.loader.api.FabricLoader;
import net.smootheez.elytracontrol.Constants;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Map;

public class ElytraControlConfig {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final ElytraControlConfig INSTANCE = new ElytraControlConfig(FabricLoader.getInstance().getConfigDir().resolve("elytracontrol.json").toFile());
    static {
        INSTANCE.load();
    }

    protected final File file;
    protected final Object2ObjectLinkedOpenHashMap<String, Option<?>> optionMap = new Object2ObjectLinkedOpenHashMap<>();
    protected final Map<String, Option<?>> optionMapView = Collections.unmodifiableMap(optionMap);

    public final Option.BooleanOption elytraToggleConfig = addOption(new Option.BooleanOption("elytra_toggle", true));

    public ElytraControlConfig (File file){
        this.file = file;
    }

    public void load(){
        if (file.exists()){
            try (FileReader reader = new FileReader(file)){
                fromJson(JsonParser.parseReader(reader));
            } catch (Exception e){
                Constants.LOGGER.error("Failed to load config", e);
            }
        }
    }

    public void save(){
        try (FileWriter writer = new FileWriter(file)){
            GSON.toJson(toJson(), writer);
        } catch (Exception e){
            Constants.LOGGER.error("Failed to save config", e);
        }
    }

    protected void fromJson(JsonElement json) throws JsonParseException{
        if (json.isJsonObject()){
            JsonObject object = json.getAsJsonObject();
            ObjectBidirectionalIterator<Object2ObjectMap.Entry<String, Option<?>>> iterator = optionMap.object2ObjectEntrySet().iterator();
            while (iterator.hasNext()){
                Object2ObjectMap.Entry<String, Option<?>> entry = iterator.next();
                JsonElement element = object.get(entry.getKey());
                if (element != null){
                    try {
                        entry.getValue().fromJson(element);
                    } catch (JsonParseException e){
                        Constants.LOGGER.info("Failed to parse option " + entry.getKey());
                    }
                }
            }
        } else {
            throw new JsonParseException("Json must be an object");
        }
    }

    protected JsonElement toJson() {
        JsonObject object = new JsonObject();
        ObjectBidirectionalIterator<Object2ObjectMap.Entry<String, Option<?>>> iterator = optionMap.object2ObjectEntrySet().fastIterator();
        while (iterator.hasNext()) {
            Object2ObjectMap.Entry<String, Option<?>> entry = iterator.next();
            object.add(entry.getKey(), entry.getValue().toJson());
        }
        return object;
    }

    protected <T extends Option<?>> T addOption(T option) {
        Option<?> old = optionMap.put(option.getKey(), option);
        if (old != null) {
            Constants.LOGGER.info("Duplicate option " + option.getKey());
        }
        return option;
    }

    @Nullable
    public Option<?> getOption(String key) {
        return optionMap.get(key);
    }

    public Map<String, Option<?>> getOptionMapView() {
        return optionMapView;
    }
}
