package activities;

import activities.db.Activity;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.OffsetReset;
import io.micronaut.configuration.kafka.annotation.Topic;

import javax.inject.Inject;

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
public class ActivityListener {

    @Inject
    ActivityRepository activityRepository;

    @Topic("sb-activity")
    public void newActivity(@KafkaKey Long tenantId, ActivityDTO activityDTO) {
        activityRepository.save(new Activity(tenantId, activityDTO));
    }
}