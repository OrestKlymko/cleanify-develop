package com.cleaning.cleanify.messages.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record HistoryMessageResponse(
		 String content,
		 String sender,
		 String email,
		 String conversationId,
		 LocalDateTime createdAt,
		 UUID id
) {
}
