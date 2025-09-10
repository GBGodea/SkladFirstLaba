package Entities;

import java.time.OffsetDateTime;
import java.util.UUID;

/*
    TODO
    Сделать Базовый класс человека, от которого будут наследоваться пользователи склада
     и работники склада
     id, имя, возраст, паспортнгые данные, дата прихода и ухода и т.д.
 */
public class Person {
    UUID id;
    String name;
    int age;
    Passport passport;
    OffsetDateTime entryDate;
    OffsetDateTime releaseDate;
}
