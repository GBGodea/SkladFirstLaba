package Entities;

import Entities.HelperClasses.Person;

import java.util.List;

public record Buyer(Person person, List<Items> basket) { }
