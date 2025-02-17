package com.example.demo.controller;

import com.example.demo.domain.type.YnType;
import com.example.demo.dto.request.ReqTodoDTO;
import com.example.demo.dto.response.ResTodoDTO;
import com.example.demo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Todo API")
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Slf4j
public class TodoController {
    private final TodoService todoService;

    // ToDo 목록 조회
    @GetMapping(name = "ToDo 목록 조회")
    @Operation(summary = "ToDo 목록 조회", description = "todo(할일) 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<List<ResTodoDTO>> getTodoList() {
        return ResponseEntity.ok().body(todoService.getTodoList(false, false));
    }

    // ToDO 완료 목록 조회
    @GetMapping(name = "ToDo 완료 목록 조회", path = "/completed")
    @Operation(summary = "ToDo 완료 목록 조회", description = "todo(할일) 완료 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<List<ResTodoDTO>> getCompletedTodoList() {
        return ResponseEntity.ok().body(todoService.getTodoList(true, false));
    }

    // ToDO 삭제 목록 조회
    @GetMapping(name = "ToDo 삭제 목록 조회", path = "/deleted")
    @Operation(summary = "ToDo 삭제 목록 조회", description = "todo(할일) 삭제 목록을 조회힙니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    public ResponseEntity<List<ResTodoDTO>> getDeletedTodoList() {
        return ResponseEntity.ok().body(todoService.getTodoList(false, true));
    }

    // ToDo 단일 조회
    @GetMapping(name = "ToDo 단일 조회", path = "/{id}")
    @Operation(summary = "ToDo 단일 조회", description = "todo(할일) 하나를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "404", description = "Resource 를 찾을 수 없음")
    public ResponseEntity<ResTodoDTO> getTodo(
            @Parameter(required = true, description = "todo 고유번호")
            @PathVariable("id") Long id) {
        ResTodoDTO resTodoDTO = todoService.getTodo(id);
        return ResponseEntity.ok().body(resTodoDTO);
    }

    // ToDo 추가
    @PostMapping(name = "ToDo 추가")
    @Operation(summary = "ToDo 추가", description = "todo(할일)을 생성합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    public ResponseEntity<ResTodoDTO> insertTodo(
            @Parameter(required = true, description = "todo 전달 객체")
            @RequestBody ReqTodoDTO reqTodoDTO) {
        ResTodoDTO resTodoDTO = todoService.insertTodo(reqTodoDTO);
        return ResponseEntity.ok().body(resTodoDTO);
    }

    // ToDo 수정
    @PutMapping(name = "ToDo 수정", path = "/{id}")
    @Operation(summary = "ToDo 수정", description = "todo(할일)을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "Resource 를 찾을 수 없음")
    public ResponseEntity<String> updateTask(
            @Parameter(required = true, description = "todo 고유번호")
            @PathVariable("id") Long id,
            @Parameter(required = true, description = "todo 전달 객체")
            @RequestBody ReqTodoDTO reqTodoDTO) {
        todoService.updateTask(id, reqTodoDTO);
        return ResponseEntity.ok().body("수정하였습니다.");
    }

    // ToDo 완료여부 수정
    @PutMapping(name = "ToDo 완료여부 수정", path = "/{id}/completeYn")
    @Operation(summary = "ToDo 완료여부 수정", description = "todo(할일)의 완료여부를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "Resource 를 찾을 수 없음")
    public ResponseEntity updateCompleteYn(
            @Parameter(required = true, description = "todo 고유번호")
            @PathVariable("id") Long id,
            @Parameter(required = true, description = "완료여부 타입")
            @RequestParam(name = "ynType") YnType ynType) {
        todoService.updateCompleteYn(id, ynType);
        return ResponseEntity.ok().body(id);
    }


    // ToDo 삭제여부 수정
    @PutMapping(name = "ToDo 삭제 처리", path = "/{id}/deleteYn")
    @Operation(summary = "ToDo 삭제여부 수정", description = "todo(힐일)의 삭제여부를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "404", description = "Resource 를 찾을 수 없음")
    public ResponseEntity updateDeleteYn(
            @Parameter(required = true, description = "todo 고유번호")
            @PathVariable("id") Long id,
            @Parameter(required = true, description = "삭제여부 타입")
            @RequestParam(name = "ynType") YnType ynType) {
        todoService.updateDeleteYn(id, ynType);
        return ResponseEntity.ok().body(id);
    }

    // ToDo 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "ToDo 삭제", description = "todo(할일)을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "404", description = "Resource 를 찾을 수 없음")
    public ResponseEntity deleteTodo(
            @Parameter(required = true, description = "todo 고유번호")
            @PathVariable("id") Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok().body(id);
    }
}