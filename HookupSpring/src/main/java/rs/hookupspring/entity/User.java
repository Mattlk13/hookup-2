package rs.hookupspring.entity;

import javax.persistence.*;

/**
 * Created by Bandjur on 8/20/2016.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;

    private String email;
    private String firstname;
    private String lastname;
    private String firebaseUID;
    private String firebaseInstaceToken;

    private Double latitude;
    private Double longitude;

    private String city;
    private String country;
    private int age;

    @OneToOne
    @JoinColumn(name = "basicInfo_id")
    private UserBasicInfo basicInfo;

    @OneToOne
    @JoinColumn(name = "activities_id")
    private UserActivities activities;

    @OneToOne
    @JoinColumn(name = "psychology_id")
    private UserPsychology psychology;

//    @ManyToMany
//    @JoinTable(name="hookups")
//    private List<User> hookups;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User))
            return false;
        if (obj == this)
            return true;

        User that = (User) obj;

        if (firebaseUID.equals(that.firebaseUID)) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return firebaseUID.hashCode();
    }
}
