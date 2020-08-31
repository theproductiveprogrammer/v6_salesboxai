package conversations;

import conversations.db.Conversation;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;
import java.util.List;

@Controller
public class ConversationServer {
    @Inject
    ConversationRepository conversationRepository;

    @Get("/conversations")
    public List<Conversation> getConversations(Long leadId) {
        return conversationRepository.findByLeadId(leadId);
    }
}
