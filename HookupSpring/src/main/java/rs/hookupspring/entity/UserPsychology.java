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
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double attrA = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double intelA = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double ambA = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double sincA = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double funA = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double sharA = 0.0;


    // B stands for 2_1, what user thinks the opposite sex looks for
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double attrB = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double intelB = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double ambB = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double sincB = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double funB = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double sharB = 0.0;

    // C stands for 3_1, how user measure himself/herself up
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double attrC = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double intelC = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double ambC = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double sincC = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double funC = 0.0;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private double sharC = 0.0;

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

    public double getAttrA() {
        return attrA;
    }

    public void setAttrA(double attrA) {
        this.attrA = attrA;
    }

    public double getIntelA() {
        return intelA;
    }

    public void setIntelA(double intelA) {
        this.intelA = intelA;
    }

    public double getAmbA() {
        return ambA;
    }

    public void setAmbA(double ambA) {
        this.ambA = ambA;
    }

    public double getSincA() {
        return sincA;
    }

    public void setSincA(double sincA) {
        this.sincA = sincA;
    }

    public double getFunA() {
        return funA;
    }

    public void setFunA(double funA) {
        this.funA = funA;
    }

    public double getSharA() {
        return sharA;
    }

    public void setSharA(double sharA) {
        this.sharA = sharA;
    }

    public double getAttrB() {
        return attrB;
    }

    public void setAttrB(double attrB) {
        this.attrB = attrB;
    }

    public double getIntelB() {
        return intelB;
    }

    public void setIntelB(double intelB) {
        this.intelB = intelB;
    }

    public double getAmbB() {
        return ambB;
    }

    public void setAmbB(double ambB) {
        this.ambB = ambB;
    }

    public double getSincB() {
        return sincB;
    }

    public void setSincB(double sincB) {
        this.sincB = sincB;
    }

    public double getFunB() {
        return funB;
    }

    public void setFunB(double funB) {
        this.funB = funB;
    }

    public double getSharB() {
        return sharB;
    }

    public void setSharB(double sharB) {
        this.sharB = sharB;
    }

    public double getAttrC() {
        return attrC;
    }

    public void setAttrC(double attrC) {
        this.attrC = attrC;
    }

    public double getIntelC() {
        return intelC;
    }

    public void setIntelC(double intelC) {
        this.intelC = intelC;
    }

    public double getAmbC() {
        return ambC;
    }

    public void setAmbC(double ambC) {
        this.ambC = ambC;
    }

    public double getSincC() {
        return sincC;
    }

    public void setSincC(double sincC) {
        this.sincC = sincC;
    }

    public double getFunC() {
        return funC;
    }

    public void setFunC(double funC) {
        this.funC = funC;
    }

    public double getSharC() {
        return sharC;
    }

    public void setSharC(double sharC) {
        this.sharC = sharC;
    }
}
