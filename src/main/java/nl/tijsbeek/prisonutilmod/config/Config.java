package nl.tijsbeek.prisonutilmod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Contract;

import java.time.Instant;

public class Config {
    public static final ForgeConfigSpec GENERAL_SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> ITEMS_PATH;
    public static final ForgeConfigSpec.ConfigValue<Long> MINE_RESET_COOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Long> LAST_TIME_MINE_RESET;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        ITEMS_PATH  = builder.define("items_path", "");

        builder.comment("Seconds it takes for the mine to reset");
        MINE_RESET_COOLDOWN = builder.define("mine_reset_cooldown", 300L);

        builder.comment("The following items should most likely be ignored, these are automatically modified by the mod.");

        builder.comment("Unix timestamp");
        LAST_TIME_MINE_RESET = builder.define("last_time_mine_reset", Instant.now().toEpochMilli());

        GENERAL_SPEC = builder.build();
    }

    @Contract(value = " -> fail", pure = true)
    private Config() {
        throw new IllegalStateException("Class shouldn't be initiated!");
    }
}
