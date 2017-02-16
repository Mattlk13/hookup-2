package rs.hookupspring.entity;

import javax.persistence.*;

/**
 * Created by Bandjur on 2/16/2017.
 */
@Entity
public class UserPsychology {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // A stands for 1_1, what user looks in the opposite sex
    private int attrA;
    private int intelA;
    private int ambA;
    private int sincA;
    private int funA;
    private int sharA;


    // B stands for 2_1, what user thinks the opposite sex looks for
    private int attrB;
    private int intelB;
    private int ambB;
    private int sincB;
    private int funB;
    private int sharB;

    // C stands for 3_1, how user measure himself/herself up
    private int attrC;
    private int intelC;
    private int ambC;
    private int sincC;
    private int funC;
    private int sharC;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAttrA() {
        return attrA;
    }

    public void setAttrA(int attrA) {
        this.attrA = attrA;
    }

    public int getIntelA() {
        return intelA;
    }

    public void setIntelA(int intelA) {
        this.intelA = intelA;
    }

    public int getAmbA() {
        return ambA;
    }

    public void setAmbA(int ambA) {
        this.ambA = ambA;
    }

    public int getSincA() {
        return sincA;
    }

    public void setSincA(int sincA) {
        this.sincA = sincA;
    }

    public int getFunA() {
        return funA;
    }

    public void setFunA(int funA) {
        this.funA = funA;
    }

    public int getSharA() {
        return sharA;
    }

    public void setSharA(int sharA) {
        this.sharA = sharA;
    }

    public int getAttrB() {
        return attrB;
    }

    public void setAttrB(int attrB) {
        this.attrB = attrB;
    }

    public int getIntelB() {
        return intelB;
    }

    public void setIntelB(int intelB) {
        this.intelB = intelB;
    }

    public int getAmbB() {
        return ambB;
    }

    public void setAmbB(int ambB) {
        this.ambB = ambB;
    }

    public int getSincB() {
        return sincB;
    }

    public void setSincB(int sincB) {
        this.sincB = sincB;
    }

    public int getFunB() {
        return funB;
    }

    public void setFunB(int funB) {
        this.funB = funB;
    }

    public int getSharB() {
        return sharB;
    }

    public void setSharB(int sharB) {
        this.sharB = sharB;
    }

    public int getAttrC() {
        return attrC;
    }

    public void setAttrC(int attrC) {
        this.attrC = attrC;
    }

    public int getIntelC() {
        return intelC;
    }

    public void setIntelC(int intelC) {
        this.intelC = intelC;
    }

    public int getAmbC() {
        return ambC;
    }

    public void setAmbC(int ambC) {
        this.ambC = ambC;
    }

    public int getSincC() {
        return sincC;
    }

    public void setSincC(int sincC) {
        this.sincC = sincC;
    }

    public int getFunC() {
        return funC;
    }

    public void setFunC(int funC) {
        this.funC = funC;
    }

    public int getSharC() {
        return sharC;
    }

    public void setSharC(int sharC) {
        this.sharC = sharC;
    }
}
