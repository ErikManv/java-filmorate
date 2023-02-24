package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext (classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {

    private final UserDbStorage userService;
    private final FilmDbStorage filmController;
    private final FilmService filmService;

    private final MpaDao mpaDao;
    @Test
    public void addFilmTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmController.addFilm(film);
        Optional<Film> filmOptional = Optional.ofNullable(film);

        assertThat(filmOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void addFilmErrorTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(11, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        assertThrows(ValidationException.class, ()->{
            filmService.addFilm(film);});

    }

    @Test
    public void findAllFilmsTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmController.addFilm(film);

        Film film2 = new Film();
        film2.setName("film name new");
        film2.setReleaseDate(LocalDate.of(2000, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(2));
        filmController.addFilm(film2);

        assertEquals(2, filmController.findAll().size());
    }
    @Test
    public void updateFilmTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmController.addFilm(film);

        Film film2 = new Film();
        film2.setId(1);
        film2.setName("film name new");
        film2.setReleaseDate(LocalDate.of(2000, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(2));

        Optional<Film> filmOptional = Optional.ofNullable(filmController.updateFilm(film2));

        assertThat(filmOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(filmController.getFilm(1)).hasFieldOrPropertyWithValue("name", "film name new"));
    }

    @Test
    public void updateFilmErrorResponseStatusTest(){
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1898, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmController.addFilm(film);

        Film film2 = new Film();
        film2.setId(5);
        film2.setName("film name new");
        film2.setReleaseDate(LocalDate.of(2000, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(2));
        assertEquals(404, filmService.updateFilm(film2).getStatusCodeValue());
        }

    @Test
    public void getFilmTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmController.addFilm(film);

        Optional<Film> filmOptional = Optional.ofNullable(film);

        assertThat(filmOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(filmController.getFilm(1)).hasFieldOrPropertyWithValue("name", "film name"));
    }

    @Test
    public void getFilmErrorTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmController.addFilm(film);

        assertThrows(NotFoundException.class, ()->{
            filmService.getFilm(5);});
    }
    @Test
    public void putLikeDeleteLikeTopFilmMethodsTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmController.addFilm(film);

        Film film2 = new Film();
        film2.setName("film name");
        film2.setReleaseDate(LocalDate.of(1894, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(1));
        filmController.addFilm(film2);

        Film film3 = new Film();
        film3.setName("film name");
        film3.setReleaseDate(LocalDate.of(1894, 12,3));
        film3.setDuration(180);
        film3.setDescription("description");
        film3.setMpa(mpaDao.getMpa(1));
        filmController.addFilm(film3);

        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userService.createUser(user);

        User user2 = new User();
        user2.setEmail("garrys2machinima@gmail.com");
        user2.setLogin("Login");
        user2.setName("Name");
        user2.setBirthday(LocalDate.of(2001,2,8));
        userService.createUser(user2);

        filmController.putLike(1, 1);
        filmController.putLike(1, 2);
        assertEquals(2, filmController.getFilm(1).getRate());

        filmController.putLike(2, 1);
        assertEquals(2, filmController.topFilms(3).get(0).getRate());

        filmController.deleteLike(1, 1);
        assertEquals(1, filmController.getFilm(1).getRate());
    }
}
