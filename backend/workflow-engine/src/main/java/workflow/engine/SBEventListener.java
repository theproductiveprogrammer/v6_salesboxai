package workflow.engine;

import com.uber.cadence.WorkflowIdReusePolicy;
import com.uber.cadence.client.BatchRequest;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import io.micronaut.configuration.kafka.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
public class SBEventListener {
    public static final Logger logger = LoggerFactory.getLogger(SBEventListener.class);

    @Topic("sb-event")
    public void onEvent(@KafkaKey Long tenantId, SBEvent event) {
        logger.info("Got new event " + event.type + "." + event.id + " for tenant " + tenantId);
        if(tenantId == null || tenantId == 0) return;
        WorkflowClient client = WorkflowClient.newInstance("salesboxai-domain");
        WorkflowOptions workflowOptions =
                new WorkflowOptions.Builder()
                        .setWorkflowId(tenantId.toString())
                        .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.AllowDuplicate)
                        .setTaskList("events-workflow-tasklist")
                        .setExecutionStartToCloseTimeout(Duration.ofSeconds(30))
                        .build();
        try {
            ISBWorkflow workflow = client.newWorkflowStub(ISBWorkflow.class, workflowOptions);
            BatchRequest req = client.newSignalWithStartRequest();
            event.tenantId = tenantId;
            req.add(workflow::addEvent, event);
            client.signalWithStart(req);
        } catch(Throwable t) {
            logger.error("Failed to invoke workflow", t);
        }
    }
}