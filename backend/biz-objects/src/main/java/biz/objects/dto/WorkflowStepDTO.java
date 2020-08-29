package biz.objects.dto;

import biz.objects.db.WorkflowStep;
import biz.objects.util.Pos;

import java.util.ArrayList;
import java.util.List;

public class WorkflowStepDTO {
    public Integer num;
    public String eventCode;
    public String code;
    public List<Integer> links;
    public Pos pos;

    public WorkflowStepDTO() {}

    public WorkflowStepDTO(WorkflowStep from) {
        this.num = from.getNum();
        this.eventCode = from.getEventCode();
        this.code = from.getCode();
        this.pos = new Pos(from.getX(), from.getY());
        addLink(from.getLink1());
        addLink(from.getLink2());
    }

    private void addLink(Integer link) {
        if(link == null || link == 0) return;
        if(links == null) links = new ArrayList<>();
        links.add(link);
    }
}
