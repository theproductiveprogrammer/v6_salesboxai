package biz.objects.db;

import biz.objects.dto.WorkflowStepDTO;
import biz.objects.util.Pos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class WorkflowStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer num;
    private Long tenantId;
    private String eventCode;
    private String code;
    private Integer link1;
    private Integer link2;
    private Integer x;
    private Integer y;

    public WorkflowStep() {}

    public WorkflowStep(WorkflowStepDTO from, Long tenantId) {
        this.num = from.num;
        this.eventCode = from.eventCode;
        this.code = from.code;
        this.x = from.pos.x;
        this.y = from.pos.y;
        this.tenantId = tenantId;
        if(from.links != null) {
            if(from.links.size() > 0) this.link1 = from.links.get(0);
            if(from.links.size() > 1) this.link2 = from.links.get(1);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getLink1() {
        return link1;
    }

    public void setLink1(Integer link1) {
        this.link1 = link1;
    }

    public Integer getLink2() {
        return link2;
    }

    public void setLink2(Integer link2) {
        this.link2 = link2;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
