package matchbaker.model;

/**
 * Created by Bandjur on 2/28/2017.
 */

public class UserPersonalization {

    private String uid;

    private UserBasicInfo basicInfo;
    private UserActivities activities;
    private UserPsychology psychology;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserBasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(UserBasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public UserActivities getActivities() {
        return activities;
    }

    public void setActivities(UserActivities activities) {
        this.activities = activities;
    }

    public UserPsychology getPsychology() {
        return psychology;
    }

    public void setPsychology(UserPsychology psychology) {
        this.psychology = psychology;
    }
}
