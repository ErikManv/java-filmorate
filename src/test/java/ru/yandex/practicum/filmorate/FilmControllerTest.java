package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
class FilmControllerTest {

    private final UserDbStorage userService;
    private final FilmDbStorage filmService;
    private final FilmController filmController;

    private final MpaDao mpaDao;
    @Test
    public void addFilmTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmService.addFilm(film);
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
        filmService.addFilm(film);

        Film film2 = new Film();
        film2.setName("film name new");
        film2.setReleaseDate(LocalDate.of(2000, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(2));
        filmService.addFilm(film2);

        assertEquals(2, filmService.findAll().size());
    }
    @Test
    public void updateFilmTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmService.addFilm(film);

        Film film2 = new Film();
        film2.setId(1);
        film2.setName("film name new");
        film2.setReleaseDate(LocalDate.of(2000, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(2));

        Optional<Film> filmOptional = Optional.ofNullable(filmService.updateFilm(film2));

        assertThat(filmOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(filmService.getFilm(1)).hasFieldOrPropertyWithValue("name", "film name new"));
    }

    @Test
    public void updateFilmErrorResponseStatusTest(){
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmService.addFilm(film);

        Film film2 = new Film();
        film2.setId(5);
        film2.setName("film name new");
        film2.setReleaseDate(LocalDate.of(2000, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(2));
        try{
           assertEquals(404, filmController.updateFilm(film2).getStatusCodeValue());
        }catch (NotFoundException e){
        }
    }
    @Test
    public void getFilmTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmService.addFilm(film);

        Optional<Film> filmOptional = Optional.ofNullable(film);

        assertThat(filmOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(filmService.getFilm(1)).hasFieldOrPropertyWithValue("name", "film name"));
    }

    @Test
    public void putLikeDeleteLikeTopFilmMethodsTest() {
        Film film = new Film();
        film.setName("film name");
        film.setReleaseDate(LocalDate.of(1894, 12,3));
        film.setDuration(180);
        film.setDescription("description");
        film.setMpa(mpaDao.getMpa(1));
        filmService.addFilm(film);

        Film film2 = new Film();
        film2.setName("film name");
        film2.setReleaseDate(LocalDate.of(1894, 12,3));
        film2.setDuration(180);
        film2.setDescription("description");
        film2.setMpa(mpaDao.getMpa(1));
        filmService.addFilm(film2);

        Film film3 = new Film();
        film3.setName("film name");
        film3.setReleaseDate(LocalDate.of(1894, 12,3));
        film3.setDuration(180);
        film3.setDescription("description");
        film3.setMpa(mpaDao.getMpa(1));
        filmService.addFilm(film3);

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

        filmService.putLike(1, 1);
        filmService.putLike(1, 2);
        assertEquals(2, filmService.getFilm(1).getRate());

        filmService.putLike(2, 1);
        assertEquals(2, filmService.topFilms(3).get(0).getRate());

        filmService.deleteLike(1, 1);
        assertEquals(1, filmService.getFilm(1).getRate());
    }
}
