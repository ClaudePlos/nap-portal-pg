package pl.kskowronski.data.entity.admin;

import pl.kskowronski.data.entity.egeria.css.SK;
import pl.kskowronski.data.entity.egeria.global.NapUser;

import javax.persistence.*;

@Entity
@Table(name = "npp_sk_for_supervisor")
public class NppSkForSupervisor {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SK_ID")
    private Integer skId;

    @Column(name = "PRC_ID")
    private Integer prcId;

    @Column(name = "PRC_NAZWISKO_IMIE")
    private String nazwImie;

    @Column(name = "SK_KOD")
    private String skKod;

    @Transient
    private SK sk;

    @Transient
    private NapUser napUser;

    public NppSkForSupervisor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSkId() {
        return skId;
    }

    public void setSkId(Integer skId) {
        this.skId = skId;
    }

    public Integer getPrcId() {
        return prcId;
    }

    public void setPrcId(Integer prcId) {
        this.prcId = prcId;
    }

    public String getNazwImie() {
        return nazwImie;
    }

    public void setNazwImie(String nazwImie) {
        this.nazwImie = nazwImie;
    }

    public String getSkKod() {
        return skKod;
    }

    public void setSkKod(String skKod) {
        this.skKod = skKod;
    }

    public SK getSk() {
        return sk;
    }

    public void setSk(SK sk) {
        this.sk = sk;
        this.skId = sk.getSkId();
        this.skKod = sk.getSkKod();
    }

    public NapUser getNapUser() {
        return napUser;
    }

    public void setNapUser(NapUser napUser) {
        this.napUser = napUser;
        this.prcId = napUser.getPrcId();
        if (this.nazwImie == null) {
            this.nazwImie = napUser.getUsername();
        }
    }
}