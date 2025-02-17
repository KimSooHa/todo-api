package com.example.demo.dto.request;

import com.example.demo.util.StringSanitizerDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqTodoDTO {

    @JsonDeserialize(using = StringSanitizerDeserializer.class)
    private String task;
}
