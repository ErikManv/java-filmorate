package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
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
class UserControllerTest {

private final UserService userController;

    @Test
    public void createUserTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));

        Optional<User> userOptional = Optional.ofNullable(userController.createUser(user));

        assertThat(userOptional)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void createUserExceptionTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Log in");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));

        assertThrows(ValidationException.class, ()->{
            userController.createUser(user);});
    }

    @Test
    public void updateUserTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userController.createUser(user);

        User userUpdate = new User();
        userUpdate.setId(1);
        userUpdate.setEmail("garrys2machinima@gmail.com");
        userUpdate.setLogin("Login");
        userUpdate.setName("Name2");
        userUpdate.setBirthday(LocalDate.of(2001,2,8));

        Optional<ResponseEntity<User>> userOptionalNew = Optional.ofNullable(userController.updateUser(userUpdate));

        assertThat(userOptionalNew)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(userController.getUser(1)).hasFieldOrPropertyWithValue("name", "Name2"));
    }

    @Test
    public void updateUserExceptionRespondStatusTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userController.createUser(user);

        User userUpdate = new User();
        userUpdate.setId(6);
        userUpdate.setEmail("garrys2machinima@gmail.com");
        userUpdate.setLogin("Login");
        userUpdate.setName("Name2");
        userUpdate.setBirthday(LocalDate.of(2001,2,8));

        assertEquals(404, userController.updateUser(userUpdate).getStatusCodeValue());
    }

    @Test
    public void getUserTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userController.createUser(user);

        Optional<User> OpUser = Optional.ofNullable(user);
        assertThat(OpUser)
                .isPresent().hasValueSatisfying(user1 ->
                        assertThat(userController.getUser(1)).hasFieldOrPropertyWithValue("email", "garrys2machinima@gmail.com"));
    }

    @Test
    public void getUserExceptionTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userController.createUser(user);
        assertThrows(NotFoundException.class, ()->{
            userController.getUser(3);});
    }
    @Test
    public void findAllUsersTest() {
        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userController.createUser(user);

        User user2 = new User();
        user2.setEmail("samsam@gmail.com");
        user2.setLogin("Login");
        user2.setName("Erni");
        user2.setBirthday(LocalDate.of(2001,2,8));
        userController.createUser(user2);

        assertEquals(2, userController.findAll().size());
    }

    @Test
    public void operationsWithFriendsTest() {
        User user2 = new User();
        user2.setEmail("samsam@gmail.com");
        user2.setLogin("Login");
        user2.setName("Erni");
        user2.setBirthday(LocalDate.of(2001,2,8));
        userController.createUser(user2);

        User user = new User();
        user.setEmail("garrys2machinima@gmail.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2001,2,8));
        userController.createUser(user);

        User user3 = new User();
        user3.setEmail("garrys2machinima@gmail.com");
        user3.setLogin("Login");
        user3.setName("Name");
        user3.setBirthday(LocalDate.of(2001,2,8));
        userController.createUser(user3);

        userController.putFriend(1, 2);
        assertEquals(1, userController.getFriendsList(1).size());

        userController.putFriend(3, 2);
        assertEquals(1, userController.getFriendsList(1).size());

        assertEquals(1, userController.commonFriends(1, 3).size());

        userController.deleteFriend(1, 2);
        assertEquals(0, userController.getFriendsList(1).size());
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
