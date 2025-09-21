package Statistic;

import Collector.MyCollector;
import Collector.utils.Triplet;
import Entities.Buyer;
import Entities.Employee;
import Entities.HelperClasses.JobPosition;
import Entities.HelperClasses.Person;
import Entities.Items;
import Statistic.utils.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statistic {
    //TODO данные list нужно именно заполнить здесь один раз для экономии памяти и Runtime
    List<Buyer> buyers;
    List<Employee> employees;
    List<Triplet<Integer, String, String>> persons;
    List<Items> items;
    public Statistic(List<Buyer> buyers,
                     List<Employee> employees,
                     List<Person> persons,
                     List<Items> items) {
        this.buyers = buyers;
        this.employees = employees;
        this.persons = persons.stream().collect(MyCollector.toMyCollector());
        this.items = items;
    }

    // Среднее по возрасту
    public int averageEmployeeAge() {
        int sum = 0;
        for(int i = 0; i < employees.size(); i++) {
            sum += employees.get(i).getAge();
        }
        return sum / employees.size();
    }

    public int averageBuyerAge() {
        return (int) buyers.stream().mapToInt(Person::getAge).average().orElse(0);
    }

    public int averagePersonAge() {
        return (int) persons.stream().mapToInt(Triplet::getAge).average().orElse(0);
    }

    // Самое часто встречаемое имя
    public Pair<String, Integer> mostFrequentEmployeeName() {
        Map<String, Integer> map = new HashMap<>();

        Pair<String, Integer> frequent = new Pair<>("", 0);
        for (Employee employee : employees) {
            String name = employee.getName();
            if (map.containsKey(name)) {
                map.put(name, map.getOrDefault(name, 0) + 1);
            } else {
                map.put(name, 1);
            }

            if (frequent.value() < map.get(name)) {
                frequent = new Pair<>(name, map.get(name));
            }
        }
        return frequent;
    }

    public Pair<String, Integer> mostFrequentBuyerName() {
        Map<String, Long> map = buyers.stream()
                .collect(Collectors.groupingBy(Buyer::getName, Collectors.counting()));

        long max = map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue)
                .orElse(0L);

        Optional<String> result = map.entrySet().stream()
                .filter(e -> e.getValue().equals(max))
                .map(Map.Entry::getKey)
                .findFirst();

        return result.map(name -> new Pair<>(name, (int) max)).orElse(null);
    }

    public Pair<String, Integer> mostFrequentPersonName() {
        Map<String, Long> map = persons.stream()
                .collect(Collectors.groupingBy(Triplet::getName, Collectors.counting()));

        long max = map.values().stream()
                .max(Long::compareTo)
                .orElse(0L);

        Optional<String> result = map.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .findFirst();

        return result.map(name -> new Pair<>(name, (int) max)).orElse(null);
    }


    // Самая часто встречаемая профессия
    public Pair<String, Integer> mostFrequentJobPosition() {
        Map<JobPosition, Long> map = employees.stream().collect(Collectors.groupingBy(Employee::getJobPosition, Collectors.counting()));

        long max = map.entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue).orElse(0L);

        Optional<JobPosition> result = map.entrySet().stream().filter(e -> e.getValue().equals(max))
                .map(Map.Entry::getKey).findFirst();

        return result.map(jobPosition -> new Pair<>(jobPosition.name(), (int)max)).orElse(null);
    }

    // Категория наиболее часто выбираемого товара в корзине
    public Pair<String, Integer> mostFrequentItemInPersonBusket() {
        return buyers.stream().flatMap(buyer -> buyer.getBasket().stream())
                .collect(Collectors.groupingBy(item -> item.getCategory(),
                        Collectors.reducing(0, e -> 1, Integer::sum)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    // Самое большое количество товара на складе
    public Pair<String, Integer> biggestCountOfItem() {
        return items.stream()
                .max(Comparator.comparingInt(Items::getCount))
                .map(item -> new Pair<>(item.getName(), item.getCount()))
                .orElse(null);
    }
}
