package authenticator.db;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, Long> {
    Optional<Tenant> findById(Long tenantId);
    Optional<Tenant> findByName(String name);
}
