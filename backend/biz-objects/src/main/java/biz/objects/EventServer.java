package biz.objects;

import biz.objects.db.EventMeta;
import biz.objects.dto.EventMetaDTO;
import biz.objects.repo.EventMetaRepository;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EventServer {
    @Inject
    EventMetaRepository eventMetaRepository;

    @Post("/neweventmeta")
    public void newEventMeta(@Body EventMeta eventMeta) {
        eventMetaRepository.save(eventMeta);
    }

    @Get("/eventmeta")
    public List<EventMetaDTO> getStepMeta() {
        List<EventMeta> events = eventMetaRepository.findAll();
        return events.stream().map(EventMetaDTO::new).collect(Collectors.toList());
    }

}
