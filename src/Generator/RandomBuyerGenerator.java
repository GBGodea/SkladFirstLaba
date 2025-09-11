package Generator;

import Entities.Buyer;
import Entities.HelperClasses.Passport;
import Entities.Items;
import Generator.helper.FileReaderClass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/*
    TODO
    Сделать рандомный генератор покупателей
 */
public class RandomBuyerGenerator implements Generator<Buyer> {
    @Override
    public List<Buyer> generator() {
        Random rand = new Random();
        List<Buyer> buyerList = new ArrayList<>();
        List<String> nameList = FileReaderClass.readNameList();
        List<String> surnameList = FileReaderClass.readSurnameList();
        List<Items> items = new RandomItemsGenerator().generator();

        if(items.size() > 0) {
            for (int i = 0; i < rand.nextInt(100_000); i++) {
                LocalDateTime[] times = TimeGenerator.between();
                int firstRand = rand.nextInt(items.size());
                int secondRand;

                if (firstRand >= items.size() - 1) {
                    secondRand = firstRand;
                } else {
                    int bound = rand.nextInt(firstRand + 1, items.size());
                    secondRand = rand.nextInt(firstRand, bound);
                }

                assert nameList != null;
                assert surnameList != null;
                buyerList.add(new Buyer.Builder()
                        .id(UUID.randomUUID())
                        .name(nameList.get(rand.nextInt(nameList.size() - 1)))
                        .surname(surnameList.get(rand.nextInt(surnameList.size() - 1)))
                        .age(rand.nextInt(14, 100))
                        .passport(new Passport(
                                rand.nextInt(1000, 9999),
                                rand.nextInt(100_000, 999_999))
                        )
                        .entryDate(times[0])
                        .releaseDate(times[1])
                        .basket(items.subList(firstRand, secondRand))
                        .build());
            }
        }

        return buyerList;
    }
}
