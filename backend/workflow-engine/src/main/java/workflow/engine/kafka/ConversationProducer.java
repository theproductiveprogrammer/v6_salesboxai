package workflow.engine.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface ConversationProducer {
    @Topic("sb-conversation")
    public void event(@KafkaKey Long tenantId, SBConversation conversation);
}