package pl.kskowronski.data.entity.egeria.ek;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ek_zatrudnienie")
public class Zatrudnienie {

    @Id
    @Column(name = "zat_id")
    private Integer zatId;

    @Temporal(TemporalType.DATE)
    @Column(name = "zat_data_przyj")
    private Date zatDataPrzyj;

    @Temporal(TemporalType.DATE)
    @Column(name = "zat_Data_Zmiany")
    private Date zatDataZmiany;

    @Temporal(TemporalType.DATE)
    @Column(name = "zat_Data_Zwolnienia")
    private Date zatDataZwolnienia;

    @Temporal(TemporalType.DATE)
    @Column(name = "zat_Data_Do")
    private Date zatDataDo;

    @Column(name = "zat_typ_umowy")
    private Integer zatTypUmowy;

    @Column(name = "zat_sk_id")
    private Integer zatSkId;

    @Column(name = "zat_ob_id")
    private Integer zatObId;

    @Column(name = "zat_aneks")
    private String zatAneks;

    @Column(name = "zat_prc_id")
    private Integer zatPrcId;

    @Column(name = "zat_zat_id")
    private Integer zatZatId;

    @Column(name = "zat_f_rodzaj")
    private String zatFRodzaj;

    @Column(name = "zat_typ_angaz")
    private String zatTypAngaz;

    @Column(name = "zat_typ_stawki")
    private String zatTypStawki;

    @Column(name = "zat_temat")
    private String zatTemat;

    @Column(name = "zat_nr_umowy")
    private String zatNrUmowy;

    @Column(name="zat_stawka")
    private Integer zatStawka;

    @Column(name="zat_inne_warunki")
    private String zatInneWarunki;

    @Column(name="zat_gprc_kod")
    private String zatGprcKod;

    @Column(name="zat_pp_id")
    private Integer ppId;

    @Column(name="zat_def_0")
    private String def0;
    @Column(name="zat_def_1")
    private String def1;
    @Column(name="zat_def_2")
    private String def2;
    @Column(name="zat_def_3")
    private String def3;
    @Column(name="zat_def_4")
    private String def4;
    @Column(name="zat_def_5")
    private String def5;
    @Column(name="zat_def_6")
    private String def6;
    @Column(name="zat_def_7")
    private String def7;
    @Column(name="zat_def_8")
    private String def8;
    @Column(name="zat_def_9")
    private String def9;

    @Column(name="zat_def_52")
    private String def52;

    @Column(name="zat_ubezp_ob_emeryt")
    private String zatUbezpObEmeryt;

    @Column(name="zat_stn_id")
    private Integer zatStnId;

    @Column(name="zat_frm_id")
    private Integer frmId;

    @Column(name="zat_wymiar")
    private Integer zatWymiar;

    @Transient
    private WymiarEtatu wymiarEtatu;

    @Transient
    private String frmNazwa;

    @Transient
    private String joName;

    public Zatrudnienie() {
    }

    public boolean isSecondContractOnHours(){
        if (getZatTypStawki().equals("G") && getZatUbezpObEmeryt().equals("N")){
            return true;
        }
        return false;
    }

    public Integer getZatId() {
        return zatId;
    }

    public void setZatId(Integer zatId) {
        this.zatId = zatId;
    }

    public Date getZatDataPrzyj() {
        return zatDataPrzyj;
    }

    public void setZatDataPrzyj(Date zatDataPrzyj) {
        this.zatDataPrzyj = zatDataPrzyj;
    }

    public Date getZatDataZmiany() {
        return zatDataZmiany;
    }

    public void setZatDataZmiany(Date zatDataZmiany) {
        this.zatDataZmiany = zatDataZmiany;
    }

    public Date getZatDataZwolnienia() {
        return zatDataZwolnienia;
    }

    public void setZatDataZwolnienia(Date zatDataZwolnienia) {
        this.zatDataZwolnienia = zatDataZwolnienia;
    }

    public Date getZatDataDo() {
        return zatDataDo;
    }

    public void setZatDataDo(Date zatDataDo) {
        this.zatDataDo = zatDataDo;
    }

    public Integer getZatTypUmowy() {
        return zatTypUmowy;
    }

    public void setZatTypUmowy(Integer zatTypUmowy) {
        this.zatTypUmowy = zatTypUmowy;
    }

    public Integer getZatSkId() {
        return zatSkId;
    }

