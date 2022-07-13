package nl.tijsbeek.prisonutilmod.items.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
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

    private final String nbtDisplayName;
    private final BasicRecipe recipe;

    @JsonCreator
    public BasicItem(
            @JsonProperty("purchasePrice") double purchasePrice,
            @JsonProperty("sellPrice") double sellPrice,
            @JsonProperty("itemTag") String itemTag,
            @JsonProperty("nbtDisplayName") String nbtDisplayName,
            @JsonProperty("recipe") BasicRecipe recipe
    ) {
        this.purchasePrice = purchasePrice;
        this.sellPrice = sellPrice;
        this.itemTag = itemTag;
        this.item = getItem(itemTag, nbtDisplayName);
        this.nbtDisplayName = nbtDisplayName;
        this.recipe = recipe;
    }

    @NotNull
    private static Item getItem(String itemTag, String nbtDisplayName) {

        ForgeRegistries.ITEMS.getKeys();

        Item registryItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemTag));
        Minecraft.getInstance().player.chat(registryItem.toString());

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("display.Name", nbtDisplayName);


        ItemStack itemStack = new ItemStack(registryItem);
        itemStack.setTag(compoundTag);

        return itemStack.getItem();
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
