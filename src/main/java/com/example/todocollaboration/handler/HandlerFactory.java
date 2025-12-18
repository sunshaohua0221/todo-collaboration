package com.example.todocollaboration.handler;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerFactory {
    private static final Map<String, MessageHandler> HANDLER_MAP = new ConcurrentHashMap<>();

    public static MessageHandler getHandler(String type) {
        return HANDLER_MAP.get(type);
    }

    public static void register(String type,MessageHandler messageHandler){
        HANDLER_MAP.put(type,messageHandler);
    }
}
