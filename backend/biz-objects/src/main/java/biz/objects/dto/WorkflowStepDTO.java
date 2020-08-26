package biz.objects.dto;

import biz.objects.db.WorkflowStep;
import biz.objects.util.Pos;

public class WorkflowStepDTO {
    public String eventCode;
    public String code;
    public Pos pos;

    public WorkflowStepDTO() {}

    public WorkflowStepDTO(WorkflowStep from) {
        this.eventCode = from.getEventCode();
        this.code = from.getCode();
        this.pos = new Pos(from.getX(), from.getY());
    }
}
