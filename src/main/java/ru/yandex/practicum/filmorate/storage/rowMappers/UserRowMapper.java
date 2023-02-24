package ru.yandex.practicum.filmorate.storage.rowMappers;

import org.springframework.jdbc.core.RowMapper;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("ID"));
        user.setName(resultSet.getString("NAME"));
        user.setLogin(resultSet.getString("LOGIN"));
        user.setEmail(resultSet.getString("EMAIL"));
        user.setBirthday(resultSet.getDate("BIRTHDAY").toLocalDate());
        return user;
    }

    public static List<User> userMapper(List<Map<String, Object>> listOfMaps) {
        List<User> list = new ArrayList<>();

        for (Map<String, Object> map : listOfMaps) {
            User user = new User();
            user.setId((Integer) map.get("ID"));
            user.setName((String) map.get("NAME"));
            user.setLogin((String) map.get("LOGIN"));
            user.setEmail((String) map.get("EMAIL"));
            user.setBirthdayFromString((String) map.get("BIRTHDAY"));
            list.add(user);
        }
        return list;
    }
}