package com.example.todocollaboration.service.impl;

import com.example.todocollaboration.entity.TodoItem;
import com.example.todocollaboration.mapper.TodoItemMapper;
import com.example.todocollaboration.mapper.TodoListMapper;
import com.example.todocollaboration.service.TodoItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoItemServiceImpl implements TodoItemService {

    private final TodoItemMapper todoItemMapper;
    private final TodoListMapper todoListMapper;

    @Override
    @Transactional
    public TodoItem createTodoItem(TodoItem todoItem) {
        Date now = new Date();
        todoItem.setCreatedAt(now);
        todoItem.setUpdatedAt(now);
        
        todoItemMapper.insert(todoItem);
        // 更新TODO列表的项目数和最后活动时间
        updateTodoListStats(todoItem.getListId());
        
        return todoItem;
    }

    @Override
    public TodoItem getTodoItemById(Long id) {
        return todoItemMapper.selectById(id);
    }

    @Override
    public List<TodoItem> getTodoItemsByListId(Long listId) {
        return todoItemMapper.findByListId(listId);
    }

    @Override
    @Transactional
    public TodoItem updateTodoItem(Long id, TodoItem todoItem) {
        TodoItem existingItem = todoItemMapper.selectById(id);
        if (existingItem != null) {
            existingItem.setContent(todoItem.getContent());
            existingItem.setDueDate(todoItem.getDueDate());
            existingItem.setPriority(todoItem.getPriority());
            existingItem.setStatus(todoItem.getStatus());
            existingItem.setMedia(todoItem.getMedia());
            existingItem.setUpdatedAt(new Date());
            
            todoItemMapper.updateById(existingItem);
            
            // 更新TODO列表的最后活动时间
            updateTodoListLastActivity(existingItem.getListId());
            
            return existingItem;
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteTodoItem(Long id) {
        TodoItem todoItem = todoItemMapper.selectById(id);
        if (todoItem != null) {
            Long listId = todoItem.getListId();
            todoItemMapper.deleteById(id);
            
            // 更新TODO列表的项目数和最后活动时间
            updateTodoListStats(listId);
        }
    }

    @Override
    @Transactional
    public TodoItem updateTodoItemStatus(Long id, String status) {
        TodoItem todoItem = todoItemMapper.selectById(id);
        if (todoItem != null) {
            todoItem.setStatus(status);
            todoItem.setUpdatedAt(new Date());
            
            todoItemMapper.updateById(todoItem);
            
            // 更新TODO列表的最后活动时间
            updateTodoListLastActivity(todoItem.getListId());
            
            return todoItem;
        }
        return null;
    }

    @Override
    public List<TodoItem> getTodoItemsByCreatedBy(Long userId) {
        return todoItemMapper.findByListId(userId);
    }

    @Override
    public long countTodoItemsByListId(Long listId) {
        return todoItemMapper.countByListId(listId);
    }

    // 更新TODO列表的项目数和最后活动时间
    private void updateTodoListStats(Long listId) {
        long itemsCount = todoItemMapper.countByListId(listId);
        todoListMapper.updateItemsCountAndLastActivity(listId, itemsCount, new Date());
    }

    // 更新TODO列表的最后活动时间
    private void updateTodoListLastActivity(Long listId) {
        todoListMapper.updateLastActivity(listId, new Date());
    }

}