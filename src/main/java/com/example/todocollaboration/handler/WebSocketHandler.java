package com.example.todocollaboration.handler;

import com.example.todocollaboration.entity.MessageTypeConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // 存储活跃的WebSocket会话
    public static final Map<Long, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从会话属性中获取用户ID
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            SESSIONS.put(userId, session);
            log.info("用户 {} 建立WebSocket连接", userId);

            // 发布用户上线事件到Kafka
            Map<String, Object> onlineEvent = new HashMap<>();
            onlineEvent.put("type", MessageTypeConstant.TYPE_USER_ONLINE);
            onlineEvent.put("userId", userId);
            onlineEvent.put("timestamp", new Date());
            kafkaTemplate.send("todo-events", objectMapper.writeValueAsString(onlineEvent));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) {
            return;
        }

        String payload = message.getPayload();
        log.info("收到用户 {} 的消息: {}", userId, payload);

        // 解析消息
        Map<String, Object> messageData = objectMapper.readValue(payload, Map.class);
        String type = (String) messageData.get("type");

        HandlerFactory.getHandler(type).handleMessage(userId, messageData);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            SESSIONS.remove(userId);
            log.info("用户 {} 关闭WebSocket连接", userId);

            // 发布用户下线事件到Kafka
            Map<String, Object> offlineEvent = new HashMap<>();
            offlineEvent.put("type", MessageTypeConstant.TYPE_USER_OFFLINE);
            offlineEvent.put("userId", userId);
            offlineEvent.put("timestamp", new Date());
            kafkaTemplate.send("todo-events", objectMapper.writeValueAsString(offlineEvent));

            // 更新Redis中的用户在线状态
            redisTemplate.delete("online_status:" + userId);
        }
    }

}