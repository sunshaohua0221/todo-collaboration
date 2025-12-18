package com.example.todocollaboration.service;

import com.example.todocollaboration.entity.TodoItem;
import java.util.List;

public interface TodoItemService {
    
    TodoItem createTodoItem(TodoItem todoItem);
    TodoItem getTodoItemById(Long id);
    List<TodoItem> getTodoItemsByListId(Long listId);
    TodoItem updateTodoItem(Long id, TodoItem todoItem);
    void deleteTodoItem(Long id);
    TodoItem updateTodoItemStatus(Long id, String status);
    List<TodoItem> getTodoItemsByCreatedBy(Long userId);
    long countTodoItemsByListId(Long listId);

}