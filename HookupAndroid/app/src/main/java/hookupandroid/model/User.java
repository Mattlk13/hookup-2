package hookupandroid.model;

import java.io.Serializable;
import java.util.Date;

import hookupandroid.common.enums.PersonRelation;

/**
 * Created by Bandjur on 3/5/2017.
 */

public class User implements Serializable{
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
    private int tempPosition;

    private PersonRelation personRelation;
    private Date birthDate;
    private String aboutMe;

    private String imgUrl;

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getTempPosition() {
        return tempPosition;
    }

    public void setTempPosition(int tempPosition) {
        this.tempPosition = tempPosition;
    }

    public PersonRelation getPersonRelation() {
        return personRelation;
    }

    public void setPersonRelation(PersonRelation personRelation) {
        this.personRelation = personRelation;
    }
}
