package com.example.todocollaboration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.todocollaboration.entity.TodoList;
import java.util.List;
import java.util.Map;

public interface TodoListService {
    
    TodoList createTodoList(TodoList todoList);
    TodoList getTodoListById(Long id);
    Page<TodoList> getTodoLists(Map<String, Object> map);
    TodoList updateTodoList(Long id, TodoList todoList);
    void deleteTodoList(Long id);

}