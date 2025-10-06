package Statistic;

import Collector.MyCollector;
import Collector.utils.Triplet;
import Entities.Buyer;
import Entities.Employee;
import Entities.HelperClasses.Category;
import Entities.HelperClasses.JobPosition;
import Entities.HelperClasses.Person;
import Entities.Items;
import Statistic.utils.Pair;
import Spliterator.BuyerSpliterator;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Statistic {
    List<Buyer> buyers;
    List<Employee> employees;
    List<Triplet<Integer, String, String>> persons;
    List<Items> items;

    public Statistic(List<Buyer> buyers,
                     List<Employee> employees,
                     List<Person> persons,
                     List<Items> items) {
        this.buyers = buyers;
        this.employees = employees;
        this.persons = persons.stream().collect(MyCollector.toMyCollector());
        this.items = items;
    }

    // Среднее по возрасту
    private int averageEmployeeAge() {
        int sum = 0;
        for (int i = 0; i < employees.size(); i++) {
            sum += employees.get(i).person().getAge();
        }
        return sum / employees.size();
    }

    public int averageEmployeeAge(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return averageEmployeeAge();
    }

    private int averageBuyerAge() {
        return (int) buyers.stream().mapToInt(buyers -> buyers.person().getAge()).average().orElse(0);
    }

    public int averageBuyerAge(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return averageBuyerAge();
    }

    private int averagePersonAge() {
        return (int) persons.stream().mapToInt(Triplet::getAge).average().orElse(0);
    }

    public int averagePersonAge(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return averagePersonAge();
    }

    // Самое часто встречаемое имя
    private Pair<String, Integer> mostFrequentEmployeeName() {
        Map<String, Integer> map = new HashMap<>();

        Pair<String, Integer> frequent = new Pair<>("", 0);
        for (Employee employee : employees) {
            String name = employee.person().getName();
            if (map.containsKey(name)) {
                map.put(name, map.getOrDefault(name, 0) + 1);
            } else {
                map.put(name, 1);
            }

            if (frequent.value() < map.get(name)) {
                frequent = new Pair<>(name, map.get(name));
            }
        }
        return frequent;
    }

    public Pair<String, Integer> mostFrequentEmployeeName(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return mostFrequentEmployeeName();
    }

    // Добавлена логика Параллельных стримов
    private Pair<String, Integer> mostFrequentBuyerName() {
        ConcurrentHashMap<String, Long> map = buyers.parallelStream()
                .collect(Collectors.groupingBy(
                        buyer -> buyer.person().getName(),
                        ConcurrentHashMap::new,
                        Collectors.counting()
                ));

        long max = map.entrySet().parallelStream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0L);

        Optional<String> result = map.entrySet().parallelStream()
                .filter(e -> e.getValue().equals(max))
                .map(Map.Entry::getKey)
                .findFirst();

        return result.map(name -> new Pair<>(name, (int) max)).orElse(null);
    }

    public Pair<String, Integer> mostFrequentBuyerName(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return mostFrequentBuyerName();
    }

    private Pair<String, Integer> mostFrequentPersonName() {
        Map<String, Long> map = persons.stream()
                .collect(Collectors.groupingBy(Triplet::getName, Collectors.counting()));

        long max = map.values().stream()
                .max(Long::compareTo)
                .orElse(0L);

        Optional<String> result = map.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .findFirst();

        return result.map(name -> new Pair<>(name, (int) max)).orElse(null);
    }

    public Pair<String, Integer> mostFrequentPersonName(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return mostFrequentPersonName();
    }


    // Самая часто встречаемая профессия
    private Pair<String, Integer> mostFrequentJobPosition() {
        Map<JobPosition, Long> map = employees.stream().collect(Collectors.groupingBy(Employee::jobPosition, Collectors.counting()));

        long max = map.entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue).orElse(0L);

        Optional<JobPosition> result = map.entrySet().stream().filter(e -> e.getValue().equals(max))
                .map(Map.Entry::getKey).findFirst();

        return result.map(jobPosition -> new Pair<>(jobPosition.name(), (int) max)).orElse(null);
    }

    public Pair<String, Integer> mostFrequentJobPosition(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return mostFrequentJobPosition();
    }

    // Категория наиболее часто выбираемого товара в корзине
    private Pair<String, Integer> mostFrequentItemInPersonBusket() {
        return buyers.stream().flatMap(buyer -> buyer.basket().stream())
                .collect(Collectors.groupingBy(item -> item.category().toString(),
                        Collectors.reducing(0, e -> 1, Integer::sum)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    public Pair<String, Integer> mostFrequentItemInPersonBusket(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return mostFrequentItemInPersonBusket();
    }

    // Самое большое количество товара на складе
    private Pair<String, Integer> biggestCountOfItem() {
        return items.stream()
                .max(Comparator.comparingInt(Items::count))
                .map(item -> new Pair<>(item.name(), item.count()))
                .orElse(null);
    }

    public Pair<String, Integer> biggestCountOfItem(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return biggestCountOfItem();
    }

    // Анализ ценности покупателей(Высчитывает суммарную покупательскую способность пользователя по корзине с товарами)
    // Добавлена логика Параллельных Стримов
    private Map<UUID, BigDecimal> valueAnalysis() {
        return buyers.parallelStream()
                .collect(Collectors.toConcurrentMap(
                        b -> b.person().getId(),
                        b -> b.basket().stream()
                                .map(item -> item.price().multiply(BigDecimal.valueOf(item.count())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add),
                        BigDecimal::add
                ))
                .entrySet()
                .parallelStream()
                .sorted(Map.Entry.<UUID, BigDecimal>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        )
                );
    }

    public Map<UUID, BigDecimal> valueAnalysis(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return valueAnalysis();
    }

    // Топ людей по разнообразию покупок
    // Добавлен собственный Spliterator для обработки данных
    private List<Pair<UUID, Integer>> topByCategoryDiversity() {
        ConcurrentMap<UUID, Set<Category>> diversity = StreamSupport.stream(new BuyerSpliterator(buyers), true)
                .collect(Collectors.toConcurrentMap(
                        b -> b.person().getId(),
                        b -> b.basket().parallelStream()
                                .map(Items::category)
                                .collect(Collectors.toSet()),
                        (s1, s2) -> {
                            s1.addAll(s2);
                            return s1;
                        }
                ));

        List<Pair<UUID, Integer>> list = diversity.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue().size())).toList();

        return list.stream()
                .sorted(Comparator.comparing(Pair<UUID, Integer>::value).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Pair<UUID, Integer>> topByCategoryDiversity(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return topByCategoryDiversity();
    }

    // Топ работников по соотношению возраста к зарплате
    // Метод с задержкой
    private List<Pair<UUID, Double>> topByAgeSalaryRatio() {
        return employees.stream()
                .map(e -> {
                    double age = e.person().getAge();
                    double salary = e.salary();
                    double ratio = salary == 0 ? Double.MAX_VALUE : age / salary;
                    return new Pair<>(e.person().getId(), ratio);
                })
                .sorted(Comparator.comparing(Pair<UUID, Double>::value))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Pair<UUID, Double>> topByAgeSalaryRatio(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return topByAgeSalaryRatio();
    }

    // Вычисление статической дисперсии цен всех товаров в корзине
    // Добавлен собственный Spliterator
    private List<Pair<UUID, Double>> topByPriceVariance() {
        return StreamSupport.stream(new BuyerSpliterator(buyers), true)
                .map(b -> {
                    List<Double> prices = b.basket().stream()
                            .flatMap(item ->
                                    Collections.nCopies(item.count(), item.price().doubleValue()).stream()
                            ).toList();

                    double mean = prices.stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);

                    double variance = prices.stream()
                            .mapToDouble(p -> (p - mean) * (p - mean))
                            .average()
                            .orElse(0.0);

                    return new Pair<>(b.person().getId(), variance);
                })
                .sorted(Comparator.comparing(Pair<UUID, Double>::value).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Pair<UUID, Double>> topByPriceVariance(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return topByPriceVariance();
    }
}
