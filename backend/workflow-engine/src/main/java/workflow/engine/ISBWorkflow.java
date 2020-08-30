package workflow.engine;

import com.uber.cadence.workflow.SignalMethod;
import com.uber.cadence.workflow.WorkflowMethod;

public interface ISBWorkflow {
    @WorkflowMethod
    public void start();
    @SignalMethod
    public void addEvent(SBEvent event);
}
