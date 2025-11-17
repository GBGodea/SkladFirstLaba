package Statistic;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.ResourceSubscriber;
import Entities.Buyer;
import Entities.Items;
import Statistic.utils.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class AdvancedReactiveStatistic {
    private final List<Buyer> buyers;

    public AdvancedReactiveStatistic(List<Buyer> buyers) {
        this.buyers = buyers;
    }

    public List<Pair<UUID, Integer>> getTopBuyersByCategoryDiversityWithBackpressure(long delay)
            throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);
        CategoryDiversitySubscriber subscriber = new CategoryDiversitySubscriber(latch);

        Flowable<List<Buyer>> flowable = Flowable.just(buyers);

        if (delay > 0) {
            flowable = flowable.delay(delay, TimeUnit.MILLISECONDS, Schedulers.io());
        }

        flowable
                .flatMap(Flowable::fromIterable)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation(), false, 100)
                .subscribe(subscriber);

        latch.await(30, TimeUnit.SECONDS);
        return subscriber.getTopBuyers();
    }

    private static class CategoryDiversitySubscriber extends ResourceSubscriber<Buyer> {
        private final ConcurrentHashMap<UUID, Set<String>> buyerCategories;
        private final CountDownLatch latch;
        private final AtomicReference<List<Pair<UUID, Integer>>> topBuyers;
        private static final int BATCH_SIZE = 50;

        public CategoryDiversitySubscriber(CountDownLatch latch) {
            this.latch = latch;
            this.buyerCategories = new ConcurrentHashMap<>();
            this.topBuyers = new AtomicReference<>(Collections.emptyList());
        }

        @Override
        protected void onStart() {
            request(BATCH_SIZE);
        }

        @Override
        public void onNext(Buyer buyer) {
            try {
                UUID buyerId = buyer.person().getId();
                Set<String> categories = buyer.basket().stream()
                        .map(item -> item.category().toString())
                        .collect(Collectors.toSet());
                buyerCategories.merge(buyerId, categories, (existingSet, newSet) -> {
                    existingSet.addAll(newSet);
                    return existingSet;
                });

                request(1);

            } catch (Exception e) {
                onError(e);
            }
        }

        @Override
        public void onError(Throwable t) {
            System.err.println("Error processing buyers: " + t.getMessage());
            processFinalResults();
            latch.countDown();
        }

        @Override
        public void onComplete() {
            System.out.println("Completed processing all buyers");
            processFinalResults();
            latch.countDown();
        }

        private void processFinalResults() {
            List<Pair<UUID, Integer>> results = buyerCategories.entrySet()
                    .stream()
                    .map(entry -> new Pair<>(entry.getKey(), entry.getValue().size()))
                    .sorted(Comparator.comparing(Pair<UUID, Integer>::value).reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            topBuyers.set(results);
        }

        public List<Pair<UUID, Integer>> getTopBuyers() {
            return topBuyers.get();
        }
    }
}