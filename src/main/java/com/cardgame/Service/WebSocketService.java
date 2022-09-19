package com.cardgame.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public  class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
//    protected abstract String getEntityTopic();

    @Autowired
    public WebSocketService(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(final String topicSuffix) {
        messagingTemplate.convertAndSend("/topic/" + topicSuffix, "Default message from our WS service");
    }

    public void sendMessage(final String topicSuffix, final String payload) {
        messagingTemplate.convertAndSend("/topic/" + topicSuffix, payload);
    }

    public void notifyFrontend() {
//        final String entityTopic = getEntityTopic();
        final String entityTopic = getEntityTopic();
        if (entityTopic == null) {
            log.error("Failed to get entity topic");
            return;
        }

        this.sendMessage(entityTopic);
    }
    protected String getEntityTopic() {
        return "vehicle";
    }
}