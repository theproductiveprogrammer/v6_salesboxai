package workflow.engine.handler;

import io.micronaut.http.client.RxHttpClient;
import workflow.engine.kafka.ActivityProducer;
import workflow.engine.kafka.ConversationProducer;

public class HandlerIOC {
    public ActivityProducer activityProducer;
    public ConversationProducer conversationProducer;
    public RxHttpClient scoringClient;

    public HandlerIOC(ActivityProducer activityProducer, ConversationProducer conversationProducer, RxHttpClient scoringClient) {
        this.activityProducer = activityProducer;
        this.conversationProducer = conversationProducer;
        this.scoringClient = scoringClient;
    }
}
