package hookupandroid.model;

import java.io.Serializable;

import hookupandroid.common.enums.PersonRelation;

/**
 * Created by Bandjur on 8/3/2016.
 */
public class Person implements Serializable {
    private int tempPosition;
    private PersonRelation personRelation;
    private String id;
    private String firstname;
    private String lastname;
    private String age;
    private String address;
    private String city;
    private String country;
    private int image_id;

    public Person() {

    }

    public Person(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getTempPosition() {
        return tempPosition;
    }

    public void setTempPosition(int tempPostion) {
        this.tempPosition = tempPostion;
    }

    public PersonRelation getPersonRelation() {
        return personRelation;
    }

    public void setPersonRelation(PersonRelation personRelation) {
        this.personRelation = personRelation;
    }
}
