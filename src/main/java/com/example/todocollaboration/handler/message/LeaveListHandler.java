package com.example.todocollaboration.handler.message;

import com.example.todocollaboration.entity.MessageTypeConstant;
import com.example.todocollaboration.entity.TodoList;
import com.example.todocollaboration.handler.HandlerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
/**
 * 处理用户离开TODO列表的消息
 */
@Component
public class LeaveListHandler extends BaseMessageHandler{
    @Override
    public void handleMessage(Long userId, Map<String, Object> message) throws Exception {
        Long listId = Long.valueOf(message.get("listId").toString());

        TodoList todoList = todoListService.getTodoListById(listId);
        if (todoList == null) {
            sendMessageToUser(userId, "列表不存在");
        }

        // 向列表的其他用户广播离开事件
        broadcastToList(listId, userId, MessageTypeConstant.TYPE_USER_LEAVE, Map.of(
                "userId", userId,
                "timestamp", new Date()
        ));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerFactory.register(MessageTypeConstant.TYPE_USER_LEAVE,this);
    }
}
