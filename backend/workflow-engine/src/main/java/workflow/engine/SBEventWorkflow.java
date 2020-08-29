package workflow.engine;

import com.uber.cadence.activity.ActivityOptions;
import com.uber.cadence.workflow.SignalMethod;
import com.uber.cadence.workflow.Workflow;
import com.uber.cadence.workflow.WorkflowMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.engine.activities.IGetter;
import workflow.engine.dto.WorkflowStepDTO;

import java.util.ArrayList;
import java.util.List;

public class SBEventWorkflow implements ISBEventWorkflow {

    private static final Logger logger = LoggerFactory.getLogger(SBEventWorkflow.class);
    private List<SBEvent> pendingEvents = new ArrayList<>();
    private final IGetter getter;


    public SBEventWorkflow() {
        ActivityOptions options = new ActivityOptions.Builder()
                .setTaskList("events-workflow-tasklist")
                .build();
        getter = Workflow.newActivityStub(IGetter.class, options);
    }

    @WorkflowMethod
    public void execute() {
        List<WorkflowStepDTO> workflows = null;

        while(pendingEvents.size() > 0) {
            SBEvent current = pendingEvents.remove(0);
            if(workflows == null) workflows = getter.getWorkflows(current.tenantId);
            switch(current.type) {
                case "new.lead":
                    onNewLead(workflows, current);
                    break;
                default:
                    logger.info("Ignoring unknown event " + current.type);
            }
        }
    }

    private void onNewLead(List<WorkflowStepDTO> workflows, SBEvent current) {
        logger.info("TODO: handle workflow for new lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflows:");
        for(WorkflowStepDTO step : workflows) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
    }

    @SignalMethod
    public void addEvent(SBEvent event) {
        pendingEvents.add(event);
    }
}
