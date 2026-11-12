
package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(
                List.of(new Task(1L, "Learn Java", false))
        );

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(1, tasks.size());
        assertEquals("Learn Java", tasks.get(0).getTitle());
    }

    @Test
    void testGetTaskById() {
        Task task = new Task(1L, "Learn Java", false);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Learn Java", result.getTitle());
    }

    @Test
    void testAddTask() {
        Task savedTask = new Task(1L, "New Task", false);

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        Task result = taskService.addTask("New Task");

        assertEquals("New Task", result.getTitle());
        assertFalse(result.isCompleted());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task(1L, "Old Task", false);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateTask(
                1L, "Updated Task", true);

        assertEquals("Updated Task", result.getTitle());
        assertTrue(result.isCompleted());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task(1L, "Delete Task", false);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(task);
    }
}
