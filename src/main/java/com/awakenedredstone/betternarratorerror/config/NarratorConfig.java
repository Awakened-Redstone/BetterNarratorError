package com.awakenedredstone.betternarratorerror.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class NarratorConfig {
    private static final File FILE = FabricLoader.getInstance().getConfigDir().resolve("betternarratorerror.json").toFile();
    private static final Gson GSON = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .setPrettyPrinting()
      .create();

    public static final NarratorConfig INSTANCE = load();

    public boolean hidePath = true;
    public boolean hideStacktraces = true;
    public boolean hideSuppressed = true;
    public boolean disableNarrator = false;

    private static NarratorConfig load() {
        return FILE.exists() ? read() : create();
    }

    private static NarratorConfig create() {
        try (var writer = new FileWriter(FILE)) {
            FILE.getParentFile().mkdirs();
            var config = new NarratorConfig();
            GSON.toJson(config, writer);
            return config;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create config", e);
        }
    }

    private static NarratorConfig read() {
        try (var reader = new FileReader(FILE)) {
            return GSON.fromJson(reader, NarratorConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config", e);
        }
    }
}
