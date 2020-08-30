package workflow.engine.workflows;

import com.uber.cadence.activity.ActivityOptions;
import com.uber.cadence.workflow.Async;
import com.uber.cadence.workflow.Promise;
import com.uber.cadence.workflow.SignalMethod;
import com.uber.cadence.workflow.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.engine.SBEvent;
import workflow.engine.activities.IGetter;
import workflow.engine.dto.WorkflowStepDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SBEventWorkflow implements ISBEventWorkflow {

    private static final Logger logger = LoggerFactory.getLogger(SBEventWorkflow.class);
    private List<SBEvent> pendingEvents = new ArrayList<>();
    private final IGetter getter;
    private List<Promise<?>> inflight = new ArrayList<>();
    private int dripCount = 100; // TODO: Set dynamically

    public SBEventWorkflow() {
        ActivityOptions options = new ActivityOptions.Builder()
                .setTaskList("events-workflow-tasklist")
                .build();
        getter = Workflow.newActivityStub(IGetter.class, options);
    }

    @Override
    public void start() {
        List<WorkflowStepDTO> workflows = null;

        while(pendingEvents.size() > 0) {
            SBEvent current = pendingEvents.remove(0);
            if(workflows == null) workflows = getter.getWorkflows(current.tenantId);
            String eventCode = "evt/" + current.type;
            List<WorkflowStepDTO> workflow = workflows.stream().filter(s -> eventCode.equals(s.eventCode)).collect(Collectors.toList());
            if(workflow == null || workflow.size() < 2) {
                logger.info("No workflow for " + current.type + " found for tenant " + current.tenantId);
            } else {
                ISBEventExecute child = Workflow.newChildWorkflowStub(ISBEventExecute.class);
                inflight.add(Async.function(child::execute, current, workflow));
                while(dripCount > 0 && inflight.size() >= dripCount) {
                    Promise.anyOf(inflight).get();
                    inflight = inflight.stream().filter(c -> !c.isCompleted()).collect(Collectors.toList());
                }
            }
        }
    }

    @Override
    public void addEvent(SBEvent event) { pendingEvents.add(event); }

    @Override
    public void setDripCount(int count) { this.dripCount = count; }
}
