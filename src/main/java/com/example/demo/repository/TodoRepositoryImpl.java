package com.example.demo.repository;

import com.example.demo.dto.response.ResTodoDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.demo.domain.QTodo.todo;

@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ResTodoDTO> findTodoList(boolean completeYn, boolean deleteYn) {
            return queryFactory
                    .select(Projections.bean(ResTodoDTO.class,
                            todo.id,
                            todo.task,
                            todo.createdDate,
                            todo.completeYn,
                            todo.deleteYn,
                            todo.completedDate,
                            todo.deletedDate))
                    .from(todo)
                    .where(filterByCompleteAndDelete(completeYn, deleteYn))  // BooleanExpression 활용
                    .orderBy(todo.createdDate.asc())
                    .fetch();
    }


    private BooleanExpression filterByCompleteAndDelete(boolean completeYn, boolean deleteYn) {
        BooleanExpression condition = Expressions.TRUE;  // 기본적으로 항상 참인 조건을 설정

        if (deleteYn) {
            condition = condition.and(todo.deleteYn.eq(true));
        }
        else if (completeYn) {
            condition = condition.and(todo.completeYn.eq(true)).and(todo.deleteYn.eq(false));
        } else {
            condition = condition.and(todo.completeYn.eq(false)).and(todo.deleteYn.eq(false));
        }

        return condition;
    }
}