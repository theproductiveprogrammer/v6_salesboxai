package biz.objects;

import biz.objects.db.StepMeta;
import biz.objects.db.WorkflowStep;
import biz.objects.dto.StepMetaDTO;
import biz.objects.dto.WorkflowStepDTO;
import biz.objects.repo.StepMetaRepository;
import biz.objects.repo.WorkflowStepRepository;
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

    @Inject
    WorkflowStepRepository workflowStepRepository;

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
        Long tenantId = (Long)principal.getAttributes().get("tenant");
        List<WorkflowStep> steps_ = steps.stream().map(s -> new WorkflowStep(s, tenantId)).collect(Collectors.toList());
        workflowStepRepository.delete(tenantId);
        for(WorkflowStep step : steps_) {
            workflowStepRepository.save(step);
        }
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/workflows")
    public List<WorkflowStepDTO> getSteps(Authentication principal) {
        Long tenantId = (Long)principal.getAttributes().get("tenant");
        return workflowStepRepository.getByTenantId(tenantId).stream().map(WorkflowStepDTO::new).collect(Collectors.toList());
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("/internal/workflows")
    public List<WorkflowStepDTO> getSteps(Long tenantId) {
        List<WorkflowStepDTO> steps = workflowStepRepository.getByTenantId(tenantId).stream().map(WorkflowStepDTO::new).collect(Collectors.toList());
        List<StepMeta> meta = stepMetaRepository.findAll();
        for(WorkflowStepDTO step : steps) {
            step.handler = findHandler(meta, step.code);
        }
        return steps;
    }

    private String findHandler(List<StepMeta> meta, String code) {
        for(StepMeta m : meta) {
            if(code.equals(m.getCode())) return m.getHandler();
        }
        return null;
    }
}
