package workflow.engine.handler;

import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.kafka.ActivityProducer;
import workflow.engine.kafka.ConversationProducer;
import workflow.engine.kafka.SBActivity;
import workflow.engine.workflows.Constants;

import java.util.Date;

public class AddToList implements IEventHandler {

    private final HandlerIOC handlerIOC;

    public AddToList(HandlerIOC handlerIOC) {
        this.handlerIOC = handlerIOC;
    }

    @Override
    public NextStep handle(SBEvent event, WorkflowStep step) {
        SBActivity activity = new SBActivity();
        activity.whatEntity = Constants.ACTIVITY_ENTITY_LEAD;
        activity.whatId = event.id;
        activity.desc = "Added lead " + event.id + " to list on " + new Date();
        handlerIOC.activityProducer.event(event.tenantId, activity);
        if(step.links == null || step.links.length == 0) return null;
        else return new NextStep(step.links[0], 100);
    }
}
