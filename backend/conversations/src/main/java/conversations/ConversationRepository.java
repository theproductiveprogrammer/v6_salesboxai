package conversations;

import conversations.db.Conversation;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ConversationRepository extends CrudRepository<Conversation, Long> {
    public List<Conversation> findByLeadId(Long leadId);
}
