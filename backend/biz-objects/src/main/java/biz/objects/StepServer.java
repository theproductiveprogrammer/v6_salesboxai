package biz.objects;

import biz.objects.db.StepMeta;
import biz.objects.dto.StepMetaDTO;
import biz.objects.dto.WorkflowStepDTO;
import biz.objects.repo.StepMetaRepository;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import javax.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller
public class StepServer {
    @Inject
    StepMetaRepository stepMetaRepository;

    @Post("/newstepmeta")
    public void newStepMeta(@Body StepMeta stepMeta) {
        stepMetaRepository.save(stepMeta);
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/stepmeta")
    public List<StepMetaDTO> getStepMeta() {
        List<StepMeta> steps = stepMetaRepository.findAll();
        return steps.stream().map(StepMetaDTO::new).collect(Collectors.toList());
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/newsteps")
    public void saveSteps(@Body List<WorkflowStepDTO> steps, Authentication principal) {
        System.out.println(principal.getAttributes().get("tenant"));
    }
}
