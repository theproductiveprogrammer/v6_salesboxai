package workflow.engine.handler;

import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.kafka.ActivityProducer;
import workflow.engine.kafka.ConversationProducer;
import workflow.engine.kafka.SBActivity;
import workflow.engine.kafka.SBConversation;

import java.util.Date;

public class ConnectFacebook extends EventHandlerBase {

    public ConnectFacebook(HandlerIOC handlerIOC) {
        super(handlerIOC);
    }

    @Override
    protected void setActivityDetails(SBEvent event, WorkflowStep step, SBActivity activity) {
        activity.desc = "Sent Facebook friend request to lead " + event.id + " on " + new Date();
    }

    @Override
    protected void setConversationDetails(SBEvent event, WorkflowStep step, SBConversation conversation) {
        conversation.incoming = false;
        conversation.message = "Sent you a friend request on Facebook";
    }
}
