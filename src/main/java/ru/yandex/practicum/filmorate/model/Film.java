package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@RequiredArgsConstructor
public class Film {
    private String id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
}
