package biz.objects;

import biz.objects.db.Lead;
import biz.objects.repo.LeadRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import javax.inject.Inject;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller
public class LeadServer {
    @Inject
    LeadRepository leadRepository;

    @Get("/leads")
    public List<Lead> getLeads(Authentication authentication) {
        Long tenantId = (Long)authentication.getAttributes().get("tenant");
        return leadRepository.findByTenantId(tenantId);
    }
}
