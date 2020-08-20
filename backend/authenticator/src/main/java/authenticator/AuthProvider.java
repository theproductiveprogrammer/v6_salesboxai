package authenticator;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import org.reactivestreams.Publisher;
import io.reactivex.Flowable;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class AuthProvider implements AuthenticationProvider {
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
        if(identity.equals("roy") && secret.equals("rajan")) return new UserDetails("Roy Rajan", new ArrayList<>());
        if(identity.equals("charles") && secret.equals("lobo")) return new UserDetails("Charles Lobo", new ArrayList<>());
        return null;
    }
}
