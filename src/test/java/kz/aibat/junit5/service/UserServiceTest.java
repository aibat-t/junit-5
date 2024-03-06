package kz.aibat.junit5.service;

import kz.aibat.junit5.dao.UserDao;
import kz.aibat.junit5.dto.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(
        MockitoExtension.class
)
public class UserServiceTest {

    static final User IVAN = User.builder().id(1).username("Ivan").password("123").build();
    static final User PETR = User.builder().id(2).username("Petr").password("111").build();

    @Captor
    private ArgumentCaptor<Integer> argumentCaptor;
    @InjectMocks
    private UserService userService;
    @Mock
    private UserDao userDao;

    @BeforeAll
    static void beforeAll() {
        System.out.println("before ALL!!!!");
    }

    @BeforeEach
    void beforeEach() {
        doReturn(true).when(userDao).delete(IVAN.getId());
        System.out.println("Before each");
    }

    @Test
    void throwExceptionIfDatabaseIsNotAvailable() {
        doThrow(RuntimeException.class).when(userDao).delete(IVAN.getId());
        assertThrows(RuntimeException.class, () -> userService.delete(IVAN.getId()));
    }

    @Test
    void shouldDeleteExistedUser() {
        userService.add(IVAN);

//        when(userDao.delete(Mockito.anyInt())).thenReturn(true);

        var deleteResult = userService.delete(IVAN.getId());

        verify(userDao, Mockito.times(1)).delete(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue()).isEqualTo(IVAN.getId());
        assertThat(deleteResult).isTrue();
    }

    @Test
    void shouldBeEmptyIfNoUserAdded() {
        System.out.println("Test1");
        var users = userService.getAll();
        assertThat(users).isEmpty();
    }

    @Test
     void userSizeIfUserAdded() {
        System.out.println("Test1");
        userService.add(IVAN);
        userService.add(PETR);

        var users = userService.getAll();

        assertThat(users).hasSize(2);
    }

    @Test
    void usersConvertedToMapById() {
        userService.add(IVAN, PETR);

        Map<Integer, User> users = userService.convertToMap();

        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId()),
                () -> assertThat(users).containsValues(IVAN, PETR)
        );


    }

    @AfterEach
    void afterEach() {
        System.out.println("After each");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after ALL!!!!");
    }

    @Nested
    @Tag("login")
    class LoginTest {
        @Test
        void loginSuccessIfUserExists() {
            userService.add(IVAN);
            Optional<User> userOp = userService.login(IVAN.getUsername(), IVAN.getPassword());
            assertThat(userOp).isPresent();
        }

        @Test
        void throwExceptionIfUserameOrPasswordIsNull() {
            assertAll(
                    () -> {
                        var exception = assertThrows(IllegalArgumentException.class, () -> userService.login(null, "dummy"));
                        assertThat(exception.getMessage()).isEqualTo("username or password is null");
                    },
                    () -> assertThrows(IllegalArgumentException.class, () -> userService.login("dummy", null))
            );

        }

        @Test
        void loginUnsuccessIfUserExists() {
            userService.add(IVAN);
            Optional<User> userOp = userService.login(IVAN.getUsername(), "dummy");
            assertThat(userOp).isEmpty();
        }

        @Test
        void loginUnsuccessIfUserDoesNotExists() {
            userService.add(IVAN);
            Optional<User> userOp = userService.login("dummy", IVAN.getPassword());
            assertThat(userOp).isEmpty();
        }

        @ParameterizedTest
//        @ValueSource(strings = {
//                "Ivan", "Petr"
//        })
        @MethodSource("getArgumentsForLoginTest")
        void loginParametrizedTest(String username, String password, Optional<User> user) {
            userService.add(IVAN, PETR);
            var userOp = userService.login(username, password);

            assertThat(userOp).isEqualTo(user);
        }

        static Stream<Arguments> getArgumentsForLoginTest() {
            return Stream.of(
                    Arguments.of("Ivan", "123", Optional.of(IVAN)),
                    Arguments.of("Petr", "111", Optional.of(PETR)),
                    Arguments.of("dummy", "111", Optional.empty()),
                    Arguments.of("Pet", "dummy", Optional.empty())
            );
        }
    }
}
