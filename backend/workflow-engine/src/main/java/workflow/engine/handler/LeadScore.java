package workflow.engine.handler;

import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.uri.UriBuilder;
import io.reactivex.Flowable;
import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.activities.Getter;
import workflow.engine.kafka.SBActivity;
import workflow.engine.workflows.Constants;

import java.net.URI;
import java.util.Random;

public class LeadScore implements IEventHandler {

    private HandlerIOC handlerIOC;

    public LeadScore(HandlerIOC handlerIOC) {
        this.handlerIOC = handlerIOC;
    }

    @Override
    public NextStep handle(SBEvent event, WorkflowStep step) {
        Score score = new Score(event.tenantId, event.id);
        score.score = new Random().nextInt(20) + 2;
        URI uri = UriBuilder.of("/internal/updatescore").build();
        Flowable<HttpResponse<ByteBuffer>> r = handlerIOC.scoringClient.exchange(HttpRequest.POST(uri, score));
        r.blockingFirst(); // TODO: Use async activity

        SBActivity activity = new SBActivity();
        activity.whatEntity = Constants.ACTIVITY_ENTITY_LEAD;
        activity.whatId = event.id;
        activity.desc = "Score updated for lead " + event.id;
        handlerIOC.activityProducer.event(event.tenantId, activity);

        if(step.links == null || step.links.length == 0) return null;
        else return new NextStep(step.links[0], 100);
    }

    private class Score {
        public Long tenantId;
        public Long leadId;
        public int score;

        public Score(Long tenantId, Long leadId) {
            this.tenantId = tenantId;
            this.leadId = leadId;
        }
    }
}
