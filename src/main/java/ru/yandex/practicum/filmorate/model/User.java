package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class User {
    private Integer id;
    private final String login;
    private final String email;
    private String name;
    private final LocalDate birthday;
    private Set<Integer> friendsList = new HashSet<>();
}
