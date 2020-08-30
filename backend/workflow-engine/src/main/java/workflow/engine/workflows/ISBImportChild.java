package workflow.engine.workflows;

import com.uber.cadence.workflow.WorkflowMethod;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;

import java.util.List;

public interface ISBImportChild {
    @WorkflowMethod
    public boolean execute(SBEvent event, List<WorkflowStep> workflow);
}
