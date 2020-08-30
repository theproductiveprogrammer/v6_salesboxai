package workflow.engine.handler;

import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.kafka.ActivityProducer;
import workflow.engine.kafka.ConversationProducer;

public class EventStart implements IEventHandler {

    public EventStart(ActivityProducer activityProducer, ConversationProducer conversationProducer) { }

    @Override
    public NextStep handle(SBEvent event, WorkflowStep step) {
        if(step.links != null && step.links.length > 0) return new NextStep(step.links[0], 100);
        else return null;
    }
}
