package conversations;

import conversations.db.Conversation;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;

import javax.inject.Inject;

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
public class ConversationListener {

    @Inject
    ConversationRepository conversationRepository;

    @Topic("sb-conversation")
    public void newConversation(@KafkaKey Long tenantId, Conversation conversation) {
        conversation.setTenantId(tenantId);
        conversationRepository.save(conversation);
    }
}