package rs.hookupspring.springweb.dto;

import java.io.Serializable;

/**
 * Created by Bandjur on 2/28/2017.
 */
public class UserBasicInfoDto implements Serializable {

    private int race;
    private int field;
    private int career;
    private int imprace;
    private int imprelig;

    public int getRace() {
        return race;
    }

    public void setRace(int race) {
        this.race = race;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

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
}
