package authenticator.db;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserid(String userid);
    @Join(value="tenant", type=Join.Type.FETCH)
    Optional<User> getByUserid(String userid);
}
