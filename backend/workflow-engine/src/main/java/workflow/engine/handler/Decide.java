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
import java.util.Random;

public class Decide implements IEventHandler {

    public Decide(HandlerIOC handlerIOC) { }

    @Override
    public NextStep handle(SBEvent event, WorkflowStep step) {
        if(step.links == null || step.links.length == 0) return null;
        return new NextStep(step.links[new Random().nextInt(step.links.length)], 100);
    }
}
