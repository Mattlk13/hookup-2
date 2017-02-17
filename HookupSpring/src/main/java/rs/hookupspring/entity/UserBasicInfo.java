package rs.hookupspring.entity;

import rs.hookupspring.common.enums.Enums;

import javax.persistence.*;

/**
 * Created by Bandjur on 2/15/2017.
 */

@Entity
public class UserBasicInfo {

    @Id
    @GeneratedValue
    public int rowId;

    private Enums.Race race;
    private Enums.Field field;
    private Enums.Career career;
//    private Enums.GoOut goOut;

    private int imprace;
    private int imprelig;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public Enums.Race getRace() {
        return race;
    }

    public void setRace(Enums.Race race) {
        this.race = race;
    }

    public Enums.Field getField() {
        return field;
    }

    public void setField(Enums.Field field) {
        this.field = field;
    }

    public Enums.Career getCareer() {
        return career;
    }

    public void setCareer(Enums.Career career) {
        this.career = career;
    }

//    public Enums.GoOut getGoOut() {
//        return goOut;
//    }
//
//    public void setGoOut(Enums.GoOut goOut) {
//        this.goOut = goOut;
//    }

    public int getImprace() {
        return imprace;
    }

    public void setImprace(int imprace) {
        this.imprace = imprace;
    }

    public int getImprelig() {
        return imprelig;
    }

    public void setImprelig(int imprelig) {
        this.imprelig = imprelig;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
