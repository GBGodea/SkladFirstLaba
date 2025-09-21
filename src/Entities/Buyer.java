package Entities;

import Entities.HelperClasses.Passport;
import Entities.HelperClasses.Person;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Buyer extends Person {
    protected List<Items> basket;

    public Buyer(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.surname = builder.surname;
        this.age = builder.age;
        this.passport = builder.passport;
        this.entryDate = builder.entryDate;
        this.releaseDate = builder.releaseDate;
        this.basket = builder.basket;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String surname;
        private int age;
        private Passport passport;
        private LocalDateTime entryDate;
        private LocalDateTime releaseDate;
        private List<Items> basket;

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

        public Builder basket(List<Items> basket) {
            this.basket = basket;
            return this;
        }

        public Buyer build() {
            return new Buyer(this);
        }
    }

    public List<Items> getBasket() {
        return basket;
    }
}
