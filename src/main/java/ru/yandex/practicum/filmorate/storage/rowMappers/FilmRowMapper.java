package ru.yandex.practicum.filmorate.storage.rowMappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int i) throws SQLException {
        Film film = new Film();

        film.setId(resultSet.getInt("ID"));
        film.setName(resultSet.getString("NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setReleaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(resultSet.getLong("DURATION"));
        film.setRate(resultSet.getInt("RATE"));
        return film;
    }
}

