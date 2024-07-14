package org.alibi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.alibi.config.TestConfig;
import org.alibi.domain.dto.UserDto;
import org.alibi.domain.dto.UserRegistrationDto;
import org.alibi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@EnableWebMvc
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    private MockMvc mockMvc;

    private UserRegistrationDto userRegistrationDto;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRegistrationDto = new UserRegistrationDto("testuser", "password");
        userDto = new UserDto(1L, "testuser");
    }

    @Test
    @DisplayName("Регистрация пользователя")
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRegistrationDto)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).registerUser(any(UserRegistrationDto.class));
    }

    @Test
    @DisplayName("Авторизация пользователя")
    public void testLoginUser() throws Exception {
        when(userService.loginUser("testuser", "password")).thenReturn(userDto);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRegistrationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService, times(1)).loginUser("testuser", "password");
    }
}
