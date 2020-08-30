package workflow.engine.workflows;

import com.uber.cadence.workflow.WorkflowMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.engine.SBEvent;
import workflow.engine.dto.WorkflowStepDTO;

import java.util.List;

public class SBImportChild implements ISBImportChild {
    private static final Logger logger = LoggerFactory.getLogger(SBImportChild.class);

    @WorkflowMethod
    public boolean execute(SBEvent event, List<WorkflowStepDTO> workflow) {
        switch(event.type) {
            case "new.lead":
                return onNewLead(workflow, event);
            default:
                logger.info("Ignoring unknown event " + event.type);
                return false;
        }
    }

    private boolean onNewLead(List<WorkflowStepDTO> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for new lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStepDTO step : workflow) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
        return true;
    }
}
