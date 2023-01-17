package ru.yandex.practicum.filmorate.storage.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.rowMappers.FilmRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private static final String SQL_ADD_FILM = "INSERT INTO FILM_TABLE" +
            "(NAME, DESCRIPTION, RELEASE_DATE, MPA, DURATION, RATE) VALUES (?,?,?,?,?,?)";

    private static final String SQL_GET_FILM = "SELECT * FROM FILM_TABLE LEFT JOIN MPA_TABLE " +
            "ON FILM_TABLE.MPA = MPA_TABLE.MPA_ID WHERE FILM_TABLE.ID=?";

    private static final String SQL_ADD_GENRES_OF_FILM = "INSERT INTO GENRE_FILM_TABLE(FILM_ID, GENRE_ID) VALUES (?,?) ";

    private static final String SQL_CHECK_FILM_IF_EXISTS = "SELECT * FROM FILM_TABLE WHERE ID=? ";

    private static final String SQL_GET_ALL_FILMS = "SELECT * FROM FILM_TABLE LEFT JOIN MPA_TABLE " +
            "ON FILM_TABLE.MPA = MPA_TABLE.MPA_ID";

    private static final String SQL_GET_GENRES_OF_FILM = "SELECT * FROM GENRE_FILM_TABLE LEFT JOIN " +
            "GENRE_TABLE ON GENRE_FILM_TABLE.GENRE_ID = GENRE_TABLE.ID WHERE GENRE_FILM_TABLE.FILM_ID=? ";

    private static final String SQL_UPDATE_FILM = "UPDATE FILM_TABLE SET NAME=?, DESCRIPTION=?," +
            "DURATION=?, RELEASE_DATE=?, MPA=? WHERE ID=?";

    private static final String SQL_DELETE_GENRES_OF_FILM = "DELETE  FROM GENRE_FILM_TABLE WHERE FILM_ID=? ";

    private static final String SQL_PUT_LIKE = "INSERT INTO LIKE_USER_TABLE (FILM_ID, USER_ID)" +
            "VALUES (?,?)";

    private static final String SQL_UPDATE_RATE_OF_FILM = "UPDATE FILM_TABLE SET RATE=?";

    private static final String SQL_GET_RATE_OF_FILM = "SELECT COUNT(FILM_ID) " +
            "FROM LIKE_USER_TABLE WHERE FILM_ID=";

    private static final String SQL_DELETE_LIKE = "DELETE FROM LIKE_USER_TABLE WHERE FILM_ID=? AND " +
            "USER_ID=?";

    private static final String SQL_GET_ALL_FILMS_ORDERED_BY_RATE = "SELECT * FROM FILM_TABLE LEFT JOIN MPA_TABLE " +
            "ON FILM_TABLE.MPA = MPA_TABLE.MPA_ID ORDER BY RATE DESC";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SQL_ADD_FILM, new String[]{"ID"});
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setString(3, film.getReleaseDate().toString());
            preparedStatement.setInt(4, film.getMpa().getId());
            preparedStatement.setLong(5, film.getDuration());
            if(film.getRate() != null) {
                preparedStatement.setInt(6, film.getRate());
            } else {
                preparedStatement.setInt(6, 0);
            }
            return preparedStatement;
        }, keyHolder);
        Integer filmId = keyHolder.getKey().intValue();
        film.setId(filmId);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres())
                jdbcTemplate.update(SQL_ADD_GENRES_OF_FILM, filmId, genre.getId());
        }
        return film;
    }

    @Override
    public boolean containsFilm(Integer filmId) {
        try {
            jdbcTemplate.queryForObject(SQL_CHECK_FILM_IF_EXISTS, new Object[]{filmId},
                    new FilmRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Film> findAll() {
        List<Map<String, Object>> films = jdbcTemplate.queryForList(SQL_GET_ALL_FILMS);

        List<Film> listOfFilms = new ArrayList<>();

        for (Map<String, Object> map : films) {
            Film film = new Film();
            film.setId((Integer) map.get("ID"));
            film.setName((String) map.get("NAME"));
            film.setDescription((String) map.get("DESCRIPTION"));
            film.setReleaseDateFromString((String) map.get("RELEASE_DATE"));
            if(map.containsKey("MPA_NAME")) {
                film.getMpa().setName((String) map.get("MPA_NAME"));
            }
            if(map.containsKey("MPA_ID")) {
                film.getMpa().setId((Integer) map.get("MPA_ID"));
            }
            film.setDuration((Long)map.get("DURATION"));
            film.setRate((Integer)map.get("RATE"));

            List<Map<String, Object>> genresOfFilm = jdbcTemplate.queryForList(SQL_GET_GENRES_OF_FILM, map.get("ID"));

            for (Map<String, Object> genreMap : genresOfFilm) {
                System.out.println(genreMap.keySet());
                System.out.println(genreMap.values());

                int genreId = (Integer) genreMap.get("ID");
                String genreName = (String) genreMap.get("NAME");
                Genre genre = new Genre(genreId, genreName);
                film.getGenres().add(genre);
            }
            listOfFilms.add(film);
        }
        return listOfFilms;
    }

    @Override
    public Film getFilm(Integer id) {

        List<Map<String, Object>> films = jdbcTemplate.queryForList(SQL_GET_FILM, id);
        List<Map<String, Object>> genresOfFilm = jdbcTemplate.queryForList(SQL_GET_GENRES_OF_FILM, id);
        return mapsToList(films, genresOfFilm).get(0);
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            jdbcTemplate.update(SQL_UPDATE_FILM,
                    film.getName(),
                    film.getDescription(),
                    film.getDuration(),
                    film.getReleaseDate(),
                    film.getMpa().getId(),
                    film.getId());
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
        jdbcTemplate.update(SQL_DELETE_GENRES_OF_FILM, film.getId());
        NavigableSet<Genre> genres = film.getGenres(); // это сделано потому-что POSTMAN выдавал ошибку
        for (Genre genre : genres.descendingSet()) {
            try {
                jdbcTemplate.update(SQL_ADD_GENRES_OF_FILM, film.getId(), genre.getId());
            } catch (DataIntegrityViolationException e) {

            }
        }
        return film;
    }

    @Override
    public void putLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(SQL_PUT_LIKE, filmId, userId);
        int rate = jdbcTemplate.queryForObject(SQL_GET_RATE_OF_FILM + filmId, Integer.class);
        jdbcTemplate.update(SQL_UPDATE_RATE_OF_FILM, rate);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(SQL_DELETE_LIKE, filmId, userId);
        int rate = jdbcTemplate.queryForObject(SQL_GET_RATE_OF_FILM + filmId, Integer.class);
        jdbcTemplate.update(SQL_UPDATE_RATE_OF_FILM, rate);

    }
    @Override
    public List<Film> topFilms(Integer count) {
        List<Map<String, Object>> films = jdbcTemplate.queryForList(SQL_GET_ALL_FILMS_ORDERED_BY_RATE);

        List<Film> listOfFilms = new ArrayList<>();

        for (Map<String, Object> map : films) {
            Film film = new Film();
            film.setId((Integer) map.get("ID"));
            film.setName((String) map.get("NAME"));
            film.setDescription((String) map.get("DESCRIPTION"));
            film.setReleaseDateFromString((String) map.get("RELEASE_DATE"));
            if(map.containsKey("MPA_NAME")) {
                film.getMpa().setName((String) map.get("MPA_NAME"));
            }
            if(map.containsKey("MPA_ID")) {
                film.getMpa().setId((Integer) map.get("MPA_ID"));
            }
            film.setDuration((Long)map.get("DURATION"));
            film.setRate((Integer)map.get("RATE"));

            List<Map<String, Object>> genresOfFilm = jdbcTemplate.queryForList(SQL_GET_GENRES_OF_FILM, map.get("ID"));

            for (Map<String, Object> genreMap : genresOfFilm) {
                System.out.println(genreMap.keySet());
                System.out.println(genreMap.values());

                int genreId = (Integer) genreMap.get("ID");
                String genreName = (String) genreMap.get("NAME");
                Genre genre = new Genre(genreId, genreName);
                film.getGenres().add(genre);
            }
            listOfFilms.add(film);
        }

        Collections.reverse(listOfFilms);
        return listOfFilms
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    private List<Film> mapsToList (List<Map<String, Object>> films, List<Map<String, Object>> genresOfFilm) {    /*convert maps of films and genres
                                                                                                                 to list of films*/
        List<Film> listOfFilms = new ArrayList<>();

        for (Map<String, Object> map : films) {
            Film film = new Film();
            film.setId((Integer) map.get("ID"));
            film.setName((String) map.get("NAME"));
            film.setDescription((String) map.get("DESCRIPTION"));
            film.setReleaseDateFromString((String) map.get("RELEASE_DATE"));
            if (map.containsKey("MPA_NAME")) {
                film.getMpa().setName((String) map.get("MPA_NAME"));
            }
            if (map.containsKey("MPA_ID")) {
                film.getMpa().setId((Integer) map.get("MPA_ID"));
            }
            film.setDuration((Long) map.get("DURATION"));
            film.setRate((Integer) map.get("RATE"));

            for (Map<String, Object> genreMap : genresOfFilm) {
                int genreId = (Integer) genreMap.get("ID");
                String genreName = (String) genreMap.get("NAME");
                Genre genre = new Genre(genreId, genreName);
                film.getGenres().add(genre);
            }
            listOfFilms.add(film);
        }
        return listOfFilms;
    }
}