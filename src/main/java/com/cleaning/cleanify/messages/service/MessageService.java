package com.cleaning.cleanify.messages.service;

import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.auth.service.UserService;
import com.cleaning.cleanify.messages.dto.ConversationDetailResponse;
import com.cleaning.cleanify.messages.dto.HistoryMessageResponse;
import com.cleaning.cleanify.messages.dto.MessageSend;
import com.cleaning.cleanify.messages.model.Message;
import com.cleaning.cleanify.messages.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {
	private final MessageRepository messageRepository;
	private final UserService userService;

	public MessageService(MessageRepository messageRepository, UserService userService) {
		this.messageRepository = messageRepository;
		this.userService = userService;
	}

	@Transactional
	public Message saveMessage(MessageSend messageSend) {
		User user = userService.getUserByEmail(messageSend.senderEmail());
		Message message = new Message();
		message.setConversationId(messageSend.conversationId());
		message.setSender(user);
		message.setContent(messageSend.content());
		message.setCreatedAt(LocalDateTime.now());
		message.setRead(false);
		return messageRepository.save(message);
	}

	public List<HistoryMessageResponse> getAllMessagesByConversationId(String conversationId) {
		List<Message> messages = messageRepository.findMessagesByConversationIdOrderByCreatedAtAsc(conversationId);
		return messages.stream()
				.map(message -> new HistoryMessageResponse(
						message.getContent(),
						message.getSender().getFirstName(),
						message.getSender().getEmail(),
						message.getConversationId(),
						message.getCreatedAt(),
						message.getId()
				))
				.toList();
	}

	public List<ConversationDetailResponse> getAllConversations() {
		return messageRepository.findAllConversationIdForAdmin();
	}

	public String getConversationId() {
		User user = userService.getAuthenticatedUser();

		if(user.getMessages().isEmpty()){
		 			return UUID.randomUUID().toString();
		} else {
			Message message = user.getMessages().get(0);
			return message.getConversationId();
		}

	}
}
