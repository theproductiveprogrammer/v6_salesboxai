package authenticator;

import authenticator.db.User;
import authenticator.db.UserRepository;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class AuthProvider implements AuthenticationProvider {

    @Inject
    UserRepository userRepository;

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            UserDetails user = getUserDetails((String) authenticationRequest.getIdentity(), (String) authenticationRequest.getSecret());
            if(user == null) emitter.onError(new AuthenticationException(new AuthenticationFailed()));
            else {
                emitter.onNext(user);
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR);
    }

    private UserDetails getUserDetails(String identity, String secret) {
        if(identity == null || secret == null) return null;
        identity = identity.strip();
        secret = secret.strip();
        if(identity.length() ==0 || secret.length() == 0) return null;

        User user = userRepository.findByName(identity).orElse(null);
        if(user == null) return null;
        if(user.getName().equals(identity) && user.getPassword().equals(secret)) {
            return new UserDetails(user.getName(), new ArrayList<>());
        }
        return null;
    }
}
