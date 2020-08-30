package workflow.engine.workflows;

import com.uber.cadence.workflow.WorkflowMethod;
import workflow.engine.SBEvent;
import workflow.engine.dto.WorkflowStepDTO;

import java.util.List;

public interface ISBEventExecute {
    @WorkflowMethod
    public boolean execute(SBEvent event, List<WorkflowStepDTO> workflow);
}
