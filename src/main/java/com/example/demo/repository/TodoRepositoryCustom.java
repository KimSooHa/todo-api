package com.example.demo.repository;

import com.example.demo.dto.response.ResTodoDTO;

import java.util.List;

public interface TodoRepositoryCustom {
    List<ResTodoDTO> findTodoList(boolean completeYn, boolean deleteYn);
}