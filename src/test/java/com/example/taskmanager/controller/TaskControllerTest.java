
package com.example.taskmanager.controller;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.service.TaskService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private TaskService taskService;

        @Test
        void testSearchTasks() throws Exception {
                when(taskService.getTasks(
                                eq(false), eq("Java"), any(Pageable.class)))
                                .thenReturn(new PageImpl<>(
                                                List.of(new Task(1L, "Learn Java", false))));

                mockMvc.perform(get("/tasks")
                                .param("completed", "false")
                                .param("keyword", "Java"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].title")
                                                .value("Learn Java"));
        }

        @Test
        void testGetAllTasks() throws Exception {
                when(taskService.getTasks(
                                isNull(), isNull(), any(Pageable.class)))
                                .thenReturn(new PageImpl<>(
                                                List.of(new Task(1L, "Learn Java", false))));

                mockMvc.perform(get("/tasks"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value(1))
                                .andExpect(jsonPath("$.content[0].title")
                                                .value("Learn Java"))
                                .andExpect(jsonPath("$.totalElements").value(1));
        }

        @Test
        void testGetTaskById() throws Exception {
                when(taskService.getTaskById(1L))
                                .thenReturn(new Task(1L, "Learn Java", false));

                mockMvc.perform(get("/tasks/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Learn Java"));
        }

        @Test
        void testAddTask() throws Exception {
                when(taskService.addTask("New Task"))
                                .thenReturn(new Task(1L, "New Task", false));

                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"New Task\"}"))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.title").value("New Task"));
        }

        @Test
        void testAddTaskWithBlankTitle() throws Exception {
                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"\"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error")
                                                .value("Task title cannot be blank"));

                verifyNoInteractions(taskService);
        }

        @Test
        void testUpdateTask() throws Exception {
                when(taskService.updateTask(1L, "Updated Task", true))
                                .thenReturn(new Task(1L, "Updated Task", true));

                mockMvc.perform(put("/tasks/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"Updated Task\",\"completed\":true}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated Task"))
                                .andExpect(jsonPath("$.completed").value(true));
        }

        @Test
        void testDeleteTask() throws Exception {
                mockMvc.perform(delete("/tasks/1"))
                                .andExpect(status().isNoContent());

                verify(taskService).deleteTask(1L);
        }

        @Test
        void testTaskNotFound() throws Exception {
                when(taskService.getTaskById(999L))
                                .thenThrow(new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Task not found"));

                mockMvc.perform(get("/tasks/999"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error")
                                                .value("Task not found"));
        }
}
