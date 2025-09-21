package Generator;

import Entities.HelperClasses.ShippingMethod;
import Entities.Items;
import Generator.utils.FileReaderClass;
import JsonReader.JsonReader;

import java.util.*;

public class RandomItemsGenerator implements Generator<Items> {
    @Override
    public List<Items> generator() {
        Random rand = new Random();
        List<Items> itemsList = new ArrayList<>();
        JsonReader jsonReader = new JsonReader();
        Map<String, List<String>> parsedJson = jsonReader.read();
        String[] categories = parsedJson.keySet().toArray(new String[0]);
        List<String> readFile = FileReaderClass.readColors();
        ShippingMethod[] methods = ShippingMethod.values();

        for(int i = 0; i < rand.nextInt(100_000_000); i++) {
            String category = categories[rand.nextInt(categories.length)];
            List<String> takedItems = parsedJson.get(category);
            String item = takedItems.get(rand.nextInt(takedItems.size()));
            String color = readFile.get(rand.nextInt(readFile.size()));

            itemsList.add(
                    new Items.Builder()
                            .id(UUID.randomUUID())
                            .category(category)
                            .name(item)
                            .color(color)
                            .size(new int[] {rand.nextInt(1000), rand.nextInt(1000), rand.nextInt(1000)})
                            .count(rand.nextInt(100))
                            .price(rand.nextDouble(1_000_000))
                            .shippingMethod(methods[rand.nextInt(methods.length)])
                            .build()
            );
        }
        return itemsList;
    }
}
