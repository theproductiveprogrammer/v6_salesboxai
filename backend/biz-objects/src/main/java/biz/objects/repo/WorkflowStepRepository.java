package biz.objects.repo;

import biz.objects.db.WorkflowStep;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface WorkflowStepRepository extends CrudRepository<WorkflowStep, Long> {
    public void delete(Long tenantId);

    public List<WorkflowStep> getByTenantId(Long tenantId);
}
