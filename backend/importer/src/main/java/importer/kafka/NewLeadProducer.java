package importer.kafka;

import importer.csv.Lead;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface NewLeadProducer {
    @Topic("import-lead")
    public void leadImportMessage(@KafkaKey Long tenantId, Lead lead);
}