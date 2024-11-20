package com.cleaning.cleanify.messages.controller;

import com.cleaning.cleanify.messages.dto.ConversationDetailResponse;
import com.cleaning.cleanify.messages.dto.HistoryMessageResponse;
import com.cleaning.cleanify.messages.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping
	public String getConversationId() {
		return messageService.getConversationId();
	}

	@GetMapping("/get-all-conversations")
	public List<ConversationDetailResponse> getAllConversations() {
		return messageService.getAllConversations();
	}



	@GetMapping("/{conversationId}")
	public List<HistoryMessageResponse> getAllMessagesByConversationId(@PathVariable String conversationId) {
		return messageService.getAllMessagesByConversationId(conversationId);
	}
}
