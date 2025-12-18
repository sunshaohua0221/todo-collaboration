package com.example.todocollaboration.handler;

import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

public interface MessageHandler extends InitializingBean {
    void handleMessage(Long userId, Map<String, Object> message) throws Exception;
}
