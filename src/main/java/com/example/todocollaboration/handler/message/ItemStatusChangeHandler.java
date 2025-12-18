package com.example.todocollaboration.handler.message;

import com.example.todocollaboration.entity.MessageTypeConstant;
import com.example.todocollaboration.entity.TodoItem;
import com.example.todocollaboration.handler.HandlerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 处理TODO项状态变更
 */
@Component
public class ItemStatusChangeHandler extends BaseMessageHandler {

    @Override
    public void handleMessage(Long userId, Map<String, Object> message) throws Exception {
        Long itemId = Long.valueOf(message.get("itemId").toString());
        String status = (String) message.get("status");

        // 验证用户权限并更新状态
        TodoItem todoItem = todoItemService.getTodoItemById(itemId);

        if (todoItem != null && permissionService.hasPermission(userId, todoItem.getListId(), "edit")) {
            // 更新TODO项状态
            TodoItem updatedItem = todoItemService.updateTodoItemStatus(itemId, status);

            if (updatedItem != null) {
                // 广播状态变更事件到列表
                broadcastToList(updatedItem.getListId(), userId, MessageTypeConstant.TYPE_ITEM_STATUS_CHANGE, Map.of(
                        "itemId", itemId,
                        "status", status,
                        "lastModifiedBy", userId,
                        "timestamp", new Date()
                ));
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerFactory.register(MessageTypeConstant.TYPE_ITEM_STATUS_CHANGE, this);
    }


}
