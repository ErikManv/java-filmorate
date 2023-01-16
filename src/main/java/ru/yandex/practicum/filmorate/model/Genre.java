package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;

@Data
public class Genre implements Comparable<Genre>{
    private Integer id;
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(Genre o) {

        return this.id - o.getId();
    }
}
