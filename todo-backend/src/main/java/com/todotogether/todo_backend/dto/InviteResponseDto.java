package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.GroupInvite;
import com.todotogether.todo_backend.entity.InviteStatus;
import lombok.Getter;

@Getter
public class InviteResponseDto {
    private final Long inviteId;
    private final Long groupId;
    private final String groupTitle;
    private final String sender;
    private final InviteStatus status;

    public InviteResponseDto(GroupInvite invite) {
        this.inviteId = invite.getId();
        this.groupId = invite.getGroup().getId();
        this.groupTitle = invite.getGroup().getTitle();
        this.sender = invite.getSender().getUsername();
        this.status = invite.getStatus();
    }
}
