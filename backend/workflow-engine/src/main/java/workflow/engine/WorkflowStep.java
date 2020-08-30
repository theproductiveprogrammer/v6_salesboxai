package workflow.engine;

public class WorkflowStep {
    public Long id;
    public Integer num;
    public Long tenantId;
    public String eventCode;
    public String code;
    public String handler;
    public Integer[] links;
}
