package event.server;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import javax.inject.Inject;
import java.security.Principal;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller
public class EventServer {
    @Inject
    EventProducer eventProducer;

    @Post("/newevent")
    public void newEvent(SBEvent sbEvent, Authentication authentication) {
        Long tenantId = (Long) authentication.getAttributes().get("tenant");
        if(tenantId == null || tenantId == 0) return;
        eventProducer.newEvent(tenantId, sbEvent);
    }
}
