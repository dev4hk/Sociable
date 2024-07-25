package com.example.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class NotificationArgs {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "source_user_id"))
    })
    private User sourceUser;
    private Integer targetUserId;
    private Integer contentId;

}
