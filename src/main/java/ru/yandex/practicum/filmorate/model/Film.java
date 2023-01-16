package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@RequiredArgsConstructor
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private Integer rate;
    private Set<Genre> genres = new TreeSet<>();
    private Mpa mpa = new Mpa();
    private Set<Integer> personsLikedFilm = new HashSet<>();

    public void setReleaseDateFromString (String releaseDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        this.releaseDate = LocalDate.parse(releaseDate, formatter);
    }

    public String getReleaseDateAsString () {
        return this.releaseDate.toString();
    }
}
