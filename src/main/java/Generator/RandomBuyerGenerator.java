package Generator;

import Entities.Buyer;
import Entities.HelperClasses.Passport;
import Entities.HelperClasses.Person;
import Entities.Items;
import JsonReader.JsonReader;

import java.time.LocalDateTime;
import java.util.*;

public class RandomBuyerGenerator implements Generator<Buyer> {

    List<Items> items;

    public RandomBuyerGenerator(List<Items> items) {
        this.items = items;
    }

    @Override
    public List<Buyer> generator(int size) {
        Random rand = new Random();
        List<Buyer> buyerList = new ArrayList<>();
        Map<String, Map<String, List<String>>> namesMap = JsonReader.readNames();

        if (!items.isEmpty()) {
            int count = rand.nextInt(size);
            for (int i = 0; i < count; i++) {
                String gender = rand.nextBoolean() ? "male" : "female";
                Map<String, List<String>> genderMap = namesMap.get(gender);

                List<String> firstNames = genderMap.get("firstNames");
                List<String> lastNames = genderMap.get("lastNames");

                String name = firstNames.get(rand.nextInt(firstNames.size()));
                String surname = lastNames.get(rand.nextInt(lastNames.size()));

                LocalDateTime[] times = TimeGenerator.between();
                int firstRand = rand.nextInt(items.size());
                int secondRand = firstRand < items.size() - 1
                        ? rand.nextInt(firstRand, items.size())
                        : firstRand;

                Person person = new Person(
                        UUID.randomUUID(),
                        name,
                        surname,
                        rand.nextInt(14, 100),
                        new Passport(rand.nextInt(1000, 9999),
                                rand.nextInt(100_000, 999_999)),
                        times[0],
                        times[1]
                );

                Buyer buyer = new Buyer(
                        person,
                        items.subList(firstRand, secondRand)
                );
                buyerList.add(buyer);
            }
        }
        return buyerList;
    }
}
