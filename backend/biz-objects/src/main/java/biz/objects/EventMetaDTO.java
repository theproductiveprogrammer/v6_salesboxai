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
}
