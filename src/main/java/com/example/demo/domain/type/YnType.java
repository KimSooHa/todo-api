package com.example.demo.domain.type;

import lombok.Getter;

@Getter
public enum YnType {
    N(0, "N"),
    Y(1, "Y");

    private final int type;
    private final String name;

    YnType(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
