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

        /*
        Тест без Thread
        // Статистика средний возраст
        Statistic statistics = new Statistic(buyer, employees, persons, items);
        long startTime = System.currentTimeMillis();

        // Средний возраст по работникам - обход циклом
        System.out.println(statistics.averageEmployeeAge());
        // Средний возраст по покупателям - stream API
        System.out.println(statistics.averageBuyerAge());
        // Средний возраст через свой коллектор
        System.out.println(statistics.averagePersonAge());

        // Статистика по имени
        // Статистика через for
        System.out.println(statistics.mostFrequentEmployeeName());
        // Статистика через stream API
        System.out.println(statistics.mostFrequentBuyerName());
        // Статистика через Collector
        System.out.println(statistics.mostFrequentPersonName());

        // Статистика по самой часто встречаемой должности
        System.out.println(statistics.mostFrequentJobPosition());
        // Статистика по наиболее частой категории предмета в корзине пользователя
        System.out.println(statistics.mostFrequentItemInPersonBusket());
        // Статистика по товарам - самое большое количество товара на складе
        System.out.println(statistics.biggestCountOfItem());

        long endTime = System.currentTimeMillis();
        long durationTime = endTime - startTime;
        System.out.println("Execution Time: " + durationTime + " ms");
         */

        /*
        // Статистика средний возраст
        Statistic statistics = new Statistic(buyer, employees, persons, items);
        long startTime = System.currentTimeMillis();

        Thread thread1 = new Thread(() -> {
            // Средний возраст по работникам - обход циклом
            System.out.println(statistics.averageEmployeeAge());
            // Средний возраст по покупателям - stream API
            System.out.println(statistics.averageBuyerAge());
            // Средний возраст через свой коллектор
            System.out.println(statistics.averagePersonAge());
        });


        Thread thread2 = new Thread(() -> {
            // Статистика по имени
            // Статистика через for
            System.out.println(statistics.mostFrequentEmployeeName());
            // Статистика через stream API
            System.out.println(statistics.mostFrequentBuyerName());
            // Статистика через Collector
            System.out.println(statistics.mostFrequentPersonName());
        });

        Thread thread3 = new Thread(() -> {
            // Статистика по самой часто встречаемой должности
            System.out.println(statistics.mostFrequentJobPosition());
            // Статистика по наиболее частой категории предмета в корзине пользователя
            System.out.println(statistics.mostFrequentItemInPersonBusket());
            // Статистика по товарам - самое большое количество товара на складе
            System.out.println(statistics.biggestCountOfItem());
        });
        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        long endTime = System.currentTimeMillis();
        long durationTime = endTime - startTime;
        System.out.println("Execution Time: " + durationTime + " ms");
         */



        // 1 Thread на каждый расчёт статистики
        // Статистика средний возраст
        Statistic statistics = new Statistic(buyer, employees, persons, items);
        long startTime = System.currentTimeMillis();

        Thread thread1 = new Thread(() -> {
            // Средний возраст по работникам - обход циклом
            System.out.println(statistics.averageEmployeeAge());
        });
        Thread thread2 = new Thread(() -> {
            // Средний возраст по покупателям - stream API
            System.out.println(statistics.averageBuyerAge());
        });
        Thread thread3 = new Thread(() -> {
            // Средний возраст через свой коллектор
            System.out.println(statistics.averagePersonAge());
        });

        // Статистика по имени
        Thread thread4 = new Thread(() -> {
            // Статистика через for
            System.out.println(statistics.mostFrequentEmployeeName());
        });
        Thread thread5 = new Thread(() -> {
            // Статистика через stream API
            System.out.println(statistics.mostFrequentBuyerName());
        });
        Thread thread6 = new Thread(() -> {
            // Статистика через Collector
            System.out.println(statistics.mostFrequentPersonName());
        });

        Thread thread7 = new Thread(() -> {
            // Статистика по самой часто встречаемой должности
            System.out.println(statistics.mostFrequentJobPosition());
        });
        Thread thread8 = new Thread(() -> {
            // Статистика по наиболее частой категории предмета в корзине пользователя
            System.out.println(statistics.mostFrequentItemInPersonBusket());
        });
        Thread thread9 = new Thread(() -> {
            // Статистика по товарам - самое большое количество товара на складе
            System.out.println(statistics.biggestCountOfItem());
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        thread6.join();
        thread7.join();
        thread8.join();
        thread9.join();

        long endTime = System.currentTimeMillis();
        long durationTime = endTime - startTime;
        System.out.println("Execution Time: " + durationTime + " ms");

        // Логика расчёта средней зарплаты по всем отдельным профессиям
        Instant start;
        Instant end;

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

        // Статистика поиска 3 покупателей с самой высокой покупательской способностью, по товарм
        System.out.println("\n" + statistics.valueAnalysis());

        // Топ людей по разнообразию корзины
        System.out.println("\nBusket: " + statistics.topByCategoryDiversity());

        // Топ людей по соотношению возраст-зарплата(чем моложе и выше зарплата, тем лучше)
        System.out.println("\nTop age: " + statistics.topByAgeSalaryRatio());

        // Расчёт дисперсии по товарам
        System.out.println("\nDispersia: " + statistics.topByPriceVariance());

        /*
            Все запуски от 1 до 100_000 элементов
            На 20 запусков, без дополнительных потоков Runtime в мс:
            1. 39
            2. 40
            3. 42
            4. 36
            5. 37
            6. 47
            7. 40
            8. 45
            9. 49
            10. 41
            11. 39
            12. 44
            13. 39
            14. 40
            15. 41
            16. 39
            17. 40
            18. 36
            19. 41
            20. 37
            Среднее время:
            812 / 20 = 40,6 мс

            Запуски с Thread - 3 Thread'а на каждые 3 запуска метода статистики
            1. 27
            2. 32
            3. 36
            4. 32
            5. 30
            6. 28
            7. 26
            8. 30
            9. 25
            10. 28
            11. 27
            12. 28
            13. 30
            14. 29
            15. 26
            16. 32
            17. 34
            18. 26
            19. 36
            20. 28
            Среднее время:
            590 / 20 = 29,5 мс

            Запуски с Thread - 1 Thread'а на каждый 1 запуска метода статистики
            1. 25
            2. 24
            3. 26
            4. 25
            5. 30
            6. 26
            7. 26
            8. 25
            9. 27
            10. 25
            11. 26
            12. 24
            13. 27
            14. 25
            15. 27
            16. 26
            17. 27
            18. 25
            19. 29
            20. 26
            Среднее время:
            521 / 20 = 26,05 мс
         */
    }
}