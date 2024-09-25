package com.softserve.itacademy.component.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.config.SpringSecurityTestConfiguration;
import com.softserve.itacademy.controller.ToDoController;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ToDoController.class, SpringSecurityTestConfiguration.class})
@EnableMethodSecurity
class ToDoControllerSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoService todoService;

    @MockBean
    private TaskService taskService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final long ownerId = 1L;
    private final long todoId = 1L;
    private final long collaboratorId = 2L;

    @BeforeEach
    void setUp() {
        // Setup code, if necessary
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAdminUser_whenPostCreateToDo_thenRedirect() throws Exception {
        mockMvc.perform(post("/todos/create/users/" + ownerId)
                        .param("title", "New ToDo"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void givenOwner_whenPostCreateToDo_thenRedirect() throws Exception {
        mockMvc.perform(post("/todos/create/users/" + ownerId)
                        .param("title", "New ToDo"))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenUnauthenticatedUser_whenCreateToDoForm_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/todos/create/users/" + ownerId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnauthenticatedUser_whenGetAllTodos_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/todos/all/users/" + ownerId))
                .andExpect(status().isUnauthorized());
    }
}
