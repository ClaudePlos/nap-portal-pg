package pl.kskowronski.data.entity.admin;

import pl.kskowronski.data.Role;
import pl.kskowronski.data.entity.egeria.ek.Zatrudnienie;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "EK_PRACOWNICY")
public class User {

    @Id
    @Column(name = "PRC_ID")
    private Integer prcId;

    @Column(name = "PRC_NUMER1", insertable=false, updatable=false)
    private String username;

    @Column(name = "PRC_NUMER")
    private Integer prcNumer;

    @Column(name = "PRC_IDENTYFIKACJA")
    private String password;

    @Column(name = "PRC_PESEL")
    private String prcPesel;

    @Column(name = "PRC_IMIE")
    private String prcImie;

    @Column(name = "PRC_NAZWISKO")
    private String prcNazwisko;

    @Column(name = "PRC_DG_KOD_EK")
    private String prcDgKodEk;

    @ElementCollection(fetch = FetchType.EAGER)
    @Transient
    private Set<Role> roles;

    @Transient
    private List<Zatrudnienie> zatrudnienia;

    public String getNazwImie () {
        return prcNazwisko + " " + prcImie;
    }

    public User() {
    }

    public User(Integer prcNumer, String password, String prcImie, String prcNazwisko) {
        this.prcNumer = prcNumer;
        this.password = password;
        this.prcImie = prcImie;
        this.prcNazwisko = prcNazwisko;
    }

    public Integer getPrcId() {
        return prcId;
    }

    public void setPrcId(Integer prcId) {
        this.prcId = prcId;
    }

    public Integer getPrcNumer() {
        return prcNumer;
    }

    public void setPrcNumer(Integer prcNumer) {
        this.prcNumer = prcNumer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrcImie() {
        return prcImie;
    }

    public void setPrcImie(String prcImie) {
        this.prcImie = prcImie;
    }

    public String getPrcNazwisko() {
        return prcNazwisko;
    }

    public void setPrcNazwisko(String prcNazwisko) {
        this.prcNazwisko = prcNazwisko;
    }

    public String getPrcPesel() {
        return prcPesel;
    }

    public void setPrcPesel(String prcPesel) {
        this.prcPesel = prcPesel;
    }

    public List<Zatrudnienie> getZatrudnienia() {
        return zatrudnienia;
    }

    public void setZatrudnienia(List<Zatrudnienie> zatrudnienia) {
        this.zatrudnienia = zatrudnienia;
    }

    public String getPrcDgKodEk() {
        return prcDgKodEk;
    }

    public void setPrcDgKodEk(String prcDgKodEk) {
        this.prcDgKodEk = prcDgKodEk;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
