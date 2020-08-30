package workflow.engine.workflows;

import com.uber.cadence.activity.ActivityOptions;
import com.uber.cadence.workflow.Async;
import com.uber.cadence.workflow.Promise;
import com.uber.cadence.workflow.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.engine.SBEvent;
import workflow.engine.activities.IGetter;
import workflow.engine.WorkflowStep;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SBEventWorkflow implements ISBEventWorkflow {
    private static final Logger logger = LoggerFactory.getLogger(SBEventWorkflow.class);

    private List<SBEvent> pendingEvents = new ArrayList<>();
    private final IGetter getter;
    private List<Promise<?>> inflight = new ArrayList<>();

    public SBEventWorkflow() {
        getter = Workflow.newActivityStub(IGetter.class);
    }

    @Override
    public void start(Long tenantId, int dripCount) {
        List<WorkflowStep> workflows = getter.getWorkflows(tenantId);
        while(pendingEvents.size() > 0 || inflight.size() > 0) {
            inflight = inflight.stream().filter(c -> !c.isCompleted()).collect(Collectors.toList());

            if(dripCount > 0 && inflight.size() >= dripCount) Promise.anyOf(inflight).get();
            else if(pendingEvents.size() > 0) execute(workflows, pendingEvents.remove(0));
            else Workflow.sleep(100);
        }
    }

    private void execute(List<WorkflowStep> workflows, SBEvent current) {
        String eventCode = "evt/" + current.type;
        List<WorkflowStep> workflow = workflows.stream().filter(s -> eventCode.equals(s.eventCode)).collect(Collectors.toList());
        if(workflow == null || workflow.size() < 2) {
            logger.info("No workflow for " + current.type + " found for tenant " + current.tenantId);
        } else {
            ISBEventChild child = Workflow.newChildWorkflowStub(ISBEventChild.class);
            inflight.add(Async.function(child::execute, current, workflow));
        }
    }

    @Override
    public void addEvent(SBEvent event) { pendingEvents.add(event); }
}
