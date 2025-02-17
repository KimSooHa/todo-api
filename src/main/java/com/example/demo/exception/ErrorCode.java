package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    ERROR_TASK_NULL(BAD_REQUEST, "todo 입력값이 비어있습니다."),
    ERROR_TODO_OVER_LENGTH(BAD_REQUEST, "todo 입력값의 길이가 초과했습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    TODO_NOT_FOUND(NOT_FOUND, "해당 할일을 찾을 수 없습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 에러 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상하지 못한 에러")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}