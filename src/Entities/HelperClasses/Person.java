package Entities.HelperClasses;

import java.time.LocalDateTime;
import java.util.UUID;

public class Person {
    protected UUID id;
    protected String name;
    protected String surname;
    protected int age;
    protected Passport passport;
    protected LocalDateTime entryDate;
    protected LocalDateTime releaseDate;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public Passport getPassport() {
        return passport;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }
}
