package workflow.engine;

import com.uber.cadence.WorkflowIdReusePolicy;
import com.uber.cadence.client.BatchRequest;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import io.micronaut.configuration.kafka.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.engine.conf.CadenceConfig;
import workflow.engine.workflows.ISBEventWorkflow;
import workflow.engine.workflows.ISBImportWorkflow;

import javax.inject.Inject;
import java.time.Duration;

@KafkaListener(offsetReset = OffsetReset.EARLIEST)
public class SBEventListener {
    public static final Logger logger = LoggerFactory.getLogger(SBEventListener.class);

    @Inject
    CadenceConfig cadenceConfig;

    @Topic("sb-event")
    public void onEvent(@KafkaKey Long tenantId, SBEvent event) {
        logger.info("Got new event " + event.type + "." + event.id + " for tenant " + tenantId);
        if(tenantId == null || tenantId == 0) return;
        event.tenantId = tenantId;
        if("new.lead".equals(event.type)) launchImportWorkflow(event);
        else launchEventWorkflow(event);
    }

    private void launchImportWorkflow(SBEvent event) {
        WorkflowClient client = WorkflowClient.newInstance(cadenceConfig.getHost(), cadenceConfig.getPort(), cadenceConfig.getDomain());
        WorkflowOptions workflowOptions =
                new WorkflowOptions.Builder()
                        .setWorkflowId("import-" + event.tenantId)
                        .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.AllowDuplicate)
                        .setTaskList("import-workflow-tasklist")
                        .setExecutionStartToCloseTimeout(Duration.ofDays(30))
                        .build();
        try {
            ISBImportWorkflow workflow = client.newWorkflowStub(ISBImportWorkflow.class, workflowOptions);
            BatchRequest req = client.newSignalWithStartRequest();
            req.add(workflow::addImport, event);
            req.add(workflow::start, event.tenantId, 1000);
            client.signalWithStart(req);
        } catch(Throwable t) {
            logger.error("Failed to invoke import workflow", t);
        }
    }

    private void launchEventWorkflow(SBEvent event) {
        WorkflowClient client = WorkflowClient.newInstance("salesboxai-domain");
        WorkflowOptions workflowOptions =
                new WorkflowOptions.Builder()
                        .setWorkflowId("event-" + event.tenantId)
                        .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.AllowDuplicate)
                        .setTaskList("events-workflow-tasklist")
                        .setExecutionStartToCloseTimeout(Duration.ofDays(30))
                        .build();
        try {
            ISBEventWorkflow workflow = client.newWorkflowStub(ISBEventWorkflow.class, workflowOptions);
            BatchRequest req = client.newSignalWithStartRequest();
            req.add(workflow::addEvent, event);
            req.add(workflow::start, event.tenantId, 1000);
            client.signalWithStart(req);
        } catch(Throwable t) {
            logger.error("Failed to invoke events workflow", t);
        }
    }
}