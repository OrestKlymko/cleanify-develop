package com.cleaning.cleanify.messages.repository;

import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.messages.dto.ConversationDetailResponse;
import com.cleaning.cleanify.messages.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
	List<Message> findMessagesByConversationIdOrderByCreatedAtAsc(String conversationId);

	@Query(value = """
			SELECT DISTINCT m.conversation_id, u.FIRST_NAME, u.EMAIL FROM messages m
                                          LEFT JOIN users u ON m.sender_id = u.id
                                          WHERE u.EMAIL !='cleanifybee@gmail.com'
			""", nativeQuery = true)
	List<ConversationDetailResponse> findAllConversationIdForAdmin();

}
