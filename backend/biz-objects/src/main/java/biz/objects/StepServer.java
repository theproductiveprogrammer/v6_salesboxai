package biz.objects;

import biz.objects.db.StepMeta;
import biz.objects.dto.StepMetaDTO;
import biz.objects.repo.StepMetaRepository;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StepServer {
    @Inject
    StepMetaRepository stepMetaRepository;

    @Post("/newstepmeta")
    public void newStepMeta(@Body StepMeta stepMeta) {
        stepMetaRepository.save(stepMeta);
    }

    @Get("/stepmeta")
    public List<StepMetaDTO> getStepMeta() {
        List<StepMeta> steps = stepMetaRepository.findAll();
        return steps.stream().map(s -> new StepMetaDTO(s)).collect(Collectors.toList());
    }

}
