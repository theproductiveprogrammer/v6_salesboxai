package workflow.engine.activities;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import workflow.engine.WorkflowStep;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class Getter implements IGetter {

    @Inject
    @Client
    RxHttpClient httpClient;

    @Override
    public List<WorkflowStep> getWorkflows(Long tenantId) {
        return httpClient
                .toBlocking()
                .retrieve(HttpRequest.GET("http://localhost:6160/internal/workflows?&tenantId=" + tenantId), Res.class);
    }

    public static class Res extends ArrayList<WorkflowStep> { }
}
