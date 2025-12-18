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
@TableName("permission")
public class Permission {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long listId;
    private Long userId;
    private String permissionType; // view/edit
    private Long grantedBy;
    private Date createdAt;
    private Date updatedAt;

}