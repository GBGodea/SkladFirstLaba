package Spliterator;

import Entities.Employee;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class EmployeeSpliterator implements Spliterator<Employee> {
    private final List<Employee> list;
    private int current;
    private final int end;
    private static final int THRESHOLD = 10_000;

    public EmployeeSpliterator(List<Employee> list) {
        this(list, 0, list.size());
    }

    public EmployeeSpliterator(List<Employee> list, int start, int end) {
        this.list = list;
        this.current = start;
        this.end = end;
    }

    @Override

    public boolean tryAdvance(Consumer<? super Employee> action) {
        if(current < end) {
            action.accept(list.get(current++));
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Employee> trySplit() {
        int size = end - current;
        if(size <= THRESHOLD) {
            return null;
        }
        int mid = current + size / 2;
        Spliterator<Employee> spliterator = new EmployeeSpliterator(list, current, mid);
        current = mid;
        return spliterator;
    }

    @Override
    public long estimateSize() {
        return end - current;
    }

    @Override
    public int characteristics() {
        return ORDERED | SIZED | SUBSIZED | IMMUTABLE;
    }
}
