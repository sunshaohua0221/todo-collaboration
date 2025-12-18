package com.example.todocollaboration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todocollaboration.entity.MediaFile;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MediaFileMapper extends BaseMapper<MediaFile> {
    
    List<MediaFile> findByUserId(Long userId);
    List<MediaFile> findByStatus(String status);

}