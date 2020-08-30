package biz.objects.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StepMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private Long numlinks;
    private Long iconszhint;
    private String handler;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumlinks() {
        return numlinks;
    }

    public void setNumlinks(Long numlinks) {
        this.numlinks = numlinks;
    }

    public Long getIconszhint() {
        return iconszhint;
    }

    public void setIconszhint(Long iconszhint) {
        this.iconszhint = iconszhint;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }
}
