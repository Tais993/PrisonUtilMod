package nl.tijsbeek.prisonutilmod.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public record BasicItem(
        double purchasePrice,
        double sellPrice,
        String itemTag,
        Item item,
        String nbtDisplayName,
        BasicRecipe basicRecipe
) {

    public BasicItem(double purchasePrice, double sellPrice, String itemTag, String nbtDisplayName, BasicRecipe basicRecipe) {
        this(purchasePrice, sellPrice, itemTag, getItem(itemTag, nbtDisplayName), nbtDisplayName, basicRecipe);
    }

    @NotNull
    private static Item getItem(String itemTag, String nbtDisplayName) {
        Item registryItem = RegistryObject.create(new ResourceLocation(itemTag), ForgeRegistries.ITEMS).get();

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("display.Name", nbtDisplayName);


        ItemStack itemStack = new ItemStack(registryItem);
        itemStack.setTag(compoundTag);

        return itemStack.getItem();
    }
}
