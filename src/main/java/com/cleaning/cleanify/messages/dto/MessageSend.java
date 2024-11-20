package com.cleaning.cleanify.messages.dto;

import java.time.LocalDateTime;

public record MessageSend(
		 String content,
		 LocalDateTime createdAt,
		 String senderEmail,
		 String conversationId
) {
}
