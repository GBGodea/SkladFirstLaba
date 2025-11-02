package Benchmark;

import Entities.Buyer;
import Entities.Employee;
import Entities.HelperClasses.Person;
import Entities.Items;
import Generator.RandomBuyerGenerator;
import Generator.RandomEmployeeGenerator;
import Generator.RandomItemsGenerator;
import Statistic.Statistic;
import Statistic.utils.Pair;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1)
public class StatisticBenchmark {
    private List<Buyer> buyers;
    private List<Employee> employees;
    private List<Person> persons;
    private List<Items> items;
    private Statistic statistic;

    // Засыпание треда можно убрать, чтобы benchmark был быстрее
    @Param({"0", "5", "10", "20"})
    private long delay;

    @Param({"1000", "10000", "100000", "1000000"})
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
    public Pair<String, Integer> streamJobPosition() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        return statistic.getMostFrequentJobPosition(delay);
    }

    @Benchmark
    public Pair<String, Integer> parallelStreamJobPosition() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        return statistic.getMostFrequentJobPositionParallel(delay);
    }

    @Benchmark
    public Pair<String, Integer> spliteratorJobPosition() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        return statistic.getMostFrequentJobPositionBySpliterator(delay);
    }

    @Benchmark
    public List<Pair<UUID, Integer>> diversityByStream() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        return statistic.getTopBuyersByCategoryDiversitySequential(delay);

    }

    @Benchmark
    public List<Pair<UUID, Integer>> diversityByParallelStream() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        return statistic.getTopBuyersByCategoryDiversityParallel(delay);
    }

    @Benchmark
    public List<Pair<UUID, Integer>> diversityBySpliterator() throws InterruptedException {
        // Засыпание треда можно убрать, чтобы benchmark был быстрее
        return statistic.getTopBuyersByCategoryDiversityBySpliterator(delay);
    }
}
