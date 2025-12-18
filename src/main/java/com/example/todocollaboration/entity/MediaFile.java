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
@TableName("media_file")
public class MediaFile {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    private String fileName;
    private String contentType;
    private Long size;
    private String s3Key;
    private String status; // uploading/processing/ready/failed
    private String transcodedUrl;
    private String thumbnailUrl;
    private Date createdAt;
    private Date updatedAt;

}