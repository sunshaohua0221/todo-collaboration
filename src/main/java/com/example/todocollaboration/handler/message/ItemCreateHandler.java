package com.example.todocollaboration.handler.message;

import com.example.todocollaboration.entity.MessageTypeConstant;
import com.example.todocollaboration.entity.Permission;
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
 * 处理TODO项创建
 */
@Component
public class ItemCreateHandler extends BaseMessageHandler {

    @Override
    public void handleMessage(Long userId, Map<String, Object> message) throws Exception {
        Map<String, Object> data = (Map<String, Object>) message.get("data");

        // 创建TODO项
        TodoList newTodo = convertToTodoList(data, userId);
        TodoList createdTodo = todoListService.createTodoList(newTodo);

        // 增加权限
        Permission permission = new Permission();
        permission.setListId(createdTodo.getId());
        permission.setUserId(userId);
        permission.setPermissionType("edit");
        permissionService.grantPermission(permission);
        sendMessageToUser(userId, "TODO创建成功");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        HandlerFactory.register(MessageTypeConstant.TYPE_TODO_CREATE, this);
    }

    private TodoList convertToTodoList(Map<String, Object> data, Long userId) {
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
        todoList.setOwnerId(userId);
        return todoList;
    }
}
