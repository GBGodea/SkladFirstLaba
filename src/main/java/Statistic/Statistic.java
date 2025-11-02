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
import Spliterator.EmployeeSpliterator;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Statistic {
    private final List<Buyer> buyers;
    private final List<Employee> employees;
    private final List<Triplet<Integer, String, String>> persons;
    private final List<Items> items;

    public Statistic(List<Buyer> buyers,
                     List<Employee> employees,
                     List<Person> persons,
                     List<Items> items) {
        this.buyers = buyers;
        this.employees = employees;
        this.persons = persons.stream().collect(MyCollector.toMyCollector());
        this.items = items;
    }

    private int getAverageEmployeeAge() {
        return (int) employees.stream()
                .mapToInt(emp -> emp.person().getAge())
                .average()
                .orElse(0);
    }

    public int getAverageEmployeeAge(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getAverageEmployeeAge();
    }

    private int getAverageBuyerAge() {
        return (int) buyers.stream()
                .mapToInt(buyer -> buyer.person().getAge())
                .average()
                .orElse(0);
    }

    public int getAverageBuyerAge(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getAverageBuyerAge();
    }

    private int getAveragePersonAge() {
        return (int) persons.stream()
                .mapToInt(Triplet::getAge)
                .average()
                .orElse(0);
    }

    public int getAveragePersonAge(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getAveragePersonAge();
    }

    private Pair<String, Integer> getMostFrequentEmployeeName() {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        emp -> emp.person().getName(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().intValue()))
                .orElse(null);
    }

    public Pair<String, Integer> getMostFrequentEmployeeName(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getMostFrequentEmployeeName();
    }

    private Pair<String, Integer> getMostFrequentBuyerName() {
        return buyers.parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        buyer -> buyer.person().getName(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().intValue()))
                .orElse(null);
    }

    public Pair<String, Integer> getMostFrequentBuyerName(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getMostFrequentBuyerName();
    }

    private Pair<String, Integer> getMostFrequentPersonName() {
        return persons.stream()
                .collect(Collectors.groupingBy(
                        Triplet::getName,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                count -> count
                        )
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().intValue()))
                .orElse(null);
    }

    public Pair<String, Integer> getMostFrequentPersonName(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getMostFrequentPersonName();
    }

    private Pair<String, Integer> getMostFrequentJobPosition() {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::jobPosition,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey().name(), entry.getValue().intValue()))
                .orElse(null);
    }

    public Pair<String, Integer> getMostFrequentJobPosition(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getMostFrequentJobPosition();
    }

    private Pair<String, Integer> getMostFrequentJobPositionParallel() {
        return employees.parallelStream()
                .collect(Collectors.groupingByConcurrent(
                        Employee::jobPosition,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey().name(), entry.getValue().intValue()))
                .orElse(null);
    }

    public Pair<String, Integer> getMostFrequentJobPositionParallel(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getMostFrequentJobPositionParallel();
    }

    private Pair<String, Integer> getMostFrequentJobPositionBySpliterator() {
        return StreamSupport.stream(new EmployeeSpliterator(employees), true)
                .collect(Collectors.groupingByConcurrent(
                        Employee::jobPosition,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey().name(), entry.getValue().intValue()))
                .orElse(null);
    }

    public Pair<String, Integer> getMostFrequentJobPositionBySpliterator(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getMostFrequentJobPositionBySpliterator();
    }

    private Pair<String, Integer> getMostFrequentItemCategoryInBuyers() {
        return buyers.stream()
                .flatMap(buyer -> buyer.basket().stream())
                .collect(Collectors.groupingBy(
                        item -> item.category().toString(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().intValue()))
                .orElse(null);
    }

    public Pair<String, Integer> getMostFrequentItemCategoryInBuyers(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getMostFrequentItemCategoryInBuyers();
    }

    private Pair<String, Integer> getMaxItemCountInWarehouse() {
        return items.stream()
                .max(Comparator.comparingInt(Items::count))
                .map(item -> new Pair<>(item.name(), item.count()))
                .orElse(null);
    }

    public Pair<String, Integer> getMaxItemCountInWarehouse(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getMaxItemCountInWarehouse();
    }

    private Map<UUID, BigDecimal> getTopBuyersByTotalPurchaseValue() {
        return buyers.parallelStream()
                .collect(Collectors.toConcurrentMap(
                        buyer -> buyer.person().getId(),
                        buyer -> buyer.basket().stream()
                                .map(item -> item.price().multiply(BigDecimal.valueOf(item.count())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add),
                        BigDecimal::add
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<UUID, BigDecimal>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<UUID, BigDecimal> getTopBuyersByTotalPurchaseValue(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getTopBuyersByTotalPurchaseValue();
    }

    private List<Pair<UUID, Integer>> getTopBuyersByCategoryDiversitySequential() {
        return buyers.stream()
                .collect(Collectors.toMap(
                        buyer -> buyer.person().getId(),
                        buyer -> buyer.basket().stream()
                                .map(Items::category)
                                .collect(Collectors.toSet()),
                        (set1, set2) -> {
                            set1.addAll(set2);
                            return set1;
                        }
                ))
                .entrySet()
                .stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().size()))
                .sorted(Comparator.comparing(Pair<UUID, Integer>::value).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Pair<UUID, Integer>> getTopBuyersByCategoryDiversitySequential(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getTopBuyersByCategoryDiversitySequential();
    }

    private List<Pair<UUID, Integer>> getTopBuyersByCategoryDiversityParallel() {
        return buyers.parallelStream()
                .collect(Collectors.toConcurrentMap(
                        buyer -> buyer.person().getId(),
                        buyer -> buyer.basket().stream()
                                .map(Items::category)
                                .collect(Collectors.toSet()),
                        (set1, set2) -> {
                            set1.addAll(set2);
                            return set1;
                        }
                ))
                .entrySet()
                .stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().size()))
                .sorted(Comparator.comparing(Pair<UUID, Integer>::value).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Pair<UUID, Integer>> getTopBuyersByCategoryDiversityParallel(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getTopBuyersByCategoryDiversityParallel();
    }

    private List<Pair<UUID, Integer>> getTopBuyersByCategoryDiversityBySpliterator() {
        return StreamSupport.stream(new BuyerSpliterator(buyers), true)
                .collect(Collectors.toConcurrentMap(
                        buyer -> buyer.person().getId(),
                        buyer -> buyer.basket().stream()
                                .map(Items::category)
                                .collect(Collectors.toSet()),
                        (set1, set2) -> {
                            set1.addAll(set2);
                            return set1;
                        }
                ))
                .entrySet()
                .stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue().size()))
                .sorted(Comparator.comparing(Pair<UUID, Integer>::value).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Pair<UUID, Integer>> getTopBuyersByCategoryDiversityBySpliterator(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getTopBuyersByCategoryDiversityBySpliterator();
    }

    private List<Pair<UUID, Double>> getTopEmployeesByAgeSalaryRatio() {
        return employees.stream()
                .map(emp -> {
                    double age = emp.person().getAge();
                    double salary = emp.salary();
                    double ratio = salary == 0 ? Double.MAX_VALUE : age / salary;
                    return new Pair<>(emp.person().getId(), ratio);
                })
                .sorted(Comparator.comparing(Pair<UUID, Double>::value))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Pair<UUID, Double>> getTopEmployeesByAgeSalaryRatio(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getTopEmployeesByAgeSalaryRatio();
    }

    private List<Pair<UUID, Double>> getTopBuyersByPriceVariance() {
        return StreamSupport.stream(new BuyerSpliterator(buyers), true)
                .map(buyer -> {
                    List<Double> prices = buyer.basket().stream()
                            .flatMap(item -> Collections.nCopies(item.count(),
                                    item.price().doubleValue()).stream())
                            .toList();

                    double mean = prices.stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);

                    double variance = prices.stream()
                            .mapToDouble(price -> (price - mean) * (price - mean))
                            .average()
                            .orElse(0.0);

                    return new Pair<>(buyer.person().getId(), variance);
                })
                .sorted(Comparator.comparing(Pair<UUID, Double>::value).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Pair<UUID, Double>> getTopBuyersByPriceVariance(long delay) throws InterruptedException {
        Thread.sleep(delay);
        return getTopBuyersByPriceVariance();
    }
}