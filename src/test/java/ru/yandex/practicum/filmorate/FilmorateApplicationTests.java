package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext (classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateApplicationTests {

    private final UserDbStorage userStorage;

    private final FilmDbStorage filmStorage;

    private final MpaDao mpaDao;


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
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userStorage.createUser(user);

        User userUpdate = new User();
        userUpdate.setId(1);
        userUpdate.setEmail("garrys2machinima@gmail.com");
        userUpdate.setLogin("Login");
        userUpdate.setName("Name2");
        userUpdate.setBirthday(LocalDate.of(2001,2,8));

        Optional<User> userOptionalNew = Optional.ofNullable(userStorage.updateUser(userUpdate));

        assertThat(userOptionalNew)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(userStorage.getUser(1)).hasFieldOrPropertyWithValue("name", "Name2"));
    }

    @Test
    public void getUserTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userStorage.createUser(user);

        Optional<User> OpUser = Optional.ofNullable(user);
        assertThat(OpUser)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(userStorage.getUser(1)).hasFieldOrPropertyWithValue("email", "garrys2machinima@gmail.com"));
    }
    @Test
    public void findAllUsersTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userStorage.createUser(user);

        User user2 = new User();
        user2.setEmail("samsam@gmail.com");
        user2.setLogin("Login");
        user2.setName("Erni");
        user2.setBirthday(LocalDate.of(2001,2,8));
        userStorage.createUser(user2);

        assertEquals(2, userStorage.findAll().size());
    }

    @Test
    public void operationsWithFriendsTest() {
        User user2 = new User();
        user2.setEmail("samsam@gmail.com");
        user2.setLogin("Login");
        user2.setName("Erni");
        user2.setBirthday(LocalDate.of(2001,2,8));
        userStorage.createUser(user2);

        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userStorage.createUser(user);

        User user3 = new User();
        user3.setEmail("garrys2machinima@gmail.com");
        user3.setLogin("Login");
        user3.setName("Name");
        user3.setBirthday(LocalDate.of(2001,2,8));
        userStorage.createUser(user3);

        userStorage.putFriend(1, 2);
        assertEquals(1, userStorage.getFriendsList(1).size());

        userStorage.putFriend(3, 2);
        assertEquals(1, userStorage.getFriendsList(1).size());

        assertEquals(1, userStorage.commonFriends(1, 3).size());

        userStorage.deleteFriend(1, 2);
        assertEquals(0, userStorage.getFriendsList(1).size());
    }
    @Test
    public void addFilmTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmStorage.addFilm(film);
        Optional<Film> filmOptional = Optional.ofNullable(film);

        assertThat(filmOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void findAllFilmsTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmStorage.addFilm(film);

        Film film2 = new Film();
        film2.setName("film name new");
        film2.setReleaseDate(LocalDate.of(2000, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(2));
        filmStorage.addFilm(film2);

        assertEquals(2, filmStorage.findAll().size());
    }
    @Test
    public void updateFilmTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmStorage.addFilm(film);

        Film film2 = new Film();
        film2.setId(1);
        film2.setName("film name new");
        film2.setReleaseDate(LocalDate.of(2000, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(2));

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.updateFilm(film2));

        assertThat(filmOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(filmStorage.getFilm(1)).hasFieldOrPropertyWithValue("name", "film name new"));
    }
    @Test
    public void getFilmTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmStorage.addFilm(film);

        Optional<Film> filmOptional = Optional.ofNullable(film);

        assertThat(filmOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(filmStorage.getFilm(1)).hasFieldOrPropertyWithValue("name", "film name"));
    }

    @Test
    public void putLikeDeleteLikeTopFilmMethodsTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmStorage.addFilm(film);

        Film film2 = new Film();
        film2.setName("film name");
        film2.setReleaseDate(LocalDate.of(1894, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(1));
        filmStorage.addFilm(film2);

        Film film3 = new Film();
        film3.setName("film name");
        film3.setReleaseDate(LocalDate.of(1894, 12,3));
        film3.setDuration(180);
        film3.setDescription("description");
        film3.setMpa(mpaDao.getMpa(1));
        filmStorage.addFilm(film3);

        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userStorage.createUser(user);

        User user2 = new User();
        user2.setEmail("garrys2machinima@gmail.com");
        user2.setLogin("Login");
        user2.setName("Name");
        user2.setBirthday(LocalDate.of(2001,2,8));
        userStorage.createUser(user2);

        filmStorage.putLike(1, 1);
        filmStorage.putLike(1, 2);
        assertEquals(2, filmStorage.getFilm(1).getRate());

        filmStorage.putLike(2, 1);
        assertEquals(2, filmStorage.topFilms(3).get(0).getRate());

        filmStorage.deleteLike(1, 1);
        assertEquals(1, filmStorage.getFilm(1).getRate());
    }
}