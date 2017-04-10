package matchbaker.model;

import java.io.Serializable;

/**
 * Created by Bandjur on 2/28/2017.
 */

public class UserBasicInfoForJson implements Serializable {

    private String race;
    private String field;
    private String career;
    private int imprace;
    private int imprelig;

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
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
