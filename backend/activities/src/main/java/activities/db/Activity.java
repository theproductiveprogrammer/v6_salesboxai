package activities.db;

import activities.ActivityDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tenantId;
    private int whatEntity;
    private Long whatId;
    private String description;

    public Activity() {}

    public Activity(Long tenantId, ActivityDTO activityDTO) {
        this.tenantId = tenantId;
        this.whatEntity = activityDTO.whatEntity;
        this.whatId = activityDTO.whatId;
        this.description = activityDTO.desc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public int getWhatEntity() {
        return whatEntity;
    }

    public void setWhatEntity(int whatEntity) {
        this.whatEntity = whatEntity;
    }

    public Long getWhatId() {
        return whatId;
    }

    public void setWhatId(Long whatId) {
        this.whatId = whatId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
