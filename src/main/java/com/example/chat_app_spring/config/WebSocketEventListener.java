package com.example.chat_app_spring.config;

import com.example.chat_app_spring.chat.ChatMessage;
import com.example.chat_app_spring.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null){
            log.info("user disconnected: {}",username);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();

//            messagingTemplate.convertAndSendToUser("/topic/public",chatMessage.getSender(),chatMessage);
            messagingTemplate.convertAndSend("/topic/public",chatMessage);
//            messagingTemplate.convertAndSendToUser("/topic/public",chatMessage.getSender(),chatMessage);
        }

    }

}
