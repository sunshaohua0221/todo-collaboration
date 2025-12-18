package com.example.todocollaboration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todocollaboration.entity.TodoItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TodoItemMapper extends BaseMapper<TodoItem> {

    @Select("SELECT * FROM todo_items WHERE list_id = #{listId}")
    List<TodoItem> findByListId(Long listId);

    @Select("SELECT COUNT(*) FROM todo_items WHERE list_id = #{listId}")
    int countByListId(Long listId);

}