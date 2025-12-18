package com.example.todocollaboration.handler.message;

import com.example.todocollaboration.entity.MessageTypeConstant;
import com.example.todocollaboration.entity.Permission;
import com.example.todocollaboration.handler.HandlerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 处理TODO项授权
 */
@Component
public class TodoAuthorizeHandler extends BaseMessageHandler {

    @Override
    public void handleMessage(Long userId, Map<String, Object> message) throws Exception {
        Map<String, Object> data = (Map<String, Object>) message.get("data");

        Permission permission = convertToPermission(data);
        permissionService.grantPermission(permission);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerFactory.register(MessageTypeConstant.TYPE_TODO_AUTHORIZE, this);
    }

    private Permission convertToPermission(Map<String, Object> data) {
        Permission permission = new Permission();
        permission.setListId(Long.parseLong((String)data.get("listId")));
        permission.setUserId(Long.parseLong((String)data.get("userId")));
        permission.setPermissionType((String) data.get("permissionType"));
        return permission;
    }
}
