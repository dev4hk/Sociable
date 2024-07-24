package com.example.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationArgs {

    private Integer sourceUserId;
    private Integer targetUserId;
    private Integer contentId;

}
