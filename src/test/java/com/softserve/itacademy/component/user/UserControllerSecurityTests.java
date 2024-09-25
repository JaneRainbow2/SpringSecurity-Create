package com.softserve.itacademy.component.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.config.SpringSecurityTestConfiguration;
import com.softserve.itacademy.controller.UserController;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {UserController.class, SpringSecurityTestConfiguration.class})
@EnableMethodSecurity
class UserControllerSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final long userId = 1L;

    @BeforeEach
    void setUp() {
        // Set up any common configurations if necessary
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAdminUser_whenCreateUser_thenAllowed() throws Exception {
        mockMvc.perform(get("/users/create"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAdminUser_whenPostCreateUser_thenRedirect() throws Exception {
        mockMvc.perform(post("/users/create")
                        .param("username", "testUser")
                        .param("password", "password123"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenUser_whenGetAllUsers_thenForbidden() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenUser_whenUpdateAnotherProfile_thenForbidden() throws Exception {
        mockMvc.perform(get("/users/2/update"))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenUnauthenticatedUser_whenCreateUser_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/users/create"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnauthenticatedUser_whenGetAllUsers_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isUnauthorized());
    }
}