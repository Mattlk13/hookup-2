package rs.hookupspring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Bandjur on 8/27/2016.
 */
@Entity
public class Hookup {

    @Id
    @GeneratedValue
    private int id;

    private int userAId;
    private int userBId;

    private boolean paired;
    private Date hookupPairCreated;
    @Column(nullable = true)
    private Date hookupRequestDate;
    @Column(nullable = true)
    private Date hookupPairedDate;
    private int hookupPositiveResponseCount;
    private int hookupRequestSentCount;
    private boolean recommended;
//    @Column(nullable = true)
    private boolean userAResponse;
//    @Column(nullable = true)
    private boolean userBResponse;

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

    public boolean isPaired() {
        return paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    public Date getHookupPairCreated() {
        return hookupPairCreated;
    }

    public void setHookupPairCreated(Date hookupPairCreated) {
        this.hookupPairCreated = hookupPairCreated;
    }

    public Date getHookupRequestDate() {
        return hookupRequestDate;
    }

    public void setHookupRequestDate(Date hookupRequestDate) {
        this.hookupRequestDate = hookupRequestDate;
    }

    public int getHookupPositiveResponseCount() {
        return hookupPositiveResponseCount;
    }

    public void setHookupPositiveResponseCount(int hookupPositiveResponseCount) {
        this.hookupPositiveResponseCount = hookupPositiveResponseCount;
    }

    public Date getHookupPairedDate() {
        return hookupPairedDate;
    }

    public void setHookupPairedDate(Date hookupPairedDate) {
        this.hookupPairedDate = hookupPairedDate;
    }

    public int getHookupRequestSentCount() {
        return hookupRequestSentCount;
    }

    public void setHookupRequestSentCount(int hookupRequestSentCount) {
        this.hookupRequestSentCount = hookupRequestSentCount;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public boolean isUserAResponse() {
        return userAResponse;
    }

    public void setUserAResponse(boolean userAResponse) {
        this.userAResponse = userAResponse;
    }

    public boolean isUserBResponse() {
        return userBResponse;
    }

    public void setUserBResponse(boolean userBResponse) {
        this.userBResponse = userBResponse;
    }

}
