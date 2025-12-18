package com.example.todocollaboration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todocollaboration.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("SELECT * FROM permission WHERE user_id = #{userId}")
    List<Permission> findByUserId(Long userId);
    @Select("SELECT * FROM permission WHERE list_id = #{listId}")
    List<Permission> findByListId(Long listId);
    @Select("SELECT * FROM permission WHERE list_id = #{listId} and user_id = #{userId}")
    Permission findByListIdAndUserId(Long listId, Long userId);
}