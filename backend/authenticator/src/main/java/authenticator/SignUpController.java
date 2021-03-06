package authenticator;

import authenticator.db.Tenant;
import authenticator.db.TenantRepository;
import authenticator.db.User;
import authenticator.db.UserRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller()
public class SignUpController {
    @Inject
    UserRepository userRepository;
    @Inject
    TenantRepository tenantRepository;

    @Post("/signup")
    public boolean signUp(String userid, String password, Long tenantId, String name) {
        if(userid == null || password == null || tenantId == null || name == null) return false;
        userid = userid.strip(); password = password.strip(); name = name.strip();
        if(userid.length() == 0 || password.length() == 0 || tenantId == 0 || name.length() == 0) return false;

        User user = userRepository.findByUserid(userid).orElse(null);
        if(user != null) return false;
        Tenant tenant = tenantRepository.findById(tenantId).orElse(null);
        if(tenant == null) return false;
        user = new User(userid, password, tenant, name);
        user = userRepository.save(user);
        return user.getId() != null;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/profile")
    public User profile(Principal principal) {
        User user = userRepository.getByUserid(principal.getName()).orElse(null);
        if(user == null) return null;
        user.getTenant();
        user.setPassword(null);
        return user;
    }

    @Post("/newtenant")
    public boolean newTenant(String name) {
        Tenant tenant = tenantRepository.findByName(name).orElse(null);
        if(tenant != null) return false;
        tenant = new Tenant(name);
        tenant = tenantRepository.save(tenant);
        return tenant.getId() != null;
    }

    @Get("/tenants")
    public Iterable<Tenant> tenants() {
        return tenantRepository.findAll();
    }
}
