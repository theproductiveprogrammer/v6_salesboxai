package workflow.engine.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface ActivityProducer {
    @Topic("sb-activity")
    public void event(@KafkaKey Long tenantId, SBActivity activity);
}