package workflow.engine.activities;

import com.uber.cadence.activity.ActivityMethod;
import workflow.engine.WorkflowStep;

import java.util.List;

public interface IGetter {
    @ActivityMethod(scheduleToCloseTimeoutSeconds = 100)
    public List<WorkflowStep> getWorkflows(Long tenantId);
}
