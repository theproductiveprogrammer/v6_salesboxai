package importer.repo;

import importer.db.Import;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ImportRepository extends CrudRepository<Import, Long> {
    List<Import> findByTenantId(Long tenantId);
}
