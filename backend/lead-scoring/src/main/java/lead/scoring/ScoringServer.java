package lead.scoring;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import lead.scoring.db.LeadScore;

import javax.inject.Inject;

@Controller
public class ScoringServer {

    @Inject
    LeadScoreRepository leadScoreRepository;

    @Post("/internal/updatescore")
    public void updateScore(ScoreDTO scoreDTO) {
        LeadScore leadScore = leadScoreRepository.findOneByLeadIdAndTenantId(scoreDTO.leadId, scoreDTO.tenantId).orElse(null);
        if(leadScore == null) {
            leadScore = new LeadScore();
            leadScore.setLeadId(scoreDTO.leadId);
            leadScore.setTenantId(scoreDTO.tenantId);
        }

        long score = leadScore.getScore() != null ? leadScore.getScore() : 0L;
        score += scoreDTO.score;

        leadScore.setScore(score);
        if(leadScore.getId() == null) leadScoreRepository.save(leadScore);
        else leadScoreRepository.update(leadScore);
    }

    @Get("/score")
    public Long getScore(Long leadId) {
        LeadScore leadScore = leadScoreRepository.findOneByLeadId(leadId).orElse(null);
        if(leadScore == null) return 0L;
        else return leadScore.getScore() != null ? leadScore.getScore() : 0L;
    }
}
