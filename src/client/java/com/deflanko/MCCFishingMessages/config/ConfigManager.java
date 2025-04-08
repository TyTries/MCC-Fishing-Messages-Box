package com.deflanko.MCCFishingMessages.config;

import com.deflanko.MCCFishingMessages.MCCFishingMessagesMod;
import com.google.gson.FieldNamingPolicy;
import net.fabricmc.loader.api.FabricLoader;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigManager {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("mccfishingmessages.json");
    public static final Identifier CONFIG_ID = Identifier.of(MCCFishingMessagesMod.MODID, "mccfishingmessages");

    private static ConfigClassHandler<Config> config;

    public static ConfigClassHandler<Config> handler() {
        if(config == null) {
            throw new IllegalStateException("Config accessed before it was initialized");
        }
        return config;
    }

    public static Config instance() {
        if(config == null) {
            throw new IllegalStateException("Config accessed before it was initialized");
        }
        return config.instance();
    }

    public static boolean isInitialized() {
        return config != null;
    }

    public static void init() {
        if (isInitialized()) return;

        config = ConfigClassHandler.createBuilder(Config.class)
                .id(CONFIG_ID)
                .serializer(config -> GsonConfigSerializerBuilder.create(config)
                        .appendGsonBuilder(b -> b
                                .serializeNulls()
                                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                .setPrettyPrinting()
                                .registerTypeAdapter(Config.class, Config.INSTANCE_CREATOR))
                        .setPath(CONFIG_PATH)
                        .setJson5(false)
                        .build())
                .build();
    }

    public static void loadWithFailureBackup() {
        assert config != null;

        try {
            config.load();
            return;
        } catch (Exception e) {
            MCCFishingMessagesMod.LOGGER.error("Failed to load config", e);
        }

        File file = CONFIG_PATH.toFile();
        if (file.exists() && file.isFile()) {
            String backupName = FilenameUtils.getBaseName(file.getName()) +
                    "-backup-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) +
                    "." + FilenameUtils.getExtension(file.getName());
            Path backup = Path.of(CONFIG_PATH.toAbsolutePath().getParent().toString(), backupName);
            try {
                Files.copy(file.toPath(), backup, StandardCopyOption.REPLACE_EXISTING);
                MCCFishingMessagesMod.LOGGER.info("Created config backup at: {}", backup);
            } catch (Exception backupException) {
                MCCFishingMessagesMod.LOGGER.error("Failed to create config backup: ", backupException);
            }
        } else if (file.exists()) {
            if(file.delete())
                MCCFishingMessagesMod.LOGGER.info("Deleted old config");
        }

        try {
            config.save();
            MCCFishingMessagesMod.LOGGER.info("Created new config");
            config.load();
        } catch (Exception loadException) {
            MCCFishingMessagesMod.LOGGER.error("Failed to load config again, please report this issue: ", loadException);
        }
    }
}