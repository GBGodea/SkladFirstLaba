package Benchmark;

import Entities.Buyer;
import Entities.Employee;
import Entities.HelperClasses.Person;
import Entities.Items;
import Generator.RandomBuyerGenerator;
import Generator.RandomEmployeeGenerator;
import Generator.RandomItemsGenerator;
import Statistic.AdvancedReactiveStatistic;
import Statistic.ReactiveStatistic;
import Statistic.Statistic;
import Statistic.utils.Pair;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1)
public class StatisticBenchmark {
    private List<Buyer> buyers;
    private List<Employee> employees;
    private List<Person> persons;
    private List<Items> items;
    private Statistic statistic;
    private ReactiveStatistic reactiveStatistic;
    private AdvancedReactiveStatistic advancedReactiveStatistic;

    // Засыпание треда можно убрать, чтобы benchmark был быстрее
    @Param({"0", "5", "10"})
    private long delay;

    @Param({"5000", "10000", "100000"})
    private int size;

    @Setup(Level.Trial)
    public void setUp() {
        items = new RandomItemsGenerator().generator((size - 1), size);
        buyers = new RandomBuyerGenerator(items).generator((size - 1), size);
        employees = new RandomEmployeeGenerator().generator((size - 1), size);
        persons = Stream.concat(buyers.stream().map(Buyer::person), employees.stream().map(Employee::person))
                .collect(Collectors.toCollection(ArrayList::new));
        statistic = new Statistic(buyers, employees, persons, items);
        reactiveStatistic = new ReactiveStatistic(employees, buyers);
        advancedReactiveStatistic = new AdvancedReactiveStatistic(employees, buyers);
    }

//    @Benchmark
//    public Pair<String, Integer> streamJobPosition() throws InterruptedException {
//        // Засыпание треда можно убрать, чтобы benchmark был быстрее
//        return statistic.getMostFrequentJobPosition(delay);
//    }

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
    public Pair<String, Integer> reactiveJobPosition() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Pair<String, Integer>> result = new AtomicReference<>();

        reactiveStatistic
                .getMostFrequentJobPositionReactive(delay)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        value -> {
                            result.set(value);
                            latch.countDown();
                        },
                        error -> {
                            System.err.println("Ошибка: " + error);
                            latch.countDown();
                        }
                );

        latch.await();
        return result.get();
    }

    @Benchmark
    public Pair<String, Integer> jobPositionWithCustomSubscriber() throws InterruptedException {
        return advancedReactiveStatistic.getMostFrequentJobPositionWithSubscriber(delay);
    }

    //    @Benchmark
//    public List<Pair<UUID, Integer>> diversityByStream() throws InterruptedException {
//        // Засыпание треда можно убрать, чтобы benchmark был быстрее
//        return statistic.getTopBuyersByCategoryDiversitySequential(delay);
//
//    }

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

    @Benchmark
    public List<Pair<UUID, Integer>> reactiveDiversity() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<Pair<UUID, Integer>>> result = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();

        reactiveStatistic
                .getTopBuyersByCategoryDiversity(delay)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        value -> {
                            result.set(value);
                            latch.countDown();
                        },
                        err -> {
                            error.set(err);
                            latch.countDown();
                        }
                );

        if (!latch.await(30, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout: ReactiveStatistic не завершилась за 30 сек");
        }

        if (error.get() != null) {
            throw new RuntimeException("Ошибка в Observable: " + error.get());
        }

        return result.get();
    }

    @Benchmark
    public List<Pair<UUID, Integer>> diversityWithCustomSubscriber() throws InterruptedException {
        return advancedReactiveStatistic.getTopBuyersByCategoryDiversityWithSubscriber(delay);
    }
}
