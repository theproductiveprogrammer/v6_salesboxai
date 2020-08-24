package biz.objects;

import biz.objects.db.StepMeta;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface StepMetaRepository extends CrudRepository<StepMeta, String> {
    List<StepMeta> findAll();
}