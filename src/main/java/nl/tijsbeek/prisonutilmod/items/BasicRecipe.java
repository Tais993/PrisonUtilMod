package nl.tijsbeek.prisonutilmod.items;

import java.util.List;

public record BasicRecipe(
        String recipeType,
        List<String> patterns,
        List<RecipeKey> recipeKeys,
        int count
) {
}
