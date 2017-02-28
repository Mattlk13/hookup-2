package rs.hookupspring.springweb.dto;

/**
 * Created by Bandjur on 2/28/2017.
 */
public class UserPersonalizationDto {

    private String uid;

    private UserBasicInfoDto basicInfo;
    private UserActivitiesDto activities;
    private UserPsychologyDto psychology;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserBasicInfoDto getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(UserBasicInfoDto basicInfo) {
        this.basicInfo = basicInfo;
    }

    public UserActivitiesDto getActivities() {
        return activities;
    }

    public void setActivities(UserActivitiesDto activities) {
        this.activities = activities;
    }

    public UserPsychologyDto getPsychology() {
        return psychology;
    }

    public void setPsychology(UserPsychologyDto psychology) {
        this.psychology = psychology;
    }
}
