package Generator;

import Entities.Employee;
import Entities.HelperClasses.JobPosition;
import Entities.HelperClasses.Passport;
import Entities.HelperClasses.Person;
import JsonReader.JsonReader;

import java.time.LocalDateTime;
import java.util.*;

public class RandomEmployeeGenerator implements Generator<Employee> {
    @Override
    public List<Employee> generator() {
        Random rand = new Random();
        List<Employee> employees = new ArrayList<>();
        Map<String, Map<String, List<String>>> namesMap = JsonReader.readNames();
        JobPosition[] jobPositions = JobPosition.values();

        int count = rand.nextInt(100, 100_000);
        for (int i = 0; i < count; i++) {
            String gender = rand.nextBoolean() ? "male" : "female";
            Map<String, List<String>> genderMap = namesMap.get(gender);

            List<String> firstNames = genderMap.get("firstNames");
            List<String> lastNames = genderMap.get("lastNames");

            String name = firstNames.get(rand.nextInt(firstNames.size()));
            String surname = lastNames.get(rand.nextInt(lastNames.size()));

            LocalDateTime[] times = TimeGenerator.between();

            Person person = new Person(
                    UUID.randomUUID(),
                    name,
                    surname,
                    rand.nextInt(18, 65),
                    new Passport(
                            rand.nextInt(1000, 9999),
                            rand.nextInt(100_000, 999_999)),
                    times[0],
                    times[1]
            );

            JobPosition jobPosition = jobPositions[rand.nextInt(jobPositions.length)];
            double salary = 0;
            switch (jobPosition) {
                case LOADER -> salary = rand.nextDouble(35_000, 45_000);
                case STOCKER -> salary = rand.nextDouble(30_000, 40_000);
                case ORDER_PICKER_PACKER -> salary = rand.nextDouble(33_000, 43_000);
                case FORKLIFT_OPERATOR -> salary = rand.nextDouble(40_000, 50_000);
                case MATERIAL_HANDLER -> salary = rand.nextDouble(37_000, 47_000);
                case QUALITY_CONTROL_INSPECTOR -> salary = rand.nextDouble(45_000, 55_000);
                case WAREHOUSE_SUPERVISOR -> salary = rand.nextDouble(60_000, 80_000);
                case WAREHOUSE_MANAGER -> salary = rand.nextDouble(80_000, 120_000);
                case LOGISTICS_MANAGER -> salary = rand.nextDouble(100_000, 150_000);
            }

            Employee employee = new Employee(person, salary, jobPosition);
            employees.add(employee);
        }
        return employees;
    }
}
