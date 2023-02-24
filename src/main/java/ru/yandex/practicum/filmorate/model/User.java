package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class User {
    private Integer id;
    private String login;
    private String email;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friendsList = new HashSet<>();

    public void setBirthdayFromString (String birthday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        this.birthday = LocalDate.parse(birthday, formatter);
    }

    public String getBirthdayAsString () {
        return this.birthday.toString();
    }
}
