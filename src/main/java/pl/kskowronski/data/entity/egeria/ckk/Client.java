package pl.kskowronski.data.entity.egeria.ckk;

import javax.persistence.*;

@Entity
@Table(name = "napwfv_kontrahenci")
public class Client {

    @Id
    @Column(name="kl_kod")
    private Integer klKod;

    @Column(name="kld_Id")
    private Integer kldId;

    @Column(name="kl_skrot")
    private String klSkrot;

    @Column(name="kld_typ")
    private String kldTyp;

    @Column(name="kld_nazwa")
    private String kldNazwa;

    @Column(name="kld_nip")
    private String kldNip;

    @Column(name="kld_pesel")
    private String kldPesel;

    @Column(name="kld_regon")
    private String kldRegon;

    @Column(name="kld_forma_wlasnosci")
    private String kldFormaWlasnosci;

    @Column(name="kld_zatwierdzony")
    private String kldZatwierdzony;

    @Column(name="kld_miejsce_urodzenia")
    private String kldMiejsceUrodzenia;

    @Column(name="kld_krs")
    private String kldKrs;

    @Column(name="kld_pkd")
    private String kldPkd;

    public Client() {
    }

    public Integer getKlKod() {
        return klKod;
    }

    public void setKlKod(Integer klKod) {
        this.klKod = klKod;
    }

    public Integer getKldId() {
        return kldId;
    }

    public void setKldId(Integer kldId) {
        this.kldId = kldId;
    }

    public String getKlSkrot() {
        return klSkrot;
    }

    public void setKlSkrot(String klSkrot) {
        this.klSkrot = klSkrot;
    }

    public String getKldTyp() {
        return kldTyp;
    }

    public void setKldTyp(String kldTyp) {
        this.kldTyp = kldTyp;
    }

    public String getKldNazwa() {
        return kldNazwa;
    }

    public void setKldNazwa(String kldNazwa) {
        this.kldNazwa = kldNazwa;
    }

    public String getKldNip() {
        return kldNip;
    }

    public void setKldNip(String kldNip) {
        this.kldNip = kldNip;
    }

    public String getKldPesel() {
        return kldPesel;
    }

    public void setKldPesel(String kldPesel) {
        this.kldPesel = kldPesel;
    }

    public String getKldRegon() {
        return kldRegon;
    }

    public void setKldRegon(String kldRegon) {
        this.kldRegon = kldRegon;
    }

    public String getKldFormaWlasnosci() {
        return kldFormaWlasnosci;
    }

    public void setKldFormaWlasnosci(String kldFormaWlasnosci) {
        this.kldFormaWlasnosci = kldFormaWlasnosci;
    }

    public String getKldZatwierdzony() {
        return kldZatwierdzony;
    }

    public void setKldZatwierdzony(String kldZatwierdzony) {
        this.kldZatwierdzony = kldZatwierdzony;
    }

    public String getKldMiejsceUrodzenia() {
        return kldMiejsceUrodzenia;
    }

    public void setKldMiejsceUrodzenia(String kldMiejsceUrodzenia) {
        this.kldMiejsceUrodzenia = kldMiejsceUrodzenia;
    }

    public String getKldKrs() {
        return kldKrs;
    }

    public void setKldKrs(String kldKrs) {
        this.kldKrs = kldKrs;
    }

    public String getKldPkd() {
        return kldPkd;
    }

    public void setKldPkd(String kldPkd) {
        this.kldPkd = kldPkd;
    }
}
