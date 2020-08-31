package activities;

import activities.db.Activity;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;
import java.util.List;

@Controller
public class ActivityServer {

    @Inject
    ActivityRepository activityRepository;

    @Get("/activities")
    public List<Activity> getActivities(Long leadId) {
        return activityRepository.findByWhatId(leadId);
    }
}
