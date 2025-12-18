package com.example.todocollaboration.handler.message;

import com.example.todocollaboration.entity.MessageTypeConstant;
import com.example.todocollaboration.entity.TodoList;
import com.example.todocollaboration.handler.HandlerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * 处理TODO项更新
 */
@Component
public class ItemUpdateHandler extends BaseMessageHandler {

    @Override
    public void handleMessage(Long userId, Map<String, Object> message) throws Exception {
        Map<String, Object> data = (Map<String, Object>) message.get("data");
        Long listId = Long.parseLong((String) data.get("id"));
        TodoList todoList = todoListService.getTodoListById(listId);

        if (todoList != null) {
            boolean hasPermission = permissionService.hasPermission(userId, listId, "edit");
            if (!hasPermission) {
                sendMessageToUser(userId, "您没有权限修改");
                return;
            }
            // 更新TODO项
            TodoList updateTodoList = todoListService.updateTodoList(listId, convertToTodoList(data));

            if (updateTodoList != null) {
                // 广播更新事件到列表
                broadcastToList(listId, userId, MessageTypeConstant.TYPE_TODO_UPDATE, Map.of(
                        "item", updateTodoList,
                        "lastModifiedBy", userId,
                        "timestamp", new Date()
                ));
            }
        } else {
            sendMessageToUser(userId, "TODO不存在");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerFactory.register(MessageTypeConstant.TYPE_TODO_UPDATE, this);
    }

    private TodoList convertToTodoList(Map<String, Object> data) {
        TodoList todoList = new TodoList();
        todoList.setTitle((String) data.get("title"));
        todoList.setDescription((String) data.get("description"));
        todoList.setVisibility((String) data.get("visibility"));
        todoList.setStatus((String) data.get("status"));
        todoList.setPriority((Integer) data.get("priority"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(data.get("dueDate").toString(), formatter);
        ZonedDateTime zdt = localDate.atStartOfDay(ZoneId.systemDefault());
        Date dueDate = Date.from(zdt.toInstant());
        todoList.setDueDate(dueDate);
        return todoList;
    }
}
