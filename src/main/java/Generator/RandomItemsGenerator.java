package Generator;

import Entities.HelperClasses.Category;
import Entities.HelperClasses.Colors;
import Entities.HelperClasses.ShippingMethod;
import Entities.Items;
import JsonReader.JsonReader;

import java.math.BigDecimal;
import java.util.*;

public class RandomItemsGenerator implements Generator<Items> {
    @Override
    public List<Items> generator(int size) {
        Random rand = new Random();
        List<Items> itemsList = new ArrayList<>();
        Map<String, List<String>> parsedJson = JsonReader.readItems();
        String[] categories = parsedJson.keySet().toArray(new String[0]);
        Colors[] colors = Colors.values();
        ShippingMethod[] methods = ShippingMethod.values();

        for (int i = 0; i < rand.nextInt(size); i++) {
            Category category = Category.valueOf(categories[rand.nextInt(categories.length)].replaceAll(" ", ""));
            List<String> takedItems = parsedJson.get(category.toString());
            String item = takedItems.get(rand.nextInt(takedItems.size()));
            Colors color = colors[rand.nextInt(colors.length)];
            int[] itemSize = new int[3];
            BigDecimal price = new BigDecimal(0);

            switch (category) {
                case Electronics -> {
                    itemSize[0] = rand.nextInt(1, 5);
                    itemSize[1] = rand.nextInt(1, 5);
                    itemSize[2] = rand.nextInt(1, 5);
                }
                case OfficeSupplies -> {
                    itemSize[0] = rand.nextInt(1, 2);
                    itemSize[1] = rand.nextInt(1, 2);
                    itemSize[2] = rand.nextInt(1, 2);
                }
                case HouseholdItems -> {
                    itemSize[0] = rand.nextInt(3, 8);
                    itemSize[1] = rand.nextInt(3, 8);
                    itemSize[2] = rand.nextInt(3, 8);
                }
                case PersonalAccessories -> {
                    itemSize[0] = rand.nextInt(1, 3);
                    itemSize[1] = rand.nextInt(1, 3);
                    itemSize[2] = rand.nextInt(1, 3);
                }
                case Tools -> {
                    itemSize[0] = rand.nextInt(2, 4);
                    itemSize[1] = rand.nextInt(2, 4);
                    itemSize[2] = rand.nextInt(2, 4);
                }
            }

            switch (category) {
                case Electronics -> price = new BigDecimal(rand.nextDouble(50_000, 100_000));
                case OfficeSupplies -> price = new BigDecimal(rand.nextDouble(300, 1000));
                case HouseholdItems -> price = new BigDecimal(rand.nextDouble(100, 3000));
                case PersonalAccessories -> price = new BigDecimal(rand.nextDouble(1000, 30_000));
                case Tools -> price = new BigDecimal(rand.nextDouble(700, 5000));
            }

            itemsList.add(
                    new Items(
                            UUID.randomUUID(),
                            item,
                            category,
                            color,
                            itemSize,
                            rand.nextInt(1, 300),
                            price,
                            methods[rand.nextInt(methods.length)]
                    )
            );

//            itemsList.add(
//                    new Items.Builder()
//                            .id(UUID.randomUUID())
//                            .category(category)
//                            .name(item)
//                            .color(color)
//                            .size(new int[] {rand.nextInt(1000), rand.nextInt(1000), rand.nextInt(1000)})
//                            .count(rand.nextInt(100))
//                            .price(rand.nextDouble(1_000_000))
//                            .shippingMethod(methods[rand.nextInt(methods.length)])
//                            .build()
//            );
        }
        return itemsList;
    }
}
