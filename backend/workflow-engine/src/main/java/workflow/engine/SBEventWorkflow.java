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
import java.util.stream.Collectors;

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
            String eventCode = "evt/" + current.type;
            List<WorkflowStepDTO> workflow = workflows.stream().filter(s -> eventCode.equals(s.eventCode)).collect(Collectors.toList());
            if(workflow == null || workflow.size() < 2) {
                logger.info("No workflow for " + current.type + " found for tenant " + current.tenantId);
                continue;
            }
            switch(current.type) {
                case "new.lead":
                    onNewLead(workflow, current);
                    break;
                case "email.open":
                    onEmailOpen(workflow, current);
                    break;
                case "link.click":
                    onLinkClick(workflow, current);
                    break;
                case "email.reply":
                    onEmailReply(workflow, current);
                    break;
                case "chat.reply":
                    onChatReply(workflow, current);
                    break;
                default:
                    logger.info("Ignoring unknown event " + current.type);
            }
        }
    }

    private void onNewLead(List<WorkflowStepDTO> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for new lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStepDTO step : workflow) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
    }

    private void onEmailOpen(List<WorkflowStepDTO> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for email open for lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStepDTO step : workflow) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
    }

    private void onLinkClick(List<WorkflowStepDTO> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for link click for lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStepDTO step : workflow) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
    }

    private void onEmailReply(List<WorkflowStepDTO> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for email reply from lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStepDTO step : workflow) {
            Integer link1 = step.links != null && step.links.length > 0 ? step.links[0] : null;
            Integer link2 = step.links != null && step.links.length > 1 ? step.links[1] : null;
            logger.info(step.num + ". " + step.eventCode + "://" + step.code + "->" + link1 + "," + link2);
        }
    }

    private void onChatReply(List<WorkflowStepDTO> workflow, SBEvent current) {
        logger.info("TODO: handle workflow for chat reply from lead " + current.id + " on tenant " + current.tenantId);
        logger.info("Workflow:");
        for(WorkflowStepDTO step : workflow) {
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
