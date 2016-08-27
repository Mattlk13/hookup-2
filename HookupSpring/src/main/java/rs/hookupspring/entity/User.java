package rs.hookupspring.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Bandjur on 8/20/2016.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private int rowId;

    private String email;
    private String firstname;
    private String lastname;
    private String firebaseUID;
    private String firebaseInstaceToken;

    private Double latitude;
    private Double longitude;

//    @ManyToMany
//    @JoinTable(name="hookups")
//    private List<User> hookups;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
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
