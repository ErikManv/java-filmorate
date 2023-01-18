package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext (classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

private final UserService userService;

    @Test
    public void createUserTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));

        Optional<User> userOptional = Optional.ofNullable(userService.createUser(user));

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
        userService.createUser(user);

        User userUpdate = new User();
        userUpdate.setId(1);
        userUpdate.setEmail("garrys2machinima@gmail.com");
        userUpdate.setLogin("Login");
        userUpdate.setName("Name2");
        userUpdate.setBirthday(LocalDate.of(2001,2,8));

        Optional<ResponseEntity<User>> userOptionalNew = Optional.ofNullable(userService.updateUser(userUpdate));

        assertThat(userOptionalNew)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(userService.getUser(1)).hasFieldOrPropertyWithValue("name", "Name2"));
    }

    @Test
    public void getUserTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userService.createUser(user);

        Optional<User> OpUser = Optional.ofNullable(user);
        assertThat(OpUser)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(userService.getUser(1)).hasFieldOrPropertyWithValue("email", "garrys2machinima@gmail.com"));
    }
    @Test
    public void findAllUsersTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userService.createUser(user);

        User user2 = new User();
        user2.setEmail("samsam@gmail.com");
        user2.setLogin("Login");
        user2.setName("Erni");
        user2.setBirthday(LocalDate.of(2001,2,8));
        userService.createUser(user2);

        assertEquals(2, userService.findAll().size());
    }

    @Test
    public void operationsWithFriendsTest() {
        User user2 = new User();
        user2.setEmail("samsam@gmail.com");
        user2.setLogin("Login");
        user2.setName("Erni");
        user2.setBirthday(LocalDate.of(2001,2,8));
        userService.createUser(user2);

        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userService.createUser(user);

        User user3 = new User();
        user3.setEmail("garrys2machinima@gmail.com");
        user3.setLogin("Login");
        user3.setName("Name");
        user3.setBirthday(LocalDate.of(2001,2,8));
        userService.createUser(user3);

        userService.putFriend(1, 2);
        assertEquals(1, userService.getFriendsList(1).size());

        userService.putFriend(3, 2);
        assertEquals(1, userService.getFriendsList(1).size());

        assertEquals(1, userService.commonFriends(1, 3).size());

        userService.deleteFriend(1, 2);
        assertEquals(0, userService.getFriendsList(1).size());
    }

    @Test
    void shouldReturnResponseStatus400CreatingUserWithEmailWithGap() {
        User user = new User();
        user.setId(0);
        user.setName("Name");
        user.setEmail("login@mail.ru");
        user.setLogin("lo gin");
        user.setBirthday(LocalDate.of(2001, 1, 12));
        
    }
}
