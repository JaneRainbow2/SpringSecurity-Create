package com.softserve.itacademy.component.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.controller.TaskController;
import com.softserve.itacademy.dto.TaskTransformer;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TaskController.class)
class TaskControllerSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private ToDoService toDoService;

    @MockBean
    private StateService stateService;

    @MockBean
    private TaskTransformer taskTransformer;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final long todoId = 1L;
    private final long taskId = 1L;

    @BeforeEach
    void setUp() {
        // Set up any common configurations if necessary
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void givenAdminUser_whenDeleteTask_thenAllowed() throws Exception {
        mockMvc.perform(get("/tasks/" + taskId + "/delete/todos/" + todoId))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void givenUnauthenticatedUser_whenCreateTask_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/tasks/create/todos/" + todoId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnauthenticatedUser_whenUpdateTask_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/tasks/" + taskId + "/update/todos/" + todoId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnauthenticatedUser_whenDeleteTask_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/tasks/" + taskId + "/delete/todos/" + todoId))
                .andExpect(status().isUnauthorized());
    }
}