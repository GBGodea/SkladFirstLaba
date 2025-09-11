package Entities.HelperClasses;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

/*
    TODO
    Сделать Базовый класс человека, от которого будут наследоваться пользователи склада
     и работники склада
     id, имя, возраст, паспортнгые данные, дата прихода и ухода и т.д.
 */
public class Person {
    public UUID id;
    public String name;
    public String surname;
    public int age;
    public Passport passport;
    public LocalDateTime entryDate;
    public LocalDateTime releaseDate;
}
