package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {


    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userStorage;

    private final FilmDbStorage filmStorage;

    private final MpaDao mpaDao;

    private final GenreDao genreDao;

    @Test
    public void createUserTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));

        Optional<User> userOptional = Optional.ofNullable(userStorage.createUser(user));

        assertThat(userOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void updateUserTest() {
        User user = new User();
        user.setId(1);
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        Optional<User> userOptional = Optional.ofNullable(user);
        User userUpdate = new User();
        userUpdate.setId(1);
        userUpdate.setEmail("garrys2machinima@gmail.com");
        userUpdate.setLogin("Login");
        userUpdate.setName("Name2");
        userUpdate.setBirthday(LocalDate.of(2001,2,8));

        userOptional = Optional.ofNullable(userStorage.updateUser(userUpdate));
        System.out.println(userOptional);
        Optional<User> finalUserOptional = userOptional;
        assertThat(userOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(finalUserOptional).hasValue(userUpdate));
    }
}