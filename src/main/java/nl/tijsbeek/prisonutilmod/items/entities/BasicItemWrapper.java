package nl.tijsbeek.prisonutilmod.items.entities;

public record BasicItemWrapper(
        int amount,
        BasicItem basicItem
) {


    /**
     * Multiplies the amount of items by the sell-price
     *
     * @return how much worth the items are
     */
    public Double getSellWorth() {
        return amount * basicItem.getSellPrice();
    }
}
