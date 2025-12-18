package com.example.todocollaboration.handler.message;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.todocollaboration.entity.MessageTypeConstant;
import com.example.todocollaboration.entity.Permission;
import com.example.todocollaboration.entity.TodoList;
import com.example.todocollaboration.handler.HandlerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 处理TODO列表查询
 */
@Component
public class ItemQueryHandler extends BaseMessageHandler {

    @Override
    public void handleMessage(Long userId, Map<String, Object> message) throws Exception {
        Map<String, Object> data = (Map<String, Object>) message.get("data");

        List<Permission> permissionsByUser = permissionService.getPermissionsByUser(userId);
        if (permissionsByUser == null || permissionsByUser.isEmpty()) {
            sendMessageToUser(userId, "没有符合条件的记录");
            return;
        }
        List<Long> listIds = permissionsByUser.stream().map(Permission::getListId).toList();

        data.put("listIds", listIds);
        Page<TodoList> page = todoListService.getTodoLists(data);
        sendMessageToUser(userId, JSON.toJSONString(page));

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerFactory.register(MessageTypeConstant.TYPE_TODO_QUERY, this);
    }

}
