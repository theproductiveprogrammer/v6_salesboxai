package workflow.engine.handler;

import workflow.engine.NextStep;
import workflow.engine.SBEvent;
import workflow.engine.WorkflowStep;

public interface IEventHandler {
    public NextStep handle(SBEvent event, WorkflowStep step);
}
