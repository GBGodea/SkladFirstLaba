package Collector.utils;

public class Triplet<T, V, R> {
    private T age;
    private V name;
    private R surname;

    public Triplet(T age, V name, R surname) {
        this.age = age;
        this.name = name;
        this.surname = surname;
    }

    public T getAge() {
        return age;
    }

    public V getName() {
        return name;
    }

    public R getSurname() {
        return surname;
    }
}
