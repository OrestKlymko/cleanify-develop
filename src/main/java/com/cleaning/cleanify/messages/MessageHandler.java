package com.cleaning.cleanify.messages;

import com.cleaning.cleanify.messages.dto.MessageResponse;
import com.cleaning.cleanify.messages.dto.MessageSend;
import com.cleaning.cleanify.messages.model.Message;
import com.cleaning.cleanify.messages.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Component
public class MessageHandler extends TextWebSocketHandler {
	private final ObjectMapper objectMapper;
	private final MessageService messageService;
	private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

	private final Map<String, Set<WebSocketSession>> conversationSessions = new HashMap<>();

	public MessageHandler(ObjectMapper objectMapper, MessageService messageService) {
		this.objectMapper = objectMapper;
		this.messageService = messageService;
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		Map<String, Object> payloadMap = objectMapper.readValue(message.getPayload(), Map.class);

		String type = (String) payloadMap.get("type");

		if ("SUBSCRIBE".equals(type)) {
			String conversationId = (String) payloadMap.get("conversationId");
			conversationSessions.computeIfAbsent(conversationId, k -> new HashSet<>()).add(session);
			session.getAttributes().put("conversationId", conversationId);
			logger.info("Сесія " + session.getId() + " підписалася на розмову " + conversationId);
		} else if ("MESSAGE".equals(type)) {
			MessageSend messageSend = objectMapper.convertValue(payloadMap, MessageSend.class);
			Message saveMessage = messageService.saveMessage(messageSend);
			MessageResponse messageResponse = new MessageResponse(
					saveMessage.getId(),
					saveMessage.getSender().getEmail(),
					saveMessage.getContent(),
					saveMessage.getCreatedAt(),
					saveMessage.getSender().getFirstName(),
					saveMessage.getConversationId()
			);

			String conversationId = saveMessage.getConversationId();
			Set<WebSocketSession> sessions = conversationSessions.getOrDefault(conversationId, Collections.emptySet());
			for (WebSocketSession s : sessions) {
				if (s.isOpen()) {
					s.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageResponse)));
				}
			}
		} else {
			logger.warn("Невідомий тип повідомлення: " + type);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String conversationId = (String) session.getAttributes().get("conversationId");
		if (conversationId != null) {
			Set<WebSocketSession> sessions = conversationSessions.get(conversationId);
			if (sessions != null) {
				sessions.remove(session);
				if (sessions.isEmpty()) {
					conversationSessions.remove(conversationId);
				}
			}
		}
		logger.info("Сесія закрита: " + session.getId());
	}
}
