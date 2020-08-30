package workflow.engine.workflows;

import com.uber.cadence.workflow.Workflow;
import com.uber.cadence.workflow.WorkflowMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;
import workflow.engine.activities.IExecuter;

import java.util.List;

public class SBEventChild implements ISBEventChild {
    private static final Logger logger = LoggerFactory.getLogger(SBEventChild.class);

    IExecuter executer;

    public SBEventChild() { executer = Workflow.newActivityStub(IExecuter.class); }

    @WorkflowMethod
    public boolean execute(SBEvent event, List<WorkflowStep> workflow) {
        switch(event.type) {
            case "email.open":
            case "link.click":
            case "email.reply":
            case "chat.reply":
                return executeWorkFlow(workflow, event);
            default:
                logger.info("Ignoring unknown event " + event.type);
                return false;
        }
    }

    private boolean executeWorkFlow(List<WorkflowStep> workflow, SBEvent event) {
        NextStep nextStep = new NextStep(0, 0);
        while(true) {
            nextStep = executer.doStep(event, workflow.get(nextStep.ndx));
            if(nextStep == null) break;
            if(nextStep.ndx <= 0 || nextStep.ndx >= workflow.size()) break;
            if(nextStep.delay > 0) Workflow.sleep(nextStep.delay);
        }
        return true;
    }

}
