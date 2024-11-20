package com.cleaning.cleanify.messages.config;

import com.cleaning.cleanify.messages.MessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class MessageConfig implements WebSocketConfigurer {
	private final MessageHandler messageHandler;

	public MessageConfig(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(messageHandler, "/ws").setAllowedOriginPatterns("*");
	}
}

