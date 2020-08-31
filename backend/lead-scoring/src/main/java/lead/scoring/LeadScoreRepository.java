package lead.scoring;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import lead.scoring.db.LeadScore;

import java.util.Optional;

@Repository
public interface LeadScoreRepository extends CrudRepository<LeadScore, Long> {
    public Optional<LeadScore> findOneByLeadId(Long leadId);
    public Optional<LeadScore> findOneByLeadIdAndTenantId(Long leadId, Long tenantId);
}
