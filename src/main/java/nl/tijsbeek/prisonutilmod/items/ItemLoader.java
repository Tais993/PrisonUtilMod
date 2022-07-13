package nl.tijsbeek.prisonutilmod.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.tijsbeek.prisonutilmod.items.entities.BasicItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static nl.tijsbeek.prisonutilmod.config.Config.ITEMS_PATH;

public class ItemLoader {

    private List<BasicItem> items;

    public void loadItems() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String itemsJson = Files.readString(Path.of(ITEMS_PATH.get()));

            items = objectMapper.readValue(itemsJson, objectMapper.getTypeFactory().constructCollectionType(List.class, BasicItem.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BasicItem> getItems() {
        return items;
    }
}
