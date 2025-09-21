package Entities;

import Entities.HelperClasses.JobPosition;
import Entities.HelperClasses.Passport;
import Entities.HelperClasses.Person;

import java.time.LocalDateTime;
import java.util.UUID;

public class Employee extends Person {
    protected double salary;
    protected JobPosition jobPosition;

    public Employee(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.surname = builder.surname;
        this.age = builder.age;
        this.passport = builder.passport;
        this.entryDate = builder.entryDate;
        this.releaseDate = builder.releaseDate;
        this.salary = builder.salary;
        this.jobPosition = builder.jobPosition;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String surname;
        private int age;
        private Passport passport;
        private LocalDateTime entryDate;
        private LocalDateTime releaseDate;
        private double salary;
        private JobPosition jobPosition;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }

        public Builder passport(Passport passport) {
            this.passport = passport;
            return this;
        }

        public Builder entryDate(LocalDateTime entryDate) {
            this.entryDate = entryDate;
            return this;
        }

        public Builder releaseDate(LocalDateTime releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder salary(double salary) {
            this.salary = salary;
            return this;
        }

        public Builder jobPosition(JobPosition jobPosition) {
            this.jobPosition = jobPosition;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    public JobPosition getJobPosition() {
        return jobPosition;
    }

    public double getSalary() {
        return salary;
    }
}
