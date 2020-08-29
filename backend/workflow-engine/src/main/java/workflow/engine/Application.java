package workflow.engine;

import com.uber.cadence.worker.Worker;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import workflow.engine.activities.Getter;

import javax.inject.Inject;

public class Application implements ApplicationEventListener<ServerStartupEvent> {

    public static void main(String[] args) { Micronaut.run(Application.class, args); }

    @Inject
    private Getter getter;

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        registerWorkflowEngine();
    }

    private void registerWorkflowEngine() {
        Worker.Factory factory = new Worker.Factory("salesboxai-domain");
        Worker worker = factory.newWorker("events-workflow-tasklist");
        worker.registerWorkflowImplementationTypes(SBEventWorkflow.class);
        worker.registerActivitiesImplementations(getter);
        factory.start();
    }

    @Override
    public boolean supports(ServerStartupEvent event) {
        return true;
    }
}
