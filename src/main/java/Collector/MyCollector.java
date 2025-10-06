package Collector;

import Collector.utils.Triplet;
import Entities.HelperClasses.Person;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class MyCollector implements Collector<Person, List<Triplet<Integer, String, String>>, List<Triplet<Integer, String, String>>> {
    public static MyCollector toMyCollector() {
        return new MyCollector();
    }

    @Override
    public Supplier<List<Triplet<Integer, String, String>>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Triplet<Integer, String, String>>, Person> accumulator() {
        return (list, person) ->
            list.add(new Triplet<>(person.getAge(), person.getName(), person.getSurname()));
    }

    @Override
    public BinaryOperator<List<Triplet<Integer, String, String>>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<Triplet<Integer, String, String>>, List<Triplet<Integer, String, String>>> finisher() {
        return Collections::unmodifiableList;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}
