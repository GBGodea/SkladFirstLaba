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
import Statistic.AdvancedReactiveStatistic;
import Statistic.ReactiveStatistic;
import Statistic.Statistic;
import Statistic.utils.Pair;
import io.reactivex.rxjava3.core.Observable;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
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
        List<Person> persons = Stream
                .concat(buyer.stream().map(Buyer::person), employees.stream().map(Employee::person))
                .collect(Collectors.toCollection(ArrayList::new));
        PrintWriter writer = new PrintWriter(System.out);

        Instant start;
        Instant end;
        Statistic statistics = new Statistic(buyer, employees, persons, items);
        ReactiveStatistic reactivestatistic = new ReactiveStatistic(buyer, null);
        AdvancedReactiveStatistic advancedreactivestatistic = new AdvancedReactiveStatistic(buyer);

        writer.write("Average buyer age: ");
        writer.write(statistics.getAverageBuyerAge(0) + "\n\n");

        writer.write("Average employee age: ");
        writer.write(statistics.getAverageEmployeeAge(0) + "\n\n");

        writer.write("Average person age: ");
        writer.write(statistics.getAveragePersonAge(0) + "\n\n");

        writer.write("Top buy price variance:\n");
        writer.write(statistics.getTopBuyersByPriceVariance(0) + "\n\n");

        writer.write("Biggest count of item:\n");
        writer.write(statistics.getMaxItemCountInWarehouse(0) + "\n\n");

        writer.write("Most frequent buyer name:\n");
        writer.write(statistics.getMostFrequentBuyerName(0) + "\n\n");

        writer.write("Most frequent employee name:\n");
        writer.write(statistics.getMostFrequentEmployeeName(0) + "\n\n");

        writer.write("Most frequent person name:\n");
        writer.write(statistics.getMostFrequentPersonName(0) + "\n\n");

        writer.write("Most frequent item in person basket:\n");
        writer.write(statistics.getMostFrequentItemCategoryInBuyers(0) + "\n\n");

        writer.write("Most frequent job position:\n");
        writer.write(statistics.getMostFrequentJobPosition(0) + "\n\n");

        start = Instant.now();
        try {
            List<Pair<UUID, Integer>> result1 = statistics.getTopBuyersByCategoryDiversityParallel(50);
            List<Pair<UUID, Integer>> result2 = statistics.getTopBuyersByCategoryDiversityParallel(50);
            List<Pair<UUID, Integer>> result3 = statistics.getTopBuyersByCategoryDiversityParallel(50);
            // writer.write("parallel" + result1 + "\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        end = Instant.now();
        writer.write("Parallel " + Duration.between(start, end).toMillis() + " ms\n");

        start = Instant.now();
        Observable<List<Pair<UUID, Integer>>> obs1 = reactivestatistic
                .getTopBuyersByCategoryDiversityByObservable(50);
        Observable<List<Pair<UUID, Integer>>> obs2 = reactivestatistic
                .getTopBuyersByCategoryDiversityByObservable(50);
        Observable<List<Pair<UUID, Integer>>> obs3 = reactivestatistic
                .getTopBuyersByCategoryDiversityByObservable(50);

        Observable.zip(obs1, obs2, obs3, (r1, r2, r3) -> r1)
                .blockingSubscribe();
        end = Instant.now();

        writer.write("Reactive " + Duration.between(start, end).toMillis() + " ms\n");

        start = Instant.now();
        List<Pair<UUID, Integer>> res1 = advancedreactivestatistic.getTopBuyersByCategoryDiversityWithBackpressure(5);
        end = Instant.now();
        writer.write("Reactive advance" + Duration.between(start, end).toMillis() + " ms\n");
        // System.out.println("\nIteration:");
        // start = Instant.now();
        // Map<String, Double> result1 = StatsEmpoyee.calculateIteration(employees);
        // end = Instant.now();
        // System.out.println("Время выполнения:" + Duration.between(start,
        // end).toNanos());
        // System.out.println(result1);
        //
        // System.out.println("\nStream API:");
        // start = Instant.now();
        // Map<String, Double> result2 = StatsEmpoyee.calculateStream(employees);
        // end = Instant.now();
        // System.out.println("Время выполнения:" + Duration.between(start,
        // end).toNanos());
        // System.out.println(result2);
        //
        // System.out.println("\nКоллектор:");
        // start = Instant.now();
        // Map<String, Double> result3 = StatsEmpoyee.calculateCollector(employees);
        // end = Instant.now();
        // System.out.println("Время выполнения:" + Duration.between(start,
        // end).toNanos());
        // System.out.println(result3);

        writer.flush();
        writer.close();
    }
}