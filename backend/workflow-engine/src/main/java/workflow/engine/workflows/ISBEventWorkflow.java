package workflow.engine.workflows;

import com.uber.cadence.workflow.SignalMethod;
import com.uber.cadence.workflow.WorkflowMethod;
import workflow.engine.SBEvent;

public interface ISBEventWorkflow {
    @WorkflowMethod
    public void start(Long tenantId, int dripCount);
    @SignalMethod
    public void addEvent(SBEvent event);
}
