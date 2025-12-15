package Statistic;

import Entities.Employee;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import Entities.Buyer;
import Entities.Items;
import Entities.HelperClasses.Category;
import Statistic.utils.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ReactiveStatistic {

    private final List<Employee> employees;
    private final List<Buyer> buyers;
    private final Scheduler scheduler;

    public ReactiveStatistic(List<Employee> employees, List<Buyer> buyers) {
        this. employees = employees;
        this.buyers = buyers;
        this.scheduler = Schedulers.computation();
    }

    public Observable<Pair<String, Integer>> getMostFrequentJobPositionReactive(long delay) {
        return Observable.fromIterable(employees)
                .groupBy(Employee::jobPosition)
                .flatMapSingle(group -> group
                        .count()
                        .map(count -> new Pair<>(group.getKey().name(), count.intValue()))
                )
                .reduce((pair1, pair2) ->
                        pair1.value() >= pair2.value() ? pair1 : pair2
                )
                .delay(delay, TimeUnit.MILLISECONDS, scheduler)
                .toObservable();
    }

    public Observable<List<Pair<UUID, Integer>>> getTopBuyersByCategoryDiversity(long delay) {
        return Observable.fromCallable(() -> buyers.parallelStream()
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
                .observeOn(Schedulers.single());
    }
}