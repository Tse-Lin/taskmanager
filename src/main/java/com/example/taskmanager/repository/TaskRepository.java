package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT t FROM Task t
            WHERE (:completed IS NULL OR t.completed = :completed)
              AND (:keyword IS NULL OR
                   LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<Task> searchTasks(
            @Param("completed") Boolean completed,
            @Param("keyword") String keyword,
            Pageable pageable);
}
