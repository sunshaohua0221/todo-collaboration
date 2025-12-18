package com.example.todocollaboration.service.impl;

import com.example.todocollaboration.entity.Permission;
import com.example.todocollaboration.entity.TodoList;
import com.example.todocollaboration.mapper.PermissionMapper;
import com.example.todocollaboration.mapper.TodoListMapper;
import com.example.todocollaboration.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final TodoListMapper todoListMapper;

    @Override
    @Transactional
    public Permission grantPermission(Permission permission) {
        // 检查列表是否存在
        if (todoListMapper.selectById(permission.getListId()) == null) {
            return null;
        }
        
        // 检查是否已存在相同的权限记录
        Permission existingPermission = 
            permissionMapper.findByListIdAndUserId(permission.getListId(), permission.getUserId());
            
        if (existingPermission != null) {
            // 更新权限类型
            existingPermission.setPermissionType(permission.getPermissionType());
            existingPermission.setUpdatedAt(new Date());
            permissionMapper.updateById(existingPermission);
            return existingPermission;
        } else {
            // 创建新权限
            Date now = new Date();
            permission.setCreatedAt(now);
            permission.setUpdatedAt(now);
            permissionMapper.insert(permission);
            return permission;
        }
    }


    @Override
    public List<Permission> getPermissionsByUser(Long userId) {
        return permissionMapper.findByUserId(userId);
    }

    @Override
    public List<Permission> getPermissionsByList(Long listId) {
        return permissionMapper.findByListId(listId);
    }

    @Override
    public boolean hasPermission(Long userId, Long listId, String permissionType) {
        // 检查用户是否是列表所有者
        TodoList todoList = todoListMapper.selectById(listId);
        if (todoList != null && todoList.getOwnerId().equals(userId)) {
            return true;
        }
        
        // 检查用户是否有相应的权限
        Permission permission = permissionMapper.findByListIdAndUserId(listId, userId);
        if (permission != null) {
            return "owner".equals(permission.getPermissionType()) || 
                   permissionType.equals(permission.getPermissionType());
        }
        
        return false;
    }

}