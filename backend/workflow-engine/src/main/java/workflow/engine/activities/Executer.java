package workflow.engine.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.handler.IEventHandler;
import workflow.engine.kafka.ActivityProducer;
import workflow.engine.kafka.ConversationProducer;
import workflow.engine.kafka.SBActivity;

import javax.inject.Inject;
import java.lang.reflect.Constructor;

public class Executer implements IExecuter {
    private static final Logger logger = LoggerFactory.getLogger(Executer.class);

    @Inject
    ActivityProducer activityProducer;
    @Inject
    ConversationProducer conversationProducer;

    @Override
    public NextStep doStep(SBEvent event, WorkflowStep step) {
        logger.info("Executing " + step.code + " using " + step.handler + " for " + event.type + " on lead: " + event.id);
        try {
            Class<IEventHandler> class_ = (Class<IEventHandler>) Class.forName(step.handler);
            Constructor<IEventHandler> const_ = class_.getConstructor(ActivityProducer.class, ConversationProducer.class);
            IEventHandler handler = const_.newInstance(activityProducer, conversationProducer);
            return handler.handle(event, step);
        } catch(Throwable t) {
            logger.error("Error executing step", t);
            return null;
        }
    }
}
