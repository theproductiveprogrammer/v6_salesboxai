package workflow.engine.handler;

import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.kafka.ActivityProducer;
import workflow.engine.kafka.ConversationProducer;
import workflow.engine.kafka.SBActivity;
import workflow.engine.kafka.SBConversation;
import workflow.engine.workflows.Constants;

import java.util.Date;
import java.util.Random;

public class Tweet extends EventHandlerBase {

    public Tweet(ActivityProducer activityProducer, ConversationProducer conversationProducer) {
        super(activityProducer, conversationProducer);
    }

    @Override
    protected void setActivityDetails(SBEvent event, WorkflowStep step, SBActivity activity) {
        activity.desc = "Sent tweet on " + new Date();
    }

    @Override
    protected void setConversationDetails(SBEvent event, WorkflowStep step, SBConversation conversation) {
        conversation.incoming = false;
        conversation.message = "Avatar Tweeted: " + Constants.MSGS[new Random().nextInt(Constants.MSGS.length)];
    }
}
