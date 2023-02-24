package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.rowMappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MpaDao {

    private static final String SQL_GET_MPA = "SELECT * FROM MPA_TABLE WHERE MPA_ID = ? ";
    private static final String SQL_GET_ALL_MPA = "SELECT * FROM MPA_TABLE";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAllMpa() {
        List<Map<String, Object>> listOfMapsOfMpa = jdbcTemplate.queryForList(SQL_GET_ALL_MPA);

        List<Mpa> listOfMpa = new ArrayList<>();

        for (Map<String, Object> map : listOfMapsOfMpa) {
            Mpa mpa = new Mpa();
            mpa.setId((Integer) map.get("MPA_ID"));
            mpa.setName((String) map.get("MPA_NAME"));

            listOfMpa.add(mpa);
        }
        listOfMpa = listOfMpa
                .stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toList());
        return listOfMpa;

    }

    public Mpa getMpa(int id) {
        Mpa mpa = jdbcTemplate.queryForObject(SQL_GET_MPA, new Object[]{id}, new MpaRowMapper());
        return mpa;
    }
}