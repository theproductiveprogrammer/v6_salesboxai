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

public class SendAdaptive extends EventHandlerBase {

    private boolean proceed;

    public SendAdaptive(ActivityProducer activityProducer, ConversationProducer conversationProducer) {
        super(activityProducer, conversationProducer);
        proceed = false;
    }

    @Override
    protected void setActivityDetails(SBEvent event, WorkflowStep step, SBActivity activity) {
        activity.desc = "Sending adaptive email to lead " + event.id + " on " + new Date();
    }

    @Override
    protected void setConversationDetails(SBEvent event, WorkflowStep step, SBConversation conversation) {
        Random random = new Random();
        conversation.incoming = false;
        conversation.message = "EMAIL: " + Constants.MSGS[random.nextInt(Constants.MSGS.length)];
        if(random.nextInt(5) < 2) proceed = true;
    }

    @Override
    protected NextStep getNextStep(SBEvent event, WorkflowStep step) {
        if(proceed) return super.getNextStep(event, step);
        else return new NextStep(step.num, 200);
    }
}
