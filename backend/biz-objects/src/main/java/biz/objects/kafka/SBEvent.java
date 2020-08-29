package biz.objects.kafka;

public class SBEvent {
    public String type;
    public Long id;

    public SBEvent(String type, Long id) {
        this.type = type;
        this.id = id;
    }
}
