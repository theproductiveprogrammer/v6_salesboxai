package workflow.engine.handler;

import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.kafka.ActivityProducer;
import workflow.engine.kafka.ConversationProducer;
import workflow.engine.kafka.SBActivity;
import workflow.engine.kafka.SBConversation;
import workflow.engine.workflows.Constants;

import java.util.Date;

public class SendToSalesForce implements IEventHandler {

    private final ActivityProducer activityProducer;

    public SendToSalesForce(ActivityProducer activityProducer, ConversationProducer conversationProducer) {
        this.activityProducer = activityProducer;
    }

    @Override
    public NextStep handle(SBEvent event, WorkflowStep step) {
        SBActivity activity = new SBActivity();
        activity.whatEntity = Constants.ACTIVITY_ENTITY_LEAD;
        activity.whatId = event.id;
        activity.desc = "Sent lead " + event.id + " to Salesforce on " + new Date();
        activityProducer.event(event.tenantId, activity);
        if(step.links == null || step.links.length == 0) return null;
        else return new NextStep(step.links[0], 100);
    }
}
