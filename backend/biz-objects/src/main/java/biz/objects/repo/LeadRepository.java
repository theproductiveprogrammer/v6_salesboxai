package biz.objects.repo;

import biz.objects.db.Lead;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface LeadRepository extends CrudRepository<Lead, Long> {
    public List<Lead> findByTenantId(Long tenantId);
}
