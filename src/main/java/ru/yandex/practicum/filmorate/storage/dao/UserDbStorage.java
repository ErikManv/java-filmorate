package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.rowMappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private static final String SQL_ADD_USER = "INSERT INTO USER_TABLE(NAME, LOGIN, EMAIL, BIRTHDAY) " +
            "VALUES (?,?,?,?)";

    private static final String SQL_UPDATE_USER = "UPDATE USER_TABLE SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE ID = ?";

    private static final String SQL_GET_USER = "SELECT * FROM USER_TABLE WHERE ID=?";

    private static final String SQL_GET_ALL_USERS = "SELECT * FROM USER_TABLE";

    private static final String SQL_ADD_FRIEND = "INSERT INTO FRIENDS_TABLE (USER_ID, ANOTHER_USER_ID) VALUES (?,?)";

    private static final String SQL_DELETE_FRIEND = "DELETE FROM FRIENDS_TABLE WHERE USER_ID = ? AND ANOTHER_USER_ID=?";

    private static final String SQL_GET_ALL_FRIENDS_OF_USER = "SELECT * FROM FRIENDS_TABLE " +
            "LEFT JOIN USER_TABLE " +
            "ON FRIENDS_TABLE.ANOTHER_USER_ID = USER_TABLE.ID " +
            "WHERE FRIENDS_TABLE.USER_ID=?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SQL_ADD_USER, new String[]{"ID"});
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getBirthday().toString());
            return preparedStatement;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }
    @Override
    public User updateUser(User user) {
            jdbcTemplate.update(SQL_UPDATE_USER,
                    user.getName(), user.getLogin(), user.getEmail(), user.getBirthdayAsString(), user.getId());
            return user;
    }

    @Override
    public boolean containsUser(Integer id) {
        try {
            jdbcTemplate.queryForObject(SQL_GET_USER, new Object[]{id},
                    new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<User> findAll() {
        List<Map<String, Object>> listOfMaps = jdbcTemplate.queryForList(SQL_GET_ALL_USERS);
        List<User> listOfUsers = new ArrayList<>();
        for(Map<String, Object> map: listOfMaps) {
            User user = new User();
            user.setId((Integer) map.get("ID"));
            user.setName((String) map.get("NAME"));
            user.setLogin((String) map.get("LOGIN"));
            user.setEmail((String) map.get("EMAIL"));
            user.setBirthdayFromString((String) map.get("BIRTHDAY"));
            listOfUsers.add(user);
        }
        return listOfUsers;
    }

    @Override
    public User getUser(Integer id) {
        return jdbcTemplate.queryForObject(SQL_GET_USER, new Object[]{id}, new UserRowMapper());
    }

    @Override
    public void putFriend(Integer userId, Integer friendId) {
        jdbcTemplate.update(SQL_ADD_FRIEND, userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        jdbcTemplate.update(SQL_DELETE_FRIEND, userId, friendId);
    }

    @Override
    public List<User> getFriendsList(Integer userId) {
        List<Map<String, Object>> allFriendsOfUser = jdbcTemplate
                .queryForList(SQL_GET_ALL_FRIENDS_OF_USER, userId);
        return UserRowMapper.userMapper(allFriendsOfUser);
    }

    @Override
    public List<User> commonFriends(Integer userId, Integer anotherId) {
        List<Map<String, Object>> allFriendsOfFirstUser = jdbcTemplate
                .queryForList(SQL_GET_ALL_FRIENDS_OF_USER, userId);

        List<Map<String, Object>> allFriendsOfSecondUser = jdbcTemplate
                .queryForList(SQL_GET_ALL_FRIENDS_OF_USER, anotherId);

        List<User> allFriendsOfFirstUserAsList = UserRowMapper.userMapper(allFriendsOfFirstUser);
        List<User> allFriendsOfSecondUserAsList = UserRowMapper.userMapper(allFriendsOfSecondUser);

        return allFriendsOfFirstUserAsList
                .stream()
                .filter(allFriendsOfSecondUserAsList::contains)
                .collect(Collectors.toList());
    }
}