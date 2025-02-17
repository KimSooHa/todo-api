package com.example.demo.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.demo.dto.response.QResTodoDTO is a Querydsl Projection type for ResTodoDTO
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QResTodoDTO extends ConstructorExpression<ResTodoDTO> {

    private static final long serialVersionUID = 1487983517L;

    public QResTodoDTO(com.querydsl.core.types.Expression<? extends com.example.demo.domain.Todo> todo) {
        super(ResTodoDTO.class, new Class<?>[]{com.example.demo.domain.Todo.class}, todo);
    }

}

