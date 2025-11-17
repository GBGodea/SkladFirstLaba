package Statistic;

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

    private final List<Buyer> buyers;
    private final Scheduler scheduler;

    public ReactiveStatistic(List<Buyer> buyers) {
        this.buyers = buyers;
        this.scheduler = Schedulers.computation();
    }

    public ReactiveStatistic(List<Buyer> buyers, Scheduler scheduler) {
        this.buyers = buyers;
        this.scheduler = scheduler;
    }

    public Observable<List<Pair<UUID, Integer>>> getTopBuyersByCategoryDiversityByObservable(long delay) {
        return Observable.just(buyers)
                .delay(delay, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribeOn(Schedulers.io())
                .map(list -> {
                    return list.parallelStream()
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
                            .collect(Collectors.toList());
                });
    }
}