package Entities;

import Entities.HelperClasses.JobPosition;
import Entities.HelperClasses.Person;

public record Employee(Person person, double salary, JobPosition jobPosition) { }
