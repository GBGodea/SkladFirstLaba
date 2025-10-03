package Statistic;

import Collector.MyCollector;
import Collector.utils.Triplet;
import Entities.Buyer;
import Entities.Employee;
import Entities.HelperClasses.Category;
import Entities.HelperClasses.JobPosition;
import Entities.HelperClasses.Person;
import Entities.Items;
import Statistic.utils.Pair;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
        for (int i = 0; i < employees.size(); i++) {
            sum += employees.get(i).person().getAge();
        }
        return sum / employees.size();
    }

    public int averageBuyerAge() {
        return (int) buyers.stream().mapToInt(buyers -> buyers.person().getAge()).average().orElse(0);
    }

    public int averagePersonAge() {
        return (int) persons.stream().mapToInt(Triplet::getAge).average().orElse(0);
    }

    // Самое часто встречаемое имя
    public Pair<String, Integer> mostFrequentEmployeeName() {
        Map<String, Integer> map = new HashMap<>();

        Pair<String, Integer> frequent = new Pair<>("", 0);
        for (Employee employee : employees) {
            String name = employee.person().getName();
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
                .collect(Collectors.groupingBy(buyer -> buyer.person().getName(), Collectors.counting()));

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
        Map<JobPosition, Long> map = employees.stream().collect(Collectors.groupingBy(Employee::jobPosition, Collectors.counting()));

        long max = map.entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue).orElse(0L);

        Optional<JobPosition> result = map.entrySet().stream().filter(e -> e.getValue().equals(max))
                .map(Map.Entry::getKey).findFirst();

        return result.map(jobPosition -> new Pair<>(jobPosition.name(), (int) max)).orElse(null);
    }

    // Категория наиболее часто выбираемого товара в корзине
    public Pair<String, Integer> mostFrequentItemInPersonBusket() {
        return buyers.stream().flatMap(buyer -> buyer.basket().stream())
                .collect(Collectors.groupingBy(item -> item.category().toString(),
                        Collectors.reducing(0, e -> 1, Integer::sum)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .orElse(null);
    }

    // Самое большое количество товара на складе
    public Pair<String, Integer> biggestCountOfItem() {
        return items.stream()
                .max(Comparator.comparingInt(Items::count))
                .map(item -> new Pair<>(item.name(), item.count()))
                .orElse(null);
    }

    // Анализ ценности покупателей(Высчитывает суммарную покупательскую способность пользователя по корзине с товарами)
    public Map<UUID, BigDecimal> valueAnalysis() {
        return buyers.stream()
                .collect(Collectors.toMap(
                        b -> b.person().getId(),
                        b -> b.basket().stream()
                                .map(item -> item.price().multiply(BigDecimal.valueOf(item.count())))
                                .reduce(BigDecimal.ZERO, BigDecimal::add),
                        BigDecimal::add
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<UUID, BigDecimal>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        )
                );
    }

    // Топ людей по разнообразию покупок
    public List<Pair<UUID, Integer>> topByCategoryDiversity() {
        Map<UUID, Set<Category>> diversity = buyers.stream()
                .collect(Collectors.toMap(
                        b -> b.person().getId(),
                        b -> b.basket().stream()
                                .map(Items::category)
                                .collect(Collectors.toSet()),
                        (s1, s2) -> { s1.addAll(s2); return s1; }
                ));

        List<Pair<UUID, Integer>> list = diversity.entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue().size())).toList();

        return list.stream()
                .sorted(Comparator.comparing(Pair<UUID, Integer>::value).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // Топ работников по соотношению возраста к зарплате
    public List<Pair<UUID, Double>> topByAgeSalaryRatio() {
        return employees.stream()
                .map(e -> {
                    double age = e.person().getAge();
                    double salary = e.salary();
                    double ratio = salary == 0 ? Double.MAX_VALUE : age / salary;
                    return new Pair<>(e.person().getId(), ratio);
                })
                .sorted(Comparator.comparing(Pair<UUID, Double>::value))
                .limit(10)
                .collect(Collectors.toList());
    }

    // Вычисление статической дисперсии цен всех товаров в корзине
    public List<Pair<UUID, Double>> topByPriceVariance() {
        return buyers.stream()
                .map(b -> {
                    List<Double> prices = b.basket().stream()
                            .flatMap(item ->
                                    Collections.nCopies(item.count(), item.price().doubleValue()).stream()
                            ).toList();

                    double mean = prices.stream()
                            .mapToDouble(Double::doubleValue)
                            .average()
                            .orElse(0.0);

                    double variance = prices.stream()
                            .mapToDouble(p -> (p - mean) * (p - mean))
                            .average()
                            .orElse(0.0);

                    return new Pair<>(b.person().getId(), variance);
                })
                .sorted(Comparator.comparing(Pair<UUID, Double>::value).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
