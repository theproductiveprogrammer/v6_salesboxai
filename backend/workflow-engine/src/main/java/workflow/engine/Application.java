package workflow.engine;

import com.uber.cadence.worker.Worker;
import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        registerWorkflowEngine();
        Micronaut.run(Application.class, args);
    }

    private static void registerWorkflowEngine() {
        Worker.Factory factory = new Worker.Factory("salesboxai-domain");
        Worker worker = factory.newWorker("events-workflow-tasklist");
        worker.registerWorkflowImplementationTypes(SBEventWorkflow.class);
        factory.start();
    }
}