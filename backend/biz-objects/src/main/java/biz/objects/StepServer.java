package biz.objects;

import biz.objects.db.StepMeta;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;
import java.util.List;

@Controller
public class StepServer {
    @Inject
    StepMetaRepository stepMetaRepository;

    @Post("/newstepmeta")
    public void newStepMeta(@Body StepMeta stepMeta) {
        stepMetaRepository.save(stepMeta);
    }

    @Get("/stepmeta")
    public List<StepMeta> getStepMeta() {
        return stepMetaRepository.findAll();
    }
}
