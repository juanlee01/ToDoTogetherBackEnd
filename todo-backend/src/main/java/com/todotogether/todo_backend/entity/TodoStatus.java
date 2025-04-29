package com.todotogether.todo_backend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TodoStatus {
    PENDING,
    IN_PROGRESS,
    ON_HOLD,
    REVISION_REQUESTED,
    COMPLETED,
    CANCELED;
}
