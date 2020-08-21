package authenticator;

import authenticator.db.User;
import authenticator.db.UserRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import javax.inject.Inject;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/signup")
public class SignUpController {
    @Inject
    UserRepository userRepository;

    @Post
    public boolean signUp(String username, String password) {
        User user = userRepository.findByName(username).orElse(null);
        if(user != null) return false;
        user = new User(username, password);
        user = userRepository.save(user);
        return user.getId() != null;
    }
}
