package nl.tijsbeek.prisonutilmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static ForgeConfigSpec.ConfigValue<String> ITEMS_PATH;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("The path of your items should be stored here");
        ITEMS_PATH  = builder.define("items_path", "");
    }
}
