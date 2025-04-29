package com.todotogether.todo_backend.config;

import com.todotogether.todo_backend.entity.TodoStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TodoStatusConverter implements Converter<String, TodoStatus> {

    @Override
    public TodoStatus convert(String source) {
        return TodoStatus.valueOf(source.toUpperCase());
    }
}