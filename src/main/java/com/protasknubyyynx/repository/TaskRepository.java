package com.protasknubyyynx.repository;

import com.protasknubyyynx.domain.Task;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT s.name, COUNT(t) FROM Task t JOIN t.status s WHERE t.createdBy = :login GROUP BY s.name")
    List<Object[]> countTasksByStatusNameAndCreatedBy(@Param("login") String login);

    @Query("SELECT p.name, COUNT(t) FROM Task t JOIN t.priority p WHERE t.createdBy = :login GROUP BY p.name")
    List<Object[]> countTasksByPriorityNameAndCreatedBy(@Param("login") String login);
}
