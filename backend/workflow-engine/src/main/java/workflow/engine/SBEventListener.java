package workflow.engine;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaListener(offsetReset = OffsetReset.LATEST)
public class SBEventListener {
    public static final Logger logger = LoggerFactory.getLogger(SBEventListener.class);

    @Topic("sb-event")
    public void onEvent(@KafkaKey Long tenantId, SBEvent event) {
        logger.info("Got new event " + event.type + "." + event.id + " for tenant " + tenantId);
    }
}