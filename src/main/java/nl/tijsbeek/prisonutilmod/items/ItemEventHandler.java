package nl.tijsbeek.prisonutilmod.items;

import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemEventHandler {

    private final ItemLoader itemLoader;

    public ItemEventHandler(ItemLoader itemLoader) {
        this.itemLoader = itemLoader;
    }

    @SubscribeEvent
    public void onClientChatEvent(ClientChatEvent event) {
        String message = event.getMessage();

        if (message.equals("!reload")) {
            itemLoader.loadItems();

            event.setCanceled(true);
        }
    }
}
