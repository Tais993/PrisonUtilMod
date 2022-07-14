package nl.tijsbeek.prisonutilmod.items.entities;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ItemDisplayNameWrapper(
        @NotNull Item item,
        @Nullable String nbtDisplayName
) {

    public static ItemDisplayNameWrapper ofItem(Item item) {
        return new ItemDisplayNameWrapper(item, null);
    }
}
