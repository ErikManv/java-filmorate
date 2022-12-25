package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
	FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
	UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
	@Test
	void validationFilmNameExcepTest() {
		Film film = new Film("", "disc", LocalDate.of(1999,12,12), 0);
		assertThrows(ValidationException.class,() ->  filmController.addFilm(film));
	}

	@Test
	void validationFilmDescriptionExcepTest() {
		Film film = new Film("name", "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.", LocalDate.of(1999,12,12), 0);
		assertThrows(ValidationException.class,() ->  filmController.addFilm(film));
	}

	@Test
	void validationFilmDurationExcepTest() {
		Film film = new Film("name", "disc", LocalDate.of(1999,12,12), -1);
		assertThrows(ValidationException.class,() ->  filmController.addFilm(film));
	}

	@Test
	void validationFilmDataExcepTest() {
		Film film = new Film("name", "disc", LocalDate.of(1967,12,12), -1);
		assertThrows(ValidationException.class,() ->  filmController.addFilm(film));
	}

	@Test
	void validationUserEmailExcepTest() {
		User user = new User("login", "discyandex.ru", LocalDate.of(1967,12,12));
		assertThrows(ValidationException.class,() ->  userController.createUser(user));
		User user2 = new User("", "discyandex.ru", LocalDate.of(1967,12,12));
		assertThrows(ValidationException.class,() ->  userController.createUser(user2));
	}

	@Test
	void validationUserLoginExcepTest() {
		User user = new User("", "disc@yandex.ru", LocalDate.of(1967,12,12));
		assertThrows(ValidationException.class,() ->  userController.createUser(user));
		User user2 = new User("log in", "disc@yandex.ru", LocalDate.of(1967,12,12));
		assertThrows(ValidationException.class,() ->  userController.createUser(user2));
	}

	@Test
	void validationUserDateOfBirthExcepTest() {
		User user = new User("login", "disc@yandex.ru", LocalDate.of(2500,12,12));
		assertThrows(ValidationException.class,() ->  userController.createUser(user));
	}

}
