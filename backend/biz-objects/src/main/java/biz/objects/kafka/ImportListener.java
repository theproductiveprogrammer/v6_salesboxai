package biz.objects.kafka;

import biz.objects.db.Lead;
import biz.objects.repo.LeadRepository;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@KafkaListener(offsetReset = OffsetReset.LATEST)
public class ImportListener {
    private static final Logger logger = LoggerFactory.getLogger(ImportListener.class);

    @Inject
    LeadRepository leadRepository;

    @Topic("import-lead")
    public void onImportLead(@KafkaKey Long tenantId, Lead lead) {
        logger.info("Got lead " + lead.getFirstName() + "(" + lead.getEmail() + ") for tenant " + tenantId);
        lead.setTenantId(tenantId);
        leadRepository.save(lead);
    }
}