
package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
class TaskRepositoryIntegrationTest {

        @Container
        @ServiceConnection
        static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

        @Autowired
        private TaskRepository taskRepository;

        @Test
        void testSaveAndFindTask() {
                Task task = new Task(null, "Test MySQL", false);

                Task saved = taskRepository.saveAndFlush(task);

                Optional<Task> result = taskRepository.findById(saved.getId());

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

        @Test
        void testSearchByCompleted() {
                taskRepository.saveAndFlush(
                                new Task(null, "Completed Task", true));

                taskRepository.saveAndFlush(
                                new Task(null, "Pending Task", false));

                Page<Task> result = taskRepository.searchTasks(
                                true, null, PageRequest.of(0, 10));

                assertTrue(result.getContent().stream()
                                .allMatch(Task::isCompleted));
        }

        @Test
        void testSearchByKeyword() {
                taskRepository.saveAndFlush(
                                new Task(null, "Learn Java", false));

                Page<Task> result = taskRepository.searchTasks(
                                null, "java", PageRequest.of(0, 10));

                assertTrue(result.getContent().stream()
                                .anyMatch(task -> task.getTitle().equals("Learn Java")));
        }

        @Test
        void testSearchByCompletedAndKeyword() {
                taskRepository.saveAndFlush(
                                new Task(null, "Learn Spring Boot", false));

                Page<Task> result = taskRepository.searchTasks(
                                false, "spring", PageRequest.of(0, 10));

                assertTrue(result.getContent().stream()
                                .anyMatch(task -> task.getTitle().equals("Learn Spring Boot")
                                                && !task.isCompleted()));
        }

        @Test
        void testSearchPagination() {
                for (int i = 1; i <= 15; i++) {
                        taskRepository.save(
                                        new Task(null, "Page Test " + i, false));
                }

                taskRepository.flush();

                Page<Task> result = taskRepository.searchTasks(
                                false, "Page Test", PageRequest.of(0, 5));

                assertEquals(5, result.getContent().size());
                assertEquals(15, result.getTotalElements());
                assertEquals(3, result.getTotalPages());
        }

}
