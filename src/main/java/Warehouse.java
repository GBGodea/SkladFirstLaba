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
import java.io.PrintWriter;
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

        RandomEmployeeGenerator randomEmployeeGenerator = new RandomEmployeeGenerator();
        RandomItemsGenerator randomItemsGenerator = new RandomItemsGenerator();
        List<Items> items = randomItemsGenerator.generator(100_000);
        RandomBuyerGenerator randomBuyerGenerator = new RandomBuyerGenerator(items);
        List<Buyer> buyer = randomBuyerGenerator.generator(100_000);
        List<Employee> employees = randomEmployeeGenerator.generator(100_000);
        List<Person> persons = Stream.concat(buyer.stream().map(Buyer::person), employees.stream().map(Employee::person))
                .collect(Collectors.toCollection(ArrayList::new));
        PrintWriter writer = new PrintWriter(System.out);

        Instant start;
        Instant end;
        Statistic statistics = new Statistic(buyer, employees, persons, items);

        writer.write("Average buyer age: ");
        writer.write(statistics.averageBuyerAge(0) + "\n\n");

        writer.write("Average employee age: ");
        writer.write(statistics.averageEmployeeAge(0) + "\n\n");

        writer.write("Average person age: ");
        writer.write(statistics.averagePersonAge(0) + "\n\n");

        writer.write("Top buy price variance:\n");
        writer.write(statistics.topByPriceVariance(0) + "\n\n");

        writer.write("Biggest count of item:\n");
        writer.write(statistics.biggestCountOfItem(0) + "\n\n");

        writer.write("Most frequent buyer name:\n");
        writer.write(statistics.mostFrequentBuyerName(0) + "\n\n");

        writer.write("Most frequent employee name:\n");
        writer.write(statistics.mostFrequentEmployeeName(0) + "\n\n");

        writer.write("Most frequent person name:\n");
        writer.write(statistics.mostFrequentPersonName(0) + "\n\n");

        writer.write("Most frequent item in person basket:\n");
        writer.write(statistics.mostFrequentItemInPersonBusket(0) + "\n\n");

        writer.write("Most frequent job position:\n");
        writer.write(statistics.mostFrequentJobPosition(0) + "\n\n");

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

        writer.flush();
        writer.close();
    }
}