    public void setZatSkId(Integer zatSkId) {
        this.zatSkId = zatSkId;
    }

    public Integer getZatObId() {
        return zatObId;
    }

    public void setZatObId(Integer zatObId) {
        this.zatObId = zatObId;
    }

    public String getZatAneks() {
        return zatAneks;
    }

    public void setZatAneks(String zatAneks) {
        this.zatAneks = zatAneks;
    }

    public Integer getZatPrcId() {
        return zatPrcId;
    }

    public void setZatPrcId(Integer zatPrcId) {
        this.zatPrcId = zatPrcId;
    }

    public Integer getZatZatId() {
        return zatZatId;
    }

    public void setZatZatId(Integer zatZatId) {
        this.zatZatId = zatZatId;
    }

    public String getZatFRodzaj() {
        return zatFRodzaj;
    }

    public void setZatFRodzaj(String zatFRodzaj) {
        this.zatFRodzaj = zatFRodzaj;
    }

    public String getZatTypAngaz() {
        return zatTypAngaz;
    }

    public void setZatTypAngaz(String zatTypAngaz) {
        this.zatTypAngaz = zatTypAngaz;
    }

    public String getZatTypStawki() {
        return zatTypStawki;
    }

    public void setZatTypStawki(String zatTypStawki) {
        this.zatTypStawki = zatTypStawki;
    }

    public String getZatTemat() {
        return zatTemat;
    }

    public void setZatTemat(String zatTemat) {
        this.zatTemat = zatTemat;
    }

    public String getZatNrUmowy() {
        return zatNrUmowy;
    }

    public void setZatNrUmowy(String zatNrUmowy) {
        this.zatNrUmowy = zatNrUmowy;
    }

    public Integer getZatStawka() {
        return zatStawka;
    }

    public void setZatStawka(Integer zatStawka) {
        this.zatStawka = zatStawka;
    }

    public String getZatInneWarunki() {
        return zatInneWarunki;
    }

    public void setZatInneWarunki(String zatInneWarunki) {
        this.zatInneWarunki = zatInneWarunki;
    }

    public String getZatGprcKod() {
        return zatGprcKod;
    }

    public void setZatGprcKod(String zatGprcKod) {
        this.zatGprcKod = zatGprcKod;
    }

    public Integer getPpId() {
        return ppId;
    }

    public void setPpId(Integer ppId) {
        this.ppId = ppId;
    }

    public String getDef0() {
        return def0;
    }

    public void setDef0(String def0) {
        this.def0 = def0;
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1;
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2;
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3;
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4;
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5;
    }

    public String getDef6() {
        return def6;
    }

    public void setDef6(String def6) {
        this.def6 = def6;
    }

    public String getDef7() {
        return def7;
    }

    public void setDef7(String def7) {
        this.def7 = def7;
    }

    public String getDef8() {
        return def8;
    }

    public void setDef8(String def8) {
        this.def8 = def8;
    }

    public String getDef9() {
        return def9;
    }

    public void setDef9(String def9) {
        this.def9 = def9;
    }

    public String getDef52() {
        return def52;
    }

    public void setDef52(String def52) {
        this.def52 = def52;
    }

    public String getZatUbezpObEmeryt() {
        return zatUbezpObEmeryt;
    }

    public void setZatUbezpObEmeryt(String zatUbezpObEmeryt) {
        this.zatUbezpObEmeryt = zatUbezpObEmeryt;
    }

    public Integer getZatStnId() {
        return zatStnId;
    }

    public void setZatStnId(Integer zatStnId) {
        this.zatStnId = zatStnId;
    }

    public Integer getFrmId() {
        return frmId;
    }

    public void setFrmId(Integer frmId) {
        this.frmId = frmId;
    }

    public Integer getZatWymiar() {
        return zatWymiar;
    }

    public void setZatWymiar(Integer zatWymiar) {
        this.zatWymiar = zatWymiar;
    }

    public WymiarEtatu getWymiarEtatu() {
        return wymiarEtatu;
    }

    public void setWymiarEtatu(WymiarEtatu wymiarEtatu) {
        this.wymiarEtatu = wymiarEtatu;
    }

    public String getFrmNazwa() {
        return frmNazwa;
    }

    public void setFrmNazwa(String frmNazwa) {
        this.frmNazwa = frmNazwa;
    }

    public String getJoName() {
        return joName;
    }

    public void setJoName(String joName) {
        this.joName = joName;
    }
}
