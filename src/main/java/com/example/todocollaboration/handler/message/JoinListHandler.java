package com.example.todocollaboration.handler.message;

import com.example.todocollaboration.entity.MessageTypeConstant;
import com.example.todocollaboration.entity.OnlineStatus;
import com.example.todocollaboration.handler.HandlerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
/**
 * 处理用户加入列表的消息
 */
@Component
public class JoinListHandler extends BaseMessageHandler {

    @Override
    public void handleMessage(Long userId, Map<String, Object> message) throws Exception {
        Long listId = Long.valueOf(message.get("listId").toString());

        // 检查用户是否有权访问该列表
        if (!permissionService.hasPermission(userId, listId, "view")) {
            return;
        }

        // 更新用户在线状态到Redis
        OnlineStatus onlineStatus = new OnlineStatus();
        onlineStatus.setUserId(userId);
        onlineStatus.setListId(listId);
        onlineStatus.setIsOnline(true);
        onlineStatus.setLastSeen(new Date());
        onlineStatus.setSessionId(userId.toString());

        redisTemplate.opsForHash().putAll("online_status:" + userId, objectMapper.convertValue(onlineStatus, Map.class));

        // 向列表的其他用户广播加入事件
        broadcastToList(listId, userId, MessageTypeConstant.TYPE_USER_JOIN, Map.of(
                "userId", userId,
                "listId", listId,
                "timestamp", new Date()
        ));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerFactory.register(MessageTypeConstant.TYPE_USER_JOIN, this);
    }
}
