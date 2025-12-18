package com.example.todocollaboration.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("todo_lists")
public class TodoList {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    private String description;
    private Long ownerId;
    private String visibility;
    private Date createdAt;
    private Date updatedAt;
    private Date lastActivity;
//    private Integer itemsCount;
    private String status;
    private Integer priority;
    private Date dueDate;

}