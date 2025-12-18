package com.example.todocollaboration.listener;

import com.example.todocollaboration.entity.MessageTypeConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Slf4j
@Component
public class KafkaEventListener implements MessageListener<String, String> {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        try {
            String message = record.value();
            log.info("从Kafka收到消息: {}", message);

            // 解析消息
            Map<String, Object> eventData = objectMapper.readValue(message, Map.class);
            String eventType = (String) eventData.get("type");
            Map<String, Object> data = (Map<String, Object>) eventData.get("data");

            handleEvent(eventType, data, eventData);
        } catch (Exception e) {
            log.error("处理Kafka消息失败", e);
        }
    }


    private void handleEvent(String eventType, Map<String, Object> data, Map<String, Object> eventData) {
        switch (eventType) {
            case MessageTypeConstant.TYPE_USER_ONLINE:
            case MessageTypeConstant.TYPE_USER_OFFLINE:
                messagingTemplate.convertAndSend("/topic/user_events", eventData);
                break;

            case MessageTypeConstant.TYPE_TODO_UPDATE:
                sendToListTopic(data, "listId", eventData); // 假设正确的字段是 listId
                break;

            case MessageTypeConstant.TYPE_TODO_DELETE:
            case MessageTypeConstant.TYPE_ITEM_STATUS_CHANGE:
                sendToListTopic(data, "itemId", eventData);
                break;

            case MessageTypeConstant.TYPE_USER_JOIN:
            case MessageTypeConstant.TYPE_USER_LEAVE:
                sendToListTopic(data, "listId", eventData);
                break;

            default:
                log.warn("未知事件类型: {}", eventType);
        }
    }


    private void sendToListTopic(Map<String, Object> data, String idField, Map<String, Object> eventData) {
        if (data == null || data.get(idField) == null) {
            log.warn("数据中缺少 {} 字段", idField);
            return;
        }

        try {
            long listId = Long.parseLong(data.get(idField).toString());
            messagingTemplate.convertAndSend("/topic/list/" + listId, eventData);
        } catch (NumberFormatException e) {
            log.error("无法解析 {} 为 Long 类型: {}", idField, data.get(idField), e);
        }
    }

}