package nl.tijsbeek.prisonutilmod.worthcalculator;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nl.tijsbeek.prisonutilmod.items.ItemLoader;
import nl.tijsbeek.prisonutilmod.items.entities.BasicItem;
import nl.tijsbeek.prisonutilmod.items.entities.BasicItemWrapper;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InventoryCalculator {

    private final ItemLoader itemLoader;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    private double inventoryWorth;


    public InventoryCalculator(ItemLoader itemLoader) {
        this.itemLoader = itemLoader;

        executorService.scheduleAtFixedRate(() -> {
            LocalPlayer player = Minecraft.getInstance().player;


            if (player != null) {
                NonNullList<Slot> slots = player.inventoryMenu.slots;

                List<BasicItem> basicItems = itemLoader.getItems();

                inventoryWorth = slots.stream()
                        .map(Slot::getItem)
                        .map(itemStack -> {
                            for (BasicItem basicItem : basicItems) {
                                if (basicItem.isEqualItem(itemStack)) {
                                    return new BasicItemWrapper(itemStack.getCount(), basicItem, itemLoader);
                                }
                            }

                            return null;
                        }).filter(Objects::nonNull)
                        .mapToDouble(BasicItemWrapper::getSellWorth)
                        .sum();
            }

        }, 0, 1, TimeUnit.SECONDS);
    }


    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent event) {
        if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            String format = "€%.2f".formatted(inventoryWorth);

            Minecraft.getInstance().font.drawShadow(new PoseStack(), format, 40, 0, Color.ORANGE.getRGB(), true);
        }
    }
}
