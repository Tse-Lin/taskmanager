
package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
class TaskRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer mysql =
            new MySQLContainer("mysql:8.4");

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testSaveAndFindTask() {
        Task task = new Task(null, "Test MySQL", false);

        Task saved = taskRepository.saveAndFlush(task);

        Optional<Task> result =
                taskRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Test MySQL", result.get().getTitle());
    }

    @Test
    void testUpdateTask() {
        Task task = taskRepository.saveAndFlush(
                new Task(null, "Old Title", false));

        task.setTitle("Updated Title");
        task.setCompleted(true);

        taskRepository.saveAndFlush(task);
        taskRepository.flush();

        Task result = taskRepository.findById(
                task.getId()).orElseThrow();

        assertEquals("Updated Title", result.getTitle());
        assertTrue(result.isCompleted());
    }

    @Test
    void testDeleteTask() {
        Task task = taskRepository.saveAndFlush(
                new Task(null, "Delete Me", false));

        Long id = task.getId();

        taskRepository.delete(task);
        taskRepository.flush();

        assertFalse(taskRepository.existsById(id));
    }
}
