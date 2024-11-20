package com.cleaning.cleanify.messages.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponse(
		UUID id,
		String email,
		String content,
		LocalDateTime createdAt,
		String sender,
		String conversationId
) {
}
