package workflow.engine.activities;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import workflow.engine.WorkflowStep;

import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Getter implements IGetter {

    @Inject
    @Client("biz-objects")
    RxHttpClient bizObjectsClient;

    @Override
    synchronized public List<WorkflowStep> getWorkflows(Long tenantId) {
        URI uri = UriBuilder.of("/internal/workflows")
                .queryParam("tenantId", tenantId)
                .build();
        return bizObjectsClient
                .toBlocking()
                .retrieve(HttpRequest.GET(uri), Res.class);
    }

    public static class Res extends ArrayList<WorkflowStep> { }
}
