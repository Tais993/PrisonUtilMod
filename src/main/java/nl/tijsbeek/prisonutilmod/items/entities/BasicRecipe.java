package nl.tijsbeek.prisonutilmod.items.entities;

import java.util.List;

public record BasicRecipe(
        String type,
        List<String> pattern,
        List<RecipeKey> keys,
        int count
) {
}
