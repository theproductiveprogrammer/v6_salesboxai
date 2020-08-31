package activities;

import activities.db.Activity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
    public List<Activity> findByWhatId(Long whatId);
}
