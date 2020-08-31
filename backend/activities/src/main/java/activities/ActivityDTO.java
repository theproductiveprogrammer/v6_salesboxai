package activities;

import activities.db.Activity;

public class ActivityDTO {
    public int whatEntity;
    public Long whatId;
    public String desc;

    public ActivityDTO() {}

    public ActivityDTO(Activity activityDTO) {
        this.whatEntity = activityDTO.getWhatEntity();
        this.whatId = activityDTO.getWhatId();
        this.desc = activityDTO.getDescription();
    }
}
