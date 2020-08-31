package event.server;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface EventProducer {
    @Topic("sb-event")
    public void newEvent(@KafkaKey Long tenantId, SBEvent event);
}