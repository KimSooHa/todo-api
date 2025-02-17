package com.example.demo.repository;

import com.example.demo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {
    // 7일 이상 지난 완료된 상태의 Todo 조회 (completeYn = true, deleteYn = false)
    Optional<List<Todo>> findByCompletedDateBeforeAndDeleteYnFalseAndCompleteYnTrue(LocalDateTime completedDate);

    // 7일 이상 지난 삭제된 상태의 Todo 조회 (deleteYn = true)
    Optional<List<Todo>> findByDeletedDateBeforeAndDeleteYnTrue(LocalDateTime deletedDate);
}