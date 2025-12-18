package com.example.todocollaboration.handler.message;

import com.example.todocollaboration.entity.Permission;
import com.example.todocollaboration.entity.TodoList;
import com.example.todocollaboration.handler.MessageHandler;
import com.example.todocollaboration.handler.WebSocketHandler;
import com.example.todocollaboration.service.PermissionService;
import com.example.todocollaboration.service.TodoItemService;
import com.example.todocollaboration.service.TodoListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public abstract class BaseMessageHandler implements MessageHandler {
    @Autowired
    protected TodoListService todoListService;
    @Autowired
    protected TodoItemService todoItemService;
    @Autowired
    protected PermissionService permissionService;
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;
    @Autowired
    protected KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    protected ObjectMapper objectMapper;

    // 向列表中的所有用户广播消息
    protected void broadcastToList(Long listId, Long excludeUserId, String type, Map<String, Object> data) throws Exception {
        // 获取列表的所有协作用户
        List<Permission> permissions = permissionService.getPermissionsByList(listId);

        // 构建消息
        Map<String, Object> message = new HashMap<>();
        message.put("type", type);
        message.put("data", data);

        String messageJson = objectMapper.writeValueAsString(message);

        // 发送消息给所有有权限的用户
        for (Permission permission : permissions) {
            Long userId = permission.getUserId();
            if (!userId.equals(excludeUserId)) {
                sendMessageToUser(userId, messageJson);
            }
        }
        // 发布事件到Kafka，用于持久化或其他服务处理
        kafkaTemplate.send("todo-events", messageJson);
    }

    protected void sendMessageToUser(Long userId, String message) {
        WebSocketSession session = WebSocketHandler.SESSIONS.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("发送消息给用户 {} 失败", userId, e);
                // 移除无效会话
                WebSocketHandler.SESSIONS.remove(userId);
            }
        }
    }
}
