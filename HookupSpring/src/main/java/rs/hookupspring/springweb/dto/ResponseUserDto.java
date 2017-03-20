package rs.hookupspring.springweb.dto;

import rs.hookupspring.common.enums.Enums;
import rs.hookupspring.entity.User;
import rs.hookupspring.entity.UserActivities;
import rs.hookupspring.entity.UserBasicInfo;
import rs.hookupspring.entity.UserPsychology;

import java.util.Date;

/**
 * Created by Bandjur on 3/19/2017.
 */
public class ResponseUserDto {

    private int id;

//    private String email;
    private String firstname;
    private String lastname;
    private String firebaseUID;
    private String firebaseInstaceToken;

//    private Double latitude;
//    private Double longitude;

    private String city; //
    private String country; //
    private int age; //

//    private Enums.Gender gender;
//    private Date birthDate;
    private String aboutMe;

    private Date friendsDate;
    private Date notificationReceivedDate;

    private UserBasicInfo basicInfo;
    private UserActivities activities;
    private UserPsychology psychology;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirebaseUID() {
        return firebaseUID;
    }

    public void setFirebaseUID(String firebaseUID) {
        this.firebaseUID = firebaseUID;
    }

    public String getFirebaseInstaceToken() {
        return firebaseInstaceToken;
    }

    public void setFirebaseInstaceToken(String firebaseInstaceToken) {
        this.firebaseInstaceToken = firebaseInstaceToken;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Date getFriendsDate() {
        return friendsDate;
    }

    public void setFriendsDate(Date friendsDate) {
        this.friendsDate = friendsDate;
    }

    public Date getNotificationReceivedDate() {
        return notificationReceivedDate;
    }

    public void setNotificationReceivedDate(Date notificationReceivedDate) {
        this.notificationReceivedDate = notificationReceivedDate;
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
