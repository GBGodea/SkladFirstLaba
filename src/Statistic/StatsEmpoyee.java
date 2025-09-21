package Statistic;

import Entities.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StatsEmpoyee {
    public static Map<String, Double> calculateIteration(List<Employee> employees) {
        Map<String, List<Double>> salaryByPosition = new HashMap<>();

        for (Employee employee : employees) {
            String position = employee.getJobPosition().toString();
            double salary = employee.getSalary();

            salaryByPosition.computeIfAbsent(position, k -> new ArrayList<>()).add(salary);
        }

        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : salaryByPosition.entrySet()) {
            double sum = 0;
            for (Double salary : entry.getValue()) {
                sum += salary;
            }
            result.put(entry.getKey(), sum / entry.getValue().size());
        }

        return result;
    }

    public static Map<String, Double> calculateStream(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        employee -> employee.getJobPosition().toString(),
                        Collectors.averagingDouble(Employee::getSalary)));
    }

    public static Map<String, Double> calculateCollector(List<Employee> employees) {
        return employees.stream()
                .collect(Collector.of(
                        HashMap::new,
                        (Map<String, SalaryStats> acc, Employee employee) -> {
                            String position = employee.getJobPosition().toString();
                            SalaryStats stats = acc.computeIfAbsent(position, k -> new SalaryStats());
                            stats.addSalary(employee.getSalary());
                        },
                        (Map<String, SalaryStats> map1, Map<String, SalaryStats> map2) -> {
                            map2.forEach((position, stats) -> map1.merge(position, stats, SalaryStats::merge));
                            return map1;
                        },
                        (Map<String, SalaryStats> acc) -> {
                            Map<String, Double> result = new HashMap<>();
                            acc.forEach((position, stats) -> result.put(position, stats.getAverage()));
                            return result;
                        }));
    }

    static class SalaryStats {
        private double sum = 0;
        private int count = 0;

        public void addSalary(double salary) {
            sum += salary;
            count++;
        }

        public double getAverage() {
            return count == 0 ? 0 : sum / count;
        }

        public SalaryStats merge(SalaryStats other) {
            this.sum += other.sum;
            this.count += other.count;
            return this;
        }
    }
}