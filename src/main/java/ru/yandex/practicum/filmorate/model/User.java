package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class User {
    private int id;
    private final String login;
    private final String email;
    private String name;
    private final LocalDate birthday;

}
