package rs.hookupspring.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Bandjur on 3/25/2017.
 */
@Entity
public class IgnoredPair {

    @Id
    @GeneratedValue
    private int id;

    private int userAId;
    private int userBId;
//    private String ignoreByUid;
    private Date ignoreDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserAId() {
        return userAId;
    }

    public void setUserAId(int userAId) {
        this.userAId = userAId;
    }

    public int getUserBId() {
        return userBId;
    }

    public void setUserBId(int userBId) {
        this.userBId = userBId;
    }

//    public String getIgnoreByUid() {
//        return ignoreByUid;
//    }
//
//    public void setIgnoreByUid(String ignoreByUid) {
//        this.ignoreByUid = ignoreByUid;
//    }

    public Date getIgnoreDate() {
        return ignoreDate;
    }

    public void setIgnoreDate(Date ignoreDate) {
        this.ignoreDate = ignoreDate;
    }
}
