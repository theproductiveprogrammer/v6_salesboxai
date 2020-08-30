package workflow.engine.handler;

import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.kafka.ActivityProducer;
import workflow.engine.kafka.ConversationProducer;
import workflow.engine.kafka.SBActivity;
import workflow.engine.kafka.SBConversation;
import workflow.engine.workflows.Constants;

public abstract class EventHandlerBase implements IEventHandler {

    private ActivityProducer activityProducer;
    private ConversationProducer conversationProducer;

    public EventHandlerBase(ActivityProducer activityProducer, ConversationProducer conversationProducer) {
        this.activityProducer = activityProducer;
        this.conversationProducer = conversationProducer;
    }

    @Override
    public NextStep handle(SBEvent event, WorkflowStep step) {
        sendActivity(event, step);
        sendConversation(event, step);
        return getNextStep(event, step);
    }

    private void sendActivity(SBEvent event, WorkflowStep step) {
        SBActivity activity = new SBActivity();
        activity.whatEntity = Constants.ACTIVITY_ENTITY_LEAD;
        activity.whatId = event.id;
        setActivityDetails(event, step, activity);
        activityProducer.event(event.tenantId, activity);
    }

    private void sendConversation(SBEvent event, WorkflowStep step) {
        SBConversation conversation = new SBConversation();
        conversation.leadId = event.id;
        setConversationDetails(event, step, conversation);
        conversationProducer.event(event.tenantId, conversation);
    }

    protected abstract void setActivityDetails(SBEvent event, WorkflowStep step, SBActivity activity);
    protected abstract void setConversationDetails(SBEvent event, WorkflowStep step, SBConversation conversation);

    protected NextStep getNextStep(SBEvent event, WorkflowStep step) {
        if(step.links != null && step.links.length > 0) return new NextStep(step.links[0], 100);
        else return null;
    }
}
