/*
    TODO
    Сделать привязку через DI(самостоятельно написанный DI на reflection)
    чтобы обрабатывать пользователей, и предметы

    Создать отдельный класс для сборка статистики и собственный коллектор
 */

import Entities.Buyer;
import Entities.Employee;
import Entities.HelperClasses.JobPosition;
import Entities.HelperClasses.Person;
import Entities.Items;
import Generator.RandomBuyerGenerator;
import Generator.RandomEmployeeGenerator;
import Generator.RandomItemsGenerator;
import Statistic.Statistic;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import Statistic.StatsEmpoyee;

public class Warehouse {
    public static void main(String[] args) throws IOException, InterruptedException {
        RandomBuyerGenerator randomBuyerGenerator = new RandomBuyerGenerator();
        RandomEmployeeGenerator randomEmployeeGenerator = new RandomEmployeeGenerator();
        RandomItemsGenerator randomItemsGenerator = new RandomItemsGenerator();
        List<Buyer> buyer = randomBuyerGenerator.generator();
        List<Employee> employees = randomEmployeeGenerator.generator();
        List<Person> persons = Stream.concat(buyer.stream().map(Buyer::person), employees.stream().map(Employee::person))
                .collect(Collectors.toCollection(ArrayList::new));
        List<Items> items = randomItemsGenerator.generator();

        Instant start;
        Instant end;
        Statistic statistics = new Statistic(buyer, employees, persons, items);

        // Статистика с задержкой

        statistics.topByPriceVariance(0);

//        System.out.println("\nIteration:");
//        start = Instant.now();
//        Map<String, Double> result1 = StatsEmpoyee.calculateIteration(employees);
//        end = Instant.now();
//        System.out.println("Время выполнения:" + Duration.between(start, end).toNanos());
//        System.out.println(result1);
//
//        System.out.println("\nStream API:");
//        start = Instant.now();
//        Map<String, Double> result2 = StatsEmpoyee.calculateStream(employees);
//        end = Instant.now();
//        System.out.println("Время выполнения:" + Duration.between(start, end).toNanos());
//        System.out.println(result2);
//
//        System.out.println("\nКоллектор:");
//        start = Instant.now();
//        Map<String, Double> result3 = StatsEmpoyee.calculateCollector(employees);
//        end = Instant.now();
//        System.out.println("Время выполнения:" + Duration.between(start, end).toNanos());
//        System.out.println(result3);
    }
}