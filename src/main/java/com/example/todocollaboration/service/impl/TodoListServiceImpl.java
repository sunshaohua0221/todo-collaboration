package com.example.todocollaboration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.todocollaboration.entity.TodoList;
import com.example.todocollaboration.mapper.TodoListMapper;
import com.example.todocollaboration.mapper.PermissionMapper;
import com.example.todocollaboration.service.TodoListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TodoListServiceImpl implements TodoListService {

    private final TodoListMapper todoListMapper;

    @Override
    @Transactional
    public TodoList createTodoList(TodoList todoList) {
        Date now = new Date();
        todoList.setCreatedAt(now);
        todoList.setUpdatedAt(now);
        todoList.setLastActivity(now);

        todoListMapper.insert(todoList);
        return todoList;
    }

    @Override
    public TodoList getTodoListById(Long id) {
        return todoListMapper.selectById(id);
    }

    @Override
    public Page<TodoList> getTodoLists(Map<String, Object> map) {
        Page<TodoList> page = new Page<>((Integer) map.get("pageNo"), (Integer) map.get("pageSize"));
        QueryWrapper<TodoList> queryWrapper = new QueryWrapper<>();
        if (map.get("title") != null) {
            queryWrapper.eq("title", map.get("title"));
        }
        if (map.get("status") != null) {
            queryWrapper.eq("status", map.get("status"));
        }
        if (map.get("priority") != null) {
            queryWrapper.eq("priority", map.get("priority"));
        }
        if (map.get("dueDate") != null) {
            queryWrapper.eq("due_date", map.get("dueDate"));
        }
        if (map.get("ListIds") != null) {
            queryWrapper.in("id", map.get("ListIds"));
        }
        if (map.get("sort") != null && map.get("orderBy") != null) {
            if (map.get("sort").equals("desc")) {
                queryWrapper.orderByDesc((String) map.get("orderBy"));
            } else {
                queryWrapper.orderByAsc((String) map.get("orderBy"));
            }
        }
        return todoListMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public TodoList updateTodoList(Long id, TodoList todoList) {
        TodoList existingList = todoListMapper.selectById(id);
        if (existingList != null) {
            existingList.setTitle(todoList.getTitle());
            existingList.setDescription(todoList.getDescription());
            existingList.setVisibility(todoList.getVisibility());
            existingList.setStatus(todoList.getStatus());
            existingList.setPriority(todoList.getPriority());
            existingList.setDueDate(todoList.getDueDate());
            existingList.setUpdatedAt(new Date());
            existingList.setLastActivity(new Date());

            todoListMapper.updateById(existingList);
            return existingList;
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteTodoList(Long id) {
        // 这里应该添加级联删除逻辑，删除相关的TODO项和权限
        // 为了简化，只删除TODO列表本身
        todoListMapper.deleteById(id);
    }


}