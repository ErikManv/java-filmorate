package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class User {
    int id;
    String login;
    String email;
    String name;
    LocalDate birthday;

}
