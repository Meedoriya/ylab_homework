package org.alibi.in;

import org.alibi.application.UserService;
import org.alibi.domain.model.Role;
import org.alibi.domain.model.User;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserInputHandlerTest {

    private UserService userService;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should register user")
    void testRegisterUser() {
        String input = "testuser\ntestpassword\nUSER\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        UserInputHandler userInputHandler = new UserInputHandler(userService);

        userInputHandler.registerUser();

        verify(userService).registerUser(eq("testuser"), eq("testpassword"), eq(Role.USER));
        assertThat(outContent.toString()).contains("User registered successfully.");
    }

    @Test
    @DisplayName("Should login user")
    void testLoginUser() {
        String input = "testuser\ntestpassword\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        User expectedUser = new User();
        expectedUser.setUsername("testuser");
        when(userService.loginUser(anyString(), anyString())).thenReturn(expectedUser);

        UserInputHandler userInputHandler = new UserInputHandler(userService);

        User user = userInputHandler.loginUser();

        verify(userService).loginUser(eq("testuser"), eq("testpassword"));
        assertThat(user).isEqualTo(expectedUser);
    }
}
