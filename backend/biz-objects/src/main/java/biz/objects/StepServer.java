package biz.objects;

import biz.objects.db.StepMeta;
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
    public List<StepMetaDAO> getStepMeta() {
        List<StepMeta> steps = stepMetaRepository.findAll();
        return steps.stream().map(s -> new StepMetaDAO(s)).collect(Collectors.toList());
    }

}
