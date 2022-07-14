package nl.tijsbeek.prisonutilmod.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import nl.tijsbeek.prisonutilmod.items.entities.BasicItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static nl.tijsbeek.prisonutilmod.config.Config.ITEMS_PATH;

public class ItemLoader {

    private Map<Item, Item> itemToFurnaceResult;
    private Map<Item, BasicItem> itemToBasicItem = Collections.emptyMap();
    private List<BasicItem> items = Collections.emptyList();

    public void loadItems() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String itemsJson = Files.readString(Path.of(ITEMS_PATH.get()));

            items = Collections.unmodifiableList(objectMapper.readValue(itemsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, BasicItem.class)));

            itemToBasicItem = items.stream()
                    .collect(Collectors.toUnmodifiableMap(BasicItem::getItem, Function.identity()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Map<Item, Item> getItemToFurnaceResult() {
        if (itemToFurnaceResult == null) {
            reloadItemToFurnaceResult();
        }

        return itemToFurnaceResult;
    }

    public void reloadItemToFurnaceResult() {
        Map<Item, Item> itemToFurnaceResult = new HashMap<>();

        Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(RecipeType.SMELTING)
                .forEach(recipe -> {
                    Item resultItem = recipe.getResultItem().getItem();

                    recipe.getIngredients()
                            .stream()
                            .map(Ingredient::getItems)
                            .flatMap(Arrays::stream)
                            .map(ItemStack::getItem)
                            .forEach(item -> {
                                itemToFurnaceResult.put(item, resultItem);
                            });
                });

        this.itemToFurnaceResult = itemToFurnaceResult;
    }

    public List<BasicItem> getItems() {
        return items;
    }

    public Map<Item, BasicItem> getItemToBasicItem() {
        return itemToBasicItem;
    }

    public BasicItem getBasicItem(Item item) {
        return itemToBasicItem.get(item);
    }
}
