package com.example.todocollaboration.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlineStatus {

    private Long userId;
    private Long listId;
    private Boolean isOnline;
    private Date lastSeen;
    private Long currentItem;
    private String sessionId;

}