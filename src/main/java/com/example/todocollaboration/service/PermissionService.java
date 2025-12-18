package com.example.todocollaboration.service;

import com.example.todocollaboration.entity.Permission;
import java.util.List;

public interface PermissionService {
    
    Permission grantPermission(Permission permission);
    List<Permission> getPermissionsByUser(Long userId);
    List<Permission> getPermissionsByList(Long listId);
    boolean hasPermission(Long userId, Long listId, String permissionType);

}