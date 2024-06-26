package net.smootheez.elytracontrol.config;

import net.smootheez.elytracontrol.Constants;

import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ElytraControlConfig {

    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final ElytraControlConfig INSTANCE = new ElytraControlConfig(FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID + ".json").toFile());
    static {
        INSTANCE.loadConfig();
    }

    protected final File file;
    protected final Object2ObjectLinkedOpenHashMap<String, Option<?>> optionMap = new Object2ObjectLinkedOpenHashMap<>();

    public final Option.BooleanOption elytraLock = addOption(new Option.BooleanOption("elytra_lock", true));
    public final Option.BooleanOption elytraCancel = addOption(new Option.BooleanOption("elytra_cancel", true));

    public ElytraControlConfig(File file) {
        this.file = file;
    }

    public void loadConfig(){
        if (file.exists()){
            try (FileReader reader = new FileReader(file)){
                fromJson(JsonParser.parseReader(reader));
            } catch (Exception e){
                Constants.LOGGER.error("Could not load config from file.", e);
            }
        }
        saveConfig();
    }

    public void saveConfig(){
        try (FileWriter writer = new FileWriter(file)){
            GSON.toJson(toJson(), writer);
        } catch (Exception e){
            Constants.LOGGER.error("Could not save config to file.", e);
        }
    }

    protected void fromJson(JsonElement json) throws JsonParseException{
        if (json.isJsonObject()){
            JsonObject object = json.getAsJsonObject();
            ObjectBidirectionalIterator<Object2ObjectMap.Entry<String, Option<?>>> iterator = optionMap.object2ObjectEntrySet().fastIterator();
            while (iterator.hasNext()){
                Object2ObjectMap.Entry<String, Option<?>> entry = iterator.next();
                JsonElement element = object.get(entry.getKey());
                if (element != null){
                    try {
                        entry.getValue().fromJson(element);
                    } catch (JsonParseException e){
                        Constants.LOGGER.error("Could not parse json for option {}", entry.getKey(), e);
                    }
                }
            }
        } else {
            throw new JsonParseException("Json must be object");
        }
    }

    protected JsonElement toJson(){
        JsonObject object = new JsonObject();
        ObjectBidirectionalIterator<Object2ObjectMap.Entry<String, Option<?>>> iterator = optionMap.object2ObjectEntrySet().fastIterator();
        while (iterator.hasNext()){
            Object2ObjectMap.Entry<String, Option<?>> entry = iterator.next();
            object.add(entry.getKey(), entry.getValue().toJson());
        }
        return object;
    }

    protected <T extends Option<?>> T addOption(T option){
        Option<?> old = optionMap.put(option.getKey(), option);
        if (old != null){
            Constants.LOGGER.warn("Option with key {} was overridden", old.getKey());
        }
        return option;
    }
}