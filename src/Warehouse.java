/*
    TODO
    Сделать привязку через DI(самостоятельно написанный DI на reflection)
    чтобы обрабатывать пользователей, и предметы

    Создать отдельный класс для сборка статистики и собственный коллектор
 */

import Entities.Buyer;
import Entities.Employee;
import Entities.Items;
import Generator.RandomBuyerGenerator;
import Generator.RandomEmployeeGenerator;
import Generator.RandomItemsGenerator;
import JsonReader.JsonReader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Warehouse {
    public static void main(String[] args) throws IOException {
//        RandomItemsGenerator randomItemsGenerator = new RandomItemsGenerator();
//        randomItemsGenerator.generator();

        RandomBuyerGenerator randomBuyerGenerator = new RandomBuyerGenerator();
        RandomEmployeeGenerator randomEmployeeGenerator = new RandomEmployeeGenerator();
        RandomItemsGenerator randomItemsGenerator = new RandomItemsGenerator();
        List<Buyer> buyer = randomBuyerGenerator.generator();
        List<Employee> employees = randomEmployeeGenerator.generator();
        List<Items> items = randomItemsGenerator.generator();
    }
}