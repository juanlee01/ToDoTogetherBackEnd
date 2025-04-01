package com.todotogether.todo_backend.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GroupMemberId implements Serializable {

    private Long groupId;
    private Long userId;

    // equals() and hashCode() 꼭 오버라이딩!
}