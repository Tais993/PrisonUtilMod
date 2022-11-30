package nl.tijsbeek.prisonutilmod.worthcalculator;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nl.tijsbeek.prisonutilmod.items.ItemLoader;
import nl.tijsbeek.prisonutilmod.items.entities.BasicItem;
import nl.tijsbeek.prisonutilmod.items.entities.ItemDisplayNameWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InventoryCalculator {
    private static final Logger logger = LoggerFactory.getLogger(InventoryCalculator.class);

    private final ItemLoader itemLoader;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    private double inventoryWorth;

    /**
     * Starts the calculations.
     * <p>
     * This schedules a thread, which checks the inventory's worth asynchronously every second.
     * Items are reloaded automatically every time, so changes can be noticed instantly.
     *
     * @param itemLoader the {@link ItemLoader}
     */
    public InventoryCalculator(ItemLoader itemLoader) {
        this.itemLoader = itemLoader;

        executorService.scheduleAtFixedRate(() -> {
            LocalPlayer player = Minecraft.getInstance().player;


            if (player == null) {
                logger.debug("Not calculating inventory's worth, not in a server/world");
            } else {
                NonNullList<Slot> slots = player.inventoryMenu.slots;

                List<BasicItem> basicItems = itemLoader.getItems();

                inventoryWorth = slots.stream()
                        .map(Slot::getItem)
                        .map(itemStack -> {
                            for (BasicItem basicItem : basicItems) {
                                if (basicItem.isEqualItem(itemStack)) {
                                    return getSellWorth(itemStack.getCount(), basicItem, itemLoader);
                                }
                            }

                            return null;
                        })
                        .filter(Objects::nonNull)
                        .mapToDouble(Double::doubleValue)
                        .sum();
            }

        }, 0, 1, TimeUnit.SECONDS);
    }


    /**
     * Multiplies the amount of items by the sell-price
     *
     * @return how much worth the items are
     */
    public static Double getSellWorth(int amount, BasicItem basicItem, ItemLoader itemLoader) {
        if (basicItem.smeltBeforeSell()) {
            Item smeltedItem = itemLoader.getItemToFurnaceResult().get(basicItem.getItem());

            BasicItem smeltedBasicItem = itemLoader.getBasicItem(ItemDisplayNameWrapper.ofItem(smeltedItem));

            return getSellWorth(amount, smeltedBasicItem, itemLoader);
        }


        return amount * basicItem.getSellPrice();
    }


    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent event) {
        if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            String format = "€%.2f".formatted(inventoryWorth);

            Minecraft.getInstance().font.drawShadow(new PoseStack(), format, 40, 0, Color.ORANGE.getRGB(), true);
        }
    }
}
