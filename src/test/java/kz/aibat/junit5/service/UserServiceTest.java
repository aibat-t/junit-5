package kz.aibat.junit5.service;

import kz.aibat.junit5.dto.User;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    static final User IVAN = User.builder().id(1).username("Ivan").password("123").build();
    static final User PETR = User.builder().id(2).username("Petr").password("111").build();
    private UserService userService;

    @BeforeAll
    static void beforeAll() {
        System.out.println("before ALL!!!!");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("Before each");
        userService = new UserService();
    }

    @Test
    void shouldBeEmptyIfNoUserAdded() {
        System.out.println("Test1");
        var users = userService.getAll();
        assertTrue(users.isEmpty(), "User list should be empty");
    }

    @Test
     void userSizeIfUserAdded() {
        System.out.println("Test1");
        userService.add(IVAN);
        userService.add(PETR);

        var users = userService.getAll();

        assertEquals(2, users.size());
    }

    @Test
    void loginSuccessIfUserExists() {
        userService.add(IVAN);
        Optional<User> userOp = userService.login(IVAN.getUsername(), IVAN.getPassword());
        assertTrue(userOp.isPresent());
    }

    @Test
    void loginUnsuccessIfUserExists() {
        userService.add(IVAN);
        Optional<User> userOp = userService.login(IVAN.getUsername(), "dummy");
        assertTrue(userOp.isEmpty());
    }

    @Test
    void loginUnsuccessIfUserDoesNotExists() {
        userService.add(IVAN);
        Optional<User> userOp = userService.login("dummy", IVAN.getPassword());
        assertTrue(userOp.isEmpty());
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after ALL!!!!");
    }
}
