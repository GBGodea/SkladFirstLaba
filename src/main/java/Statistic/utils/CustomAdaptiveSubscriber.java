package Statistic.utils;

import io.reactivex.rxjava3.subscribers.DefaultSubscriber;

import java.util.function.Consumer;
import java.util.function.Function;

public class CustomAdaptiveSubscriber<T, R> extends DefaultSubscriber<T> {
    private final int maxBufferSize;
    private final long processingThreeshold;
    private final Function<T, R> processor;
    private final Consumer<R> resultHandler;
    private int bufferCount;

    public CustomAdaptiveSubscriber(int maxBufferSize,
                                    long processingThreshold,
                                    Function<T, R> processor,
                                    Consumer<R> resultHandler) {
        this.maxBufferSize = maxBufferSize;
        this.processingThreeshold = processingThreshold;
        this.processor = processor;
        this.resultHandler = resultHandler;
    }

    @Override
    public void onStart() {
        request(maxBufferSize);
    }

    @Override
    public void onNext(T t) {
        long startTime = System.currentTimeMillis();

        R result = processor.apply(t);
        resultHandler.accept(result);

        long processingTime = System.currentTimeMillis() - startTime;
        bufferCount++;

        if(bufferCount >= maxBufferSize) {
            int nextBatchSize = processingTime < processingThreeshold ?
                    maxBufferSize * 2 : Math.max(1, maxBufferSize / 2);
            request(nextBatchSize);
            bufferCount = 0;
        }
    }

    @Override
    public void onError(Throwable t) {
        System.err.println("Ошибка обработки: " +  t.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Обработка завершена");
    }
}
