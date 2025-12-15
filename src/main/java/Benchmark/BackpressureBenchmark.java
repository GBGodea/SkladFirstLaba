package Benchmark;

import Entities.Buyer;
import Entities.Items;
import Generator.RandomBuyerGenerator;
import Generator.RandomItemsGenerator;
import Statistic.utils.CustomAdaptiveSubscriber;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1)
@Threads(3)
public class BackpressureBenchmark {

    private List<Buyer> buyers;

    @Param({ "10", "100", "1000" })
    private int bufferSize;

    @Param({ "5", "50", "100" })
    private long threshold;

    @Param({ "100000", "500000" })
    private int size;

    @Setup(Level.Trial)
    public void setUp() {
        System.out.println("\n📊 setUp: bufferSize=" + bufferSize + ", threshold=" + threshold +
                ", size=" + size);
        List<Items> items = new RandomItemsGenerator().generator(size, size);
        buyers = new RandomBuyerGenerator(items).generator(size, size);
    }

    @Benchmark
    public void backpressureTest(Blackhole blackhole) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger count = new AtomicInteger(0);

        Flowable.fromIterable(buyers)
                .subscribeOn(Schedulers.io())
                .subscribe(new CustomAdaptiveSubscriber<>(
                        bufferSize,
                        threshold,
                        buyer -> buyer.person().getAge(),
                        age -> {
                            count.incrementAndGet();
                            if (count.get() >= buyers.size()) {
                                latch.countDown();
                            }
                        }
                ));

        boolean completed = latch.await(120, TimeUnit.SECONDS);
        blackhole.consume(completed);
        blackhole.consume(count.get());
    }
}
