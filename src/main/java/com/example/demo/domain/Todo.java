package com.example.demo.domain;


import com.example.demo.dto.request.ReqTodoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "task", "completeYn", "deleteYn", "createdDate", "completedDate", "deletedDate"})
@Table(
        name = "todo",
        indexes = {
            @Index(name = "idx_complete_delete", columnList = "completeYn, deleteYn"), // 완료 여부 & 삭제 여부 복합 인덱스
            @Index(name = "idx_completed_deleted_date", columnList = "completedDate, deletedDate") // 오래된 항목 조회
        }
)
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @Column(nullable = false, length = 200)
    private String task;                // 할일

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private boolean completeYn;         // 완료 여부

    @Column(nullable = false)
    private boolean deleteYn;           // 삭제 여부

    @Column
    private LocalDateTime completedDate; // 완료처리한 시간

    @Column
    private LocalDateTime deletedDate; // 삭제처리한 시간

    public Todo(ReqTodoDTO reqTodoDTO) {
        this.task = reqTodoDTO.getTask();
    }

    public void updateCompleteYn(Boolean completeYn) {
        this.completeYn = completeYn;
        this.completedDate = completeYn ? LocalDateTime.now() : null;  // 완료할 때만 시간 저장
    }

    public void updateDeleteYn(Boolean deleteYn) {
        this.deleteYn = deleteYn;
        this.deletedDate = deleteYn ? LocalDateTime.now() : null;  // 삭제할 때만 시간 저장
    }
}