package Benchmark;

import Entities.Buyer;
import Entities.Employee;
import Entities.HelperClasses.Category;
import Entities.HelperClasses.JobPosition;
import Entities.HelperClasses.Person;
import Entities.Items;
import Generator.RandomBuyerGenerator;
import Generator.RandomEmployeeGenerator;
import Generator.RandomItemsGenerator;
import Spliterator.BuyerSpliterator;
import Spliterator.EmployeeSpliterator;
import Statistic.Statistic;
import Statistic.utils.Pair;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StatisticBenchmark {
    private List<Buyer> buyers;
    private List<Employee> employees;
    private List<Person> persons;
    private List<Items> items;
    private Statistic statistic;

    // Засыпание треда можно убрать, чтобы benchmark был быстрее
    @Param({"0", "5", "10", "20"})
    private long delay;

    @Param({"1000", "10000", "100000"})
    private int size;

    @Setup(Level.Trial)
    public void setUp() {
        items = new RandomItemsGenerator().generator(size);
        buyers = new RandomBuyerGenerator(items).generator(size);
        employees = new RandomEmployeeGenerator().generator(size);
        persons = Stream.concat(buyers.stream().map(Buyer::person), employees.stream().map(Employee::person))
                .collect(Collectors.toCollection(ArrayList::new));
        statistic = new Statistic(buyers, employees, persons, items);
    }

    @Benchmark
    public Pair<String, Integer> sequentialStreamJobPosition() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        return statistic.mostFrequentJobPosition(delay);
    }

    @Benchmark
    public Pair<String, Integer> parallelStreamJobPosition() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        Thread.sleep(delay);

        ConcurrentMap<JobPosition, Long> map = employees.parallelStream()
                .collect(Collectors.groupingByConcurrent(Employee::jobPosition, Collectors.counting()));

        long max = map.entrySet().parallelStream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue).orElse(0L);

        Optional<JobPosition> result = map.entrySet().parallelStream().filter(e -> e.getValue().equals(max))
                .map(Map.Entry::getKey).findFirst();

        return result.map(jobPosition -> new Pair<>(jobPosition.name(), (int) max)).orElse(null);
    }

    @Benchmark
    public Pair<String, Integer> spliteratorJobPosition() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        Thread.sleep(delay);

        ConcurrentMap<JobPosition, Long> map = StreamSupport.stream(new EmployeeSpliterator(employees), true)
                .collect(Collectors.groupingByConcurrent(Employee::jobPosition, Collectors.counting()));

        long max = map.entrySet().parallelStream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue).orElse(0L);

        Optional<JobPosition> result = map.entrySet().parallelStream().filter(e -> e.getValue().equals(max))
                .map(Map.Entry::getKey).findFirst();

        return result.map(jobPosition -> new Pair<>(jobPosition.name(), (int) max)).orElse(null);
    }

    @Benchmark
    public List<Pair<UUID, Integer>> diversityByStream() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        Thread.sleep(delay);

        Map<UUID, Set<Category>> diversity = buyers.stream()
                .collect(Collectors.toMap(
                        b -> b.person().getId(),
                        b -> b.basket().stream()
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

    @Benchmark
    public List<Pair<UUID, Integer>> diversityByParallelStream() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        Thread.sleep(delay);

        ConcurrentMap<UUID, Set<Category>> diversity = buyers.parallelStream()
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

    @Benchmark
    public List<Pair<UUID, Integer>> diversityBySpliterator() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        return statistic.topByCategoryDiversity(delay);
    }
}
