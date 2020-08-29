package workflow.engine.activities;

import com.uber.cadence.activity.ActivityMethod;
import workflow.engine.dto.WorkflowStepDTO;

import java.util.List;

public interface IGetter {
    @ActivityMethod(scheduleToCloseTimeoutSeconds = 100)
    public List<WorkflowStepDTO> getWorkflows(Long tenantId);
}
