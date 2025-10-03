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

    public Person(UUID id,
                  String name,
                  String surname,
                  int age,
                  Passport passport,
                  LocalDateTime entryDate,
                  LocalDateTime releaseDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.passport = passport;
        this.entryDate = entryDate;
        this.releaseDate = releaseDate;
    }

    public UUID getId() {
        return id;
    }

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
