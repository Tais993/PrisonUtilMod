package nl.tijsbeek.prisonutilmod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nl.tijsbeek.prisonutilmod.items.ItemEventHandler;
import nl.tijsbeek.prisonutilmod.items.ItemLoader;
import nl.tijsbeek.prisonutilmod.minereset.MineTimer;
import org.slf4j.Logger;

import static nl.tijsbeek.prisonutilmod.config.Config.GENERAL_SPEC;

@Mod("prisonutilmod")
public class PrisonUtilMod {

    private static final Logger LOGGER = LogUtils.getLogger();

    public PrisonUtilMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        ItemLoader itemLoader = new ItemLoader();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MineTimer());
        MinecraftForge.EVENT_BUS.register(new ItemEventHandler(itemLoader));

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GENERAL_SPEC,"prison_util_mod_config.toml");
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
