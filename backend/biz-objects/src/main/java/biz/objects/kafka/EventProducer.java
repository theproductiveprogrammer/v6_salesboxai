package biz.objects.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface EventProducer {
    @Topic("sb-event")
    public void event(@KafkaKey Long tenantId, SBEvent event);
}