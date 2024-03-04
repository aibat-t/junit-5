package kz.aibat.junit5.service;

import kz.aibat.junit5.dto.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

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
        userService.add(new User());
        userService.add(new User());

        var users = userService.getAll();

        assertEquals(2, users.size());
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
