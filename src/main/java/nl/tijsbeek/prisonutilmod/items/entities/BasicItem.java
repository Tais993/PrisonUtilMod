package nl.tijsbeek.prisonutilmod.items.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    private static Item getItem(String itemTag) {

        ForgeRegistries.ITEMS.getKeys();

        Item registryItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemTag));

        return new ItemStack(registryItem).getItem();
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public String getItemTag() {
        return itemTag;
    }

    public Item getItem() {
        return item;
    }

    public boolean smeltBeforeSell() {
        return smeltBeforeSell;
    }

    public String getNbtDisplayName() {
        return nbtDisplayName;
    }

    public BasicRecipe getRecipe() {
        return recipe;
    }

    /**
     * Checks or the item & NBT tag is the same.
     * This ignores the amount, and other info.
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
