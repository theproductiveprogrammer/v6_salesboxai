package workflow.engine;

import com.uber.cadence.workflow.SignalMethod;
import com.uber.cadence.workflow.WorkflowMethod;

public interface ISBEventWorkflow {
    @WorkflowMethod
    public void execute();
    @SignalMethod
    public void addEvent(SBEvent event);
}
