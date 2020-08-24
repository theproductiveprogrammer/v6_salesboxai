package biz.objects.repo;

import biz.objects.db.EventMeta;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface EventMetaRepository extends CrudRepository<EventMeta, String> {
    List<EventMeta> findAll();
}