package com.example.todocollaboration.handler.message;

import com.example.todocollaboration.entity.MessageTypeConstant;
import com.example.todocollaboration.entity.TodoList;
import com.example.todocollaboration.handler.HandlerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 处理用户停止输入
 */
@Component
public class TypingStopHandler extends BaseMessageHandler {

    @Override
    public void handleMessage(Long userId, Map<String, Object> message) throws Exception {
        Long listId = Long.valueOf(message.get("listId").toString());

        TodoList todoList = todoListService.getTodoListById(listId);
        if (todoList == null) {
            sendMessageToUser(userId, "列表不存在");
        }

        // 广播输入状态到列表
        broadcastToList(listId, userId, MessageTypeConstant.TYPE_USER_TYPING_STOP, Map.of(
                "userId", userId,
                "listId", listId,
                "timestamp", new Date()
        ));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerFactory.register(MessageTypeConstant.TYPE_USER_TYPING_STOP, this);
    }


}
