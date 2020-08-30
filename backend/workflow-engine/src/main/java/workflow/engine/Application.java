package workflow.engine;

import com.uber.cadence.worker.Worker;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import workflow.engine.activities.Getter;
import workflow.engine.conf.CadenceConfig;

import javax.inject.Inject;

public class Application implements ApplicationEventListener<ServerStartupEvent> {

    public static void main(String[] args) { Micronaut.run(Application.class, args); }

    @Inject
    private Getter getter;

    @Inject
    CadenceConfig cadenceConfig;

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
        registerWorkflowEngine();
    }

    private void registerWorkflowEngine() {
        Worker.Factory factory = new Worker.Factory(cadenceConfig.getHost(), cadenceConfig.getPort(), cadenceConfig.getDomain());
        Worker worker = factory.newWorker("events-workflow-tasklist");
        worker.registerWorkflowImplementationTypes(SBWorkflow.class, SBEventExecute.class);
        worker.registerActivitiesImplementations(getter);
        factory.start();
    }

    @Override
    public boolean supports(ServerStartupEvent event) {
        return true;
    }
}
