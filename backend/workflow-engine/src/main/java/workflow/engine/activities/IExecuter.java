package workflow.engine.activities;

import com.uber.cadence.activity.ActivityMethod;
import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;

public interface IExecuter {
    @ActivityMethod(scheduleToCloseTimeoutSeconds = 100)
    public NextStep doStep(SBEvent event, WorkflowStep step);
}
