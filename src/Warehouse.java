/*
    TODO
    Сделать привязку через DI(самостоятельно написанный DI на reflection)
    чтобы обрабатывать пользователей, и предметы

    Создать отдельный класс для сборка статистики и собственный коллектор
 */

import Entities.Buyer;
import Entities.Employee;
import Entities.Items;
import Entities.StatsClasses.StatsEmpoyee;

import Generator.RandomBuyerGenerator;
import Generator.RandomEmployeeGenerator;
import Generator.RandomItemsGenerator;
import JsonReader.JsonReader;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Warehouse {
    public static void main(String[] args) throws IOException {
        // RandomItemsGenerator randomItemsGenerator = new RandomItemsGenerator();
        // randomItemsGenerator.generator();

        Instant start;
        Instant end;

        RandomBuyerGenerator randomBuyerGenerator = new RandomBuyerGenerator();
        RandomEmployeeGenerator randomEmployeeGenerator = new RandomEmployeeGenerator();
        RandomItemsGenerator randomItemsGenerator = new RandomItemsGenerator();
        List<Buyer> buyer = randomBuyerGenerator.generator();
        List<Employee> employees = randomEmployeeGenerator.generator();
        List<Items> items = randomItemsGenerator.generator();

        System.out.println("\nIteration:");
        start = Instant.now();
        Map<String, Double> result1 = StatsEmpoyee.calculateIteration(employees);
        end = Instant.now();
        System.out.println("Время выполнения:" + Duration.between(start, end).toNanos());
        System.out.println(result1);

        System.out.println("\nStream API:");
        start = Instant.now();
        Map<String, Double> result2 = StatsEmpoyee.calculateStream(employees);
        end = Instant.now();
        System.out.println("Время выполнения:" + Duration.between(start, end).toNanos());
        System.out.println(result2);

        System.out.println("\nКоллектор:");
        start = Instant.now();
        Map<String, Double> result3 = StatsEmpoyee.calculateCollector(employees);
        end = Instant.now();
        System.out.println("Время выполнения:" + Duration.between(start, end).toNanos());
        System.out.println(result3);

    }
}