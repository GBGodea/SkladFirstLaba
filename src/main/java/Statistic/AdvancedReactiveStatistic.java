package Statistic;

import Entities.Buyer;
import Entities.Employee;
import Entities.Items;
import Statistic.utils.CustomAdaptiveSubscriber;
import Statistic.utils.Pair;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class AdvancedReactiveStatistic {
    private final List<Employee> employees;
    private final List<Buyer> buyers;
    private final Scheduler scheduler;

    public AdvancedReactiveStatistic(List<Employee> employees, List<Buyer> buyers) {
        this.employees = employees;
        this.buyers = buyers;
        this.scheduler = Schedulers.computation();
    }

    public Pair<String, Integer> getMostFrequentJobPositionWithSubscriber(long delay)
            throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Pair<String, Integer>> result = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();

        CustomAdaptiveSubscriber<Pair<String, Integer>, Pair<String, Integer>> subscriber =
                new CustomAdaptiveSubscriber<>(
                        16,
                        10,
                        pair -> pair,
                        pair -> result.set(pair)
                );

        Flowable.fromIterable(employees)
                .groupBy(Employee::jobPosition)
                .flatMapSingle(group -> group
                        .count()
                        .map(count -> new Pair<>(group.getKey().name(), count.intValue()))
                )
                .reduce((pair1, pair2) ->
                        pair1.value() >= pair2.value() ? pair1 : pair2
                )
                .delay(delay, TimeUnit.MILLISECONDS, scheduler)
                .toFlowable()
                .doOnError(err -> error.set(err))
                .doOnComplete(latch::countDown)
                .subscribe(subscriber);

        if (!latch.await(30, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout: AdvancedStatistic getMostFrequentJobPosition");
        }

        if (error.get() != null) {
            throw new RuntimeException("Error: " + error.get());
        }

        return result.get();
    }

    public List<Pair<UUID, Integer>> getTopBuyersByCategoryDiversityWithSubscriber(long delay)
            throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<List<Pair<UUID, Integer>>> result = new AtomicReference<>(new ArrayList<>());
        AtomicReference<Throwable> error = new AtomicReference<>();

        CustomAdaptiveSubscriber<Pair<UUID, Integer>, Pair<UUID, Integer>> subscriber =
                new CustomAdaptiveSubscriber<>(
                        32,
                        15,
                        pair -> pair,
                        pair -> result.get().add(pair)
                );

        Flowable.fromCallable(() -> buyers.parallelStream()
                        .collect(Collectors.toConcurrentMap(
                                buyer -> buyer.person().getId(),
                                buyer -> buyer.basket().stream()
                                        .map(Items::category)
                                        .collect(Collectors.toSet()),
                                (set1, set2) -> {
                                    set1.addAll(set2);
                                    return set1;
                                }))
                        .entrySet()
                        .stream()
                        .map(entry -> new Pair<>(entry.getKey(), entry.getValue().size()))
                        .sorted(Comparator.comparing(Pair<UUID, Integer>::value).reversed())
                        .limit(10)
                        .collect(Collectors.toList()))
                .subscribeOn(scheduler)
                .delay(delay, TimeUnit.MILLISECONDS, scheduler)
                .observeOn(Schedulers.single())
                .flatMap(Flowable::fromIterable)
                .doOnError(err -> error.set(err))
                .doOnComplete(latch::countDown)
                .subscribe(subscriber);

        if (!latch.await(30, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout: AdvancedStatistic getTopBuyersByCategoryDiversity");
        }

        if (error.get() != null) {
            throw new RuntimeException("Error: " + error.get());
        }

        return result.get();
    }
}
