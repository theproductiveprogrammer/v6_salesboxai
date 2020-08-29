package workflow.engine;

import com.uber.cadence.workflow.SignalMethod;
import com.uber.cadence.workflow.WorkflowMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SBEventWorkflow implements ISBEventWorkflow {

    private static final Logger logger = LoggerFactory.getLogger(SBEventWorkflow.class);
    private List<SBEvent> pendingEvents = new ArrayList<>();

    @WorkflowMethod
    public void execute() {
        while(pendingEvents.size() > 0) {
            SBEvent current = pendingEvents.remove(0);
            switch(current.type) {
                case "new.lead":
                    onNewLead(current);
                    break;
                default:
                    logger.info("Ignoring unknown event " + current.type);
            }
        }
    }

    private void onNewLead(SBEvent current) {
        logger.info("TODO: handle workflow for new lead " + current.id + " on tenant " + current.tenantId);
    }

    @SignalMethod
    public void addEvent(SBEvent event) {
        pendingEvents.add(event);
    }
}
