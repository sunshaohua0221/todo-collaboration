package com.example.todocollaboration.handler.message;

import com.example.todocollaboration.entity.MessageTypeConstant;
import com.example.todocollaboration.entity.TodoList;
import com.example.todocollaboration.handler.HandlerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 处理TODO项删除
 */
@Component
public class ItemDeleteHandler extends BaseMessageHandler {

    @Override
    public void handleMessage(Long userId, Map<String, Object> message) throws Exception {
        Map<String, Object> data = (Map<String, Object>) message.get("data");
        Long listId = Long.parseLong((String) data.get("id"));

        TodoList todoList = todoListService.getTodoListById(listId);

        if (todoList != null) {
            boolean hasPermission = permissionService.hasPermission(userId, listId, "edit");
            if (!hasPermission) {
                sendMessageToUser(userId, "您没有权限删除");
                return;
            }
            // 删除TODO项
            todoListService.deleteTodoList(listId);

            // 广播删除事件到列表
            broadcastToList(listId, userId, MessageTypeConstant.TYPE_TODO_DELETE, Map.of(
                    "itemId", listId,
                    "timestamp", new Date()
            ));
        } else {
            sendMessageToUser(userId, "TODO不存在");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerFactory.register(MessageTypeConstant.TYPE_TODO_DELETE, this);
    }

}
