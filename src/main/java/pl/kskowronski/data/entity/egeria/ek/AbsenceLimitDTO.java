package pl.kskowronski.data.entity.egeria.ek;

public class AbsenceLimitDTO {

    private Integer prcId;
    private Integer rok;
    private String ldOd;
    private String ldDo;
    private Double pozostaloUrlopu;
    private String kodUrlopu;
    private String nazwaWymiaru;
    private Integer frmId;
    private String frmNazwa;

    public AbsenceLimitDTO() {
    }

    public Integer getPrcId() {
        return prcId;
    }

    public void setPrcId(Integer prcId) {
        this.prcId = prcId;
    }

    public Integer getRok() {
        return rok;
    }

    public void setRok(Integer rok) {
        this.rok = rok;
    }

    public String getLdOd() {
        return ldOd;
    }

    public void setLdOd(String ldOd) {
        this.ldOd = ldOd;
    }

    public String getLdDo() {
        return ldDo;
    }

    public void setLdDo(String ldDo) {
        this.ldDo = ldDo;
    }

    public Double getPozostaloUrlopu() {
        return pozostaloUrlopu;
    }

    public void setPozostaloUrlopu(Double pozostaloUrlopu) {
        this.pozostaloUrlopu = pozostaloUrlopu;
    }

    public String getKodUrlopu() {
        return kodUrlopu;
    }

    public void setKodUrlopu(String kodUrlopu) {
        this.kodUrlopu = kodUrlopu;
    }

    public Integer getFrmId() {
        return frmId;
    }

    public void setFrmId(Integer frmId) {
        this.frmId = frmId;
    }

    public String getFrmNazwa() {
        return frmNazwa;
    }

    public void setFrmNazwa(String frmNazwa) {
        this.frmNazwa = frmNazwa;
    }

    public String getNazwaWymiaru() {
        return nazwaWymiaru;
    }

    public void setNazwaWymiaru(String nazwaWymiaru) {
        this.nazwaWymiaru = nazwaWymiaru;
    }
}
