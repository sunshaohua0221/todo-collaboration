package com.example.todocollaboration.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("todo_item")
public class TodoItem {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long listId;
    private String content;
    private String status;
    private Integer priority;
    private Date dueDate;
    private String media; // JSON格式存储媒体附件数组
    private Long createdBy;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;

}