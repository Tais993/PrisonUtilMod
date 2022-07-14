package nl.tijsbeek.prisonutilmod.items.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Contains info about items.
 * <p>
 * This includes:
 * <br/>Their {@link #getSellPrice() sell price}, the {@link #getItem() item}, their {@link #getNbtDisplayName() display name}, the {@link #getRecipe() recipe} and more.
 * <p>
 * For checking or items are the same, {@link #isEqualItem(ItemStack)} is recommended.
 */
public class BasicItem {

    private final double purchasePrice;
    private final double sellPrice;
    private final String itemTag;
    private final Item item;
    private final boolean smeltBeforeSell;

    private final String nbtDisplayName;
    private final BasicRecipe recipe;

    @JsonCreator
    public BasicItem(
            @JsonProperty("purchasePrice") double purchasePrice,
            @JsonProperty("sellPrice") double sellPrice,
            @JsonProperty("itemTag") String itemTag,
            @JsonProperty("smeltBeforeSell") boolean smeltBeforeSell,
            @JsonProperty("nbtDisplayName") String nbtDisplayName,
            @JsonProperty("recipe") BasicRecipe recipe
    ) {
        this.purchasePrice = purchasePrice;
        this.sellPrice = sellPrice;
        this.itemTag = itemTag;
        this.item = getItem(itemTag);
        this.smeltBeforeSell = smeltBeforeSell;
        this.nbtDisplayName = nbtDisplayName;
        this.recipe = recipe;
    }

    /**
     * Gets the Minecraft {@link Item} instance of the given tag.
     * <p>
     * Example tag is {@code minecraft:air}, or {@code minecraft:gold_ingot}.
     *
     * @param itemTag the item's tag
     *
     * @return the {@link Item item} attached to the tag
     *
     * @see <a href="https://minecraft.fandom.com/wiki/Tag">Minecraft wiki on tag's</a>
     */
    @NotNull
    private static Item getItem(String itemTag) {
        ForgeRegistries.ITEMS.getKeys();

        Item registryItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemTag));

        return new ItemStack(registryItem).getItem();
    }

    /**
     * The price the individual item can be purchased at.
     *
     * @return the purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * The price the individual item can be sold at.
     *
     * @return the sell price
     */
    public double getSellPrice() {
        return sellPrice;
    }

    /**
     * The tag used to get the {@link #getItem() item} with.
     * <br/>This most likely isn't the old tag possible to get the item, just one of the tags.
     *
     * @return the item's tag
     */
    public String getItemTag() {
        return itemTag;
    }

    public Item getItem() {
        return item;
    }

    public boolean smeltBeforeSell() {
        return smeltBeforeSell;
    }

    /**
     * The display name the item has.
     * <p>
     * This is to differentiate items based on their display name, of the same type.
     *
     * @return the display name
     */
    public String getNbtDisplayName() {
        return nbtDisplayName;
    }

    /**
     * The {@link BasicRecipe} to create this item, this is only for crafting.
     *
     * @return the recipe
     */
    public BasicRecipe getRecipe() {
        return recipe;
    }

    /**
     * Creates a wrapper with the item type, and the display name.
     *
     * @return an {@link ItemDisplayNameWrapper} instance
     */
    public ItemDisplayNameWrapper toItemDisplayNameWrapper() {
        return new ItemDisplayNameWrapper(item, nbtDisplayName);
    }

    /**
     * Checks or the item & NBT tag is the same.
     * <br/>This ignores the amount, and other info.
     *
     * @param itemStack the item to check equality with
     *
     * @return whenever it's the same name & NBT display tag
     */
    public boolean isEqualItem(ItemStack itemStack) {
        if (nbtDisplayName != null) {

            String displayName = itemStack.getDisplayName().getString();

            if (!displayName.endsWith(nbtDisplayName + ']')) {
                return false;
            }
        }

        Item item1 = itemStack.getItem();
        return item1.equals(item);
    }


    @NonNls
    @NotNull
    @Override
    public String toString() {
        return "BasicItem{" +
                "purchasePrice=" + purchasePrice +
                ", sellPrice=" + sellPrice +
                ", itemTag='" + itemTag + '\'' +
                ", item=" + item +
                ", nbtDisplayName='" + nbtDisplayName + '\'' +
                ", recipe=" + recipe +
                '}';
    }
}
