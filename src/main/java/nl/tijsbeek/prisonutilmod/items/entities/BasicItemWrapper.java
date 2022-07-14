package nl.tijsbeek.prisonutilmod.items.entities;

import net.minecraft.world.item.Item;
import nl.tijsbeek.prisonutilmod.items.ItemLoader;

public record BasicItemWrapper(
        int amount,
        BasicItem basicItem,
        ItemLoader itemLoader
) {

    /**
     * Multiplies the amount of items by the sell-price
     *
     * @return how much worth the items are
     */
    public Double getSellWorth() {
        if (basicItem.smeltBeforeSell()) {
            Item smeltedItem = itemLoader.getItemToFurnaceResult().get(basicItem.getItem());

            BasicItem smeltedBasicItem = itemLoader.getBasicItem(ItemDisplayNameWrapper.ofItem(smeltedItem));

            return amount * smeltedBasicItem.getSellPrice();
        }


        return amount * basicItem.getSellPrice();
    }
}
