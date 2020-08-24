package biz.objects;

import biz.objects.db.StepMeta;

public class StepMetaDAO {
    private Long id;
    private String code;
    private String name;
    private Long numlinks;
    private Long iconszhint;

    private String icon;
    private String pic;

    public StepMetaDAO(StepMeta s) {
        id = s.getId();
        code = s.getCode();
        name = s.getName();
        numlinks = s.getNumlinks();
        iconszhint = s.getIconszhint();

        icon = "/steps/icon/" + code + ".svg";
        pic = "/steps/pic/" + code + ".svg";
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
