package com.example.todocollaboration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.todocollaboration.entity.TodoList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface TodoListMapper extends BaseMapper<TodoList> {

    @Update("UPDATE todo_lists SET items_count = #{itemsCount}, last_activity = #{lastActivity} WHERE id = #{listId}")
    int updateItemsCountAndLastActivity(Long listId, long itemsCount, Date lastActivity);

    @Update("UPDATE todo_lists SET last_activity = #{lastActivity} WHERE id = #{listId}")
    int updateLastActivity(Long listId, Date lastActivity);

}