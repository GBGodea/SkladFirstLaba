package Generator;

import Entities.Employee;
import Entities.HelperClasses.JobPosition;
import Entities.HelperClasses.Passport;
import Generator.helper.FileReaderClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomEmployeeGenerator implements Generator<Employee> {
    @Override
    public List<Employee> generator() {
        Random rand = new Random();
        List<Employee> employees = new ArrayList<>();
        List<String> nameList = FileReaderClass.readNameList();
        List<String> surnameList = FileReaderClass.readSurnameList();
        JobPosition[] jobPosition = JobPosition.values();

        for(int i = 0; i < rand.nextInt(100_000); i++) {
            LocalDateTime[] times = TimeGenerator.between();


            employees.add(
                    new Employee.Builder()
                            .id(UUID.randomUUID())
                            .name(nameList.get(nameList.size() - 1))
                            .surname(surnameList.get(nameList.size() - 1))
                            .age(rand.nextInt(18, 100))
                            .passport(new Passport(rand.nextInt(1000, 9999), rand.nextInt(100_000, 999_999)))
                            .entryDate(times[0])
                            .releaseDate(times[1])
                            .salary(rand.nextInt(25_000, 1_000_000))
                            .jobPosition(jobPosition[rand.nextInt(jobPosition.length - 1)])
                            .build()

            );
        }
        return employees;
    }
}
