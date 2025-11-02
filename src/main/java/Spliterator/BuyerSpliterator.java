package Spliterator;

import Entities.Buyer;
import Entities.Employee;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class BuyerSpliterator implements Spliterator<Buyer> {
    private final List<Buyer> list;
    private int current;
    private final int end;
    private static final int THRESHOLD = 100;

    public BuyerSpliterator(List<Buyer> list) {
        this(list, 0, list.size());
    }

    public BuyerSpliterator(List<Buyer> list, int start, int end) {
        this.list = list;
        this.current = start;
        this.end = end;
    }

    @Override

    public boolean tryAdvance(Consumer<? super Buyer> action) {
        if(current < end) {
            action.accept(list.get(current++));
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Buyer> trySplit() {
        int size = end - current;
        if(size <= THRESHOLD) {
            return null;
        }
        int mid = current + size / 2;
        Spliterator<Buyer> spliterator = new BuyerSpliterator(list, current, mid);
        current = mid;
        return spliterator;
    }

    @Override
    public void forEachRemaining(Consumer<? super Buyer> action) {
        for(int i = current; i < end; i++) {
            action.accept(list.get(i));
        }
        current = end;
    }

    @Override
    public long estimateSize() {
        return end - current;
    }

    @Override
    public int characteristics() {
        return ORDERED | SIZED | SUBSIZED;
    }
}
