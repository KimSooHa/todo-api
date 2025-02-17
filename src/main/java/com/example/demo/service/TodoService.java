package com.example.demo.service;

import com.example.demo.domain.Todo;
import com.example.demo.domain.type.YnType;
import com.example.demo.dto.request.ReqTodoDTO;
import com.example.demo.dto.response.ResTodoDTO;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.TodoRepository;
import com.example.demo.util.FieldCheckUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.domain.type.YnType.Y;
import static com.example.demo.exception.ErrorCode.TODO_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TodoService {
    private final TodoRepository todoRepository;

    // ToDo 목록 조회
    public List<ResTodoDTO> getTodoList(boolean completeYn, boolean deleteYn) {
        List<ResTodoDTO> todoList = todoRepository.findTodoList(completeYn, deleteYn);
        return todoList;
    }

    // ToDo 단일 조회
    public ResTodoDTO getTodo(Long id) {
        return new ResTodoDTO(todoRepository.findById(id).orElseThrow(() -> new CustomException(TODO_NOT_FOUND)));
    }

    // ToDo 추가
    @Transactional
    public ResTodoDTO insertTodo(ReqTodoDTO reqTodoDTO) {
        FieldCheckUtils.paramNullCheck(reqTodoDTO.getTask());
        String task = reqTodoDTO.getTask().trim();
        FieldCheckUtils.overWordCntCheck(task, 200);
        reqTodoDTO.setTask(task);
        Todo todo = new Todo(reqTodoDTO);
        todoRepository.save(todo);

        return new ResTodoDTO(todo);
    }

    // Todo 수정
    @Transactional
    public void updateTask(Long todoId, ReqTodoDTO reqTodoDTO) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));
        FieldCheckUtils.paramNullCheck(reqTodoDTO.getTask());
        String task = reqTodoDTO.getTask().trim();
        FieldCheckUtils.overWordCntCheck(task, 200);
        todo.setTask(task);
        todoRepository.save(todo);
        todoRepository.flush();
    }

    // ToDo 완료여부 수정
    @Transactional
    public void updateCompleteYn(Long todoId, YnType ynType) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));
        todo.updateCompleteYn(ynType == Y ? true : false);
        todoRepository.save(todo);
    }

    // ToDo 삭제여부 수정
    @Transactional
    public void updateDeleteYn(Long todoId, YnType ynType) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));
        todo.updateDeleteYn(ynType == Y ? true : false);
        todoRepository.save(todo);
    }

    // ToDo 삭제
    @Transactional
    public void deleteTodo(Long id) {
        todoRepository.findById(id)
                .orElseThrow(() -> new CustomException(TODO_NOT_FOUND));
        todoRepository.deleteById(id);
    }

    // 3일 이상 지난 완료된 Todo 자동 삭제
    @Scheduled(fixedRate = 1000 * 60 * 60) // 1시간마다 실행
    @Transactional
    public void deleteOldTodos() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        // 삭제된 상태에서 3일 이상 지난 Todo 삭제
        Optional<List<Todo>> todosToDelete = todoRepository.findByCompletedDateBeforeAndDeleteYnFalseAndCompleteYnTrue(threeDaysAgo);
        if(!todosToDelete.get().isEmpty()) {
            todoRepository.deleteAll(todosToDelete.get());
            log.info("3일 이상 지난 Todo 항목을 삭제했습니다.");
            todosToDelete.get().forEach(todo -> log.info("삭제된 항목: id = {}, 할일 = {}", todo.getId(), todo.getTask()));
        }

        // 완료된 상태에서 3일 이상 지난 Todo 삭제
        todosToDelete = todoRepository.findByDeletedDateBeforeAndDeleteYnTrue(threeDaysAgo);
        if(!todosToDelete.get().isEmpty()) {
            todoRepository.deleteAll(todosToDelete.get());
            log.info("3일 이상 지난 Todo 항목을 삭제했습니다.");
            todosToDelete.get().forEach(todo -> log.info("삭제된 항목: id = {}, 할일 = {}", todo.getId(), todo.getTask()));
        }
    }
}