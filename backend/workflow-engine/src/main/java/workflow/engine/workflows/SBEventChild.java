package workflow.engine.workflows;

import com.uber.cadence.workflow.WorkflowMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;

import java.util.List;

public class SBEventChild implements ISBEventChild {
    private static final Logger logger = LoggerFactory.getLogger(SBEventChild.class);

    @WorkflowMethod
    public boolean execute(SBEvent event, List<WorkflowStep> workflow) {
        switch(event.type) {
            case "email.open":
                return onEmailOpen(workflow, event);
            case "link.click":
                return onLinkClick(workflow, event);
            case "email.reply":
                return onEmailReply(workflow, event);
            case "chat.reply":
                return onChatReply(workflow, event);
            default:
                logger.info("Ignoring unknown event " + event.type);
                return false;
        }
    }

    private boolean onEmailOpen(List<WorkflowStep> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for email open for lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStep step : workflow) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
        return true;
    }

    private boolean onLinkClick(List<WorkflowStep> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for link click for lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStep step : workflow) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
        return true;
    }

    private boolean onEmailReply(List<WorkflowStep> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for email reply from lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStep step : workflow) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
        return true;
    }

    private boolean onChatReply(List<WorkflowStep> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for chat reply from lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStep step : workflow) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
        return true;
    }

}
