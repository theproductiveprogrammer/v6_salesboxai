package biz.objects;

import biz.objects.db.EventMeta;

public class EventMetaDTO {
    private Long id;
    private String code;
    private String name;
    private Long iconszhint;

    private String pic;

    public EventMetaDTO(EventMeta e) {
        id = e.getId();
        code = e.getCode();
        name = e.getName();
        iconszhint = e.getIconszhint();

        pic = pic = "/events/pic/" + code + ".svg";
    }

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

    public Long getIconszhint() {
        return iconszhint;
    }

    public void setIconszhint(Long iconszhint) {
        this.iconszhint = iconszhint;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
