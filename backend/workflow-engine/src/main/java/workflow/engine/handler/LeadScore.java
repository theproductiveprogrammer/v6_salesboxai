package workflow.engine.handler;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.uri.UriBuilder;
import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.activities.Getter;
import workflow.engine.kafka.SBActivity;
import workflow.engine.workflows.Constants;

import java.net.URI;

public class LeadScore implements IEventHandler {

    private HandlerIOC handlerIOC;

    public LeadScore(HandlerIOC handlerIOC) {
        this.handlerIOC = handlerIOC;
    }

    @Override
    public NextStep handle(SBEvent event, WorkflowStep step) {
        URI uri = UriBuilder.of("/internal/leadscore")
                .queryParam("tenantId", event.tenantId)
                .queryParam("leadId", event.id)
                .build();
        handlerIOC.scoringClient.retrieve(HttpRequest.GET(uri));

        SBActivity activity = new SBActivity();
        activity.whatEntity = Constants.ACTIVITY_ENTITY_LEAD;
        activity.whatId = event.id;
        activity.desc = "Score updated for lead " + event.id;
        handlerIOC.activityProducer.event(event.tenantId, activity);

        if(step.links == null || step.links.length == 0) return null;
        else return new NextStep(step.links[0], 100);
    }
}
