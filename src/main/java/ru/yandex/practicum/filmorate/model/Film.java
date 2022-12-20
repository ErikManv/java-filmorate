package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Data
@RequiredArgsConstructor
public class Film {
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private int rate;
}
