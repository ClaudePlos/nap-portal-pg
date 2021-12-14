package pl.kskowronski.data.entity.egeria.ek;

import javax.persistence.*;

@Entity
@Table(name="EK_GRUPY_KODOW")
public class EkGroupCode {

    @Column(name = "GK_DG_KOD")
    private String gkDgKod;

    @Id
    @Column(name = "GK_DSK_ID")
    private Integer gkDskId;

    @Column(name = "GK_NUMER")
    private Integer gkNumer;

    @Transient
    private Integer dskKod;

    @Transient
    private String dskNazwa;

    @Transient
    private Double wartosc;

    public EkGroupCode() {
    }

    public String getGkDgKod() {
        return gkDgKod;
    }

    public void setGkDgKod(String gkDgKod) {
        this.gkDgKod = gkDgKod;
    }

    public Integer getGkDskId() {
        return gkDskId;
    }

    public void setGkDskId(Integer gkDskId) {
        this.gkDskId = gkDskId;
    }

    public Integer getGkNumer() {
        return gkNumer;
    }

    public void setGkNumer(Integer gkNumer) {
        this.gkNumer = gkNumer;
    }

    public Integer getDskKod() {
        return dskKod;
    }

    public void setDskKod(Integer dskKod) {
        this.dskKod = dskKod;
    }

    public String getDskNazwa() {
        return dskNazwa;
    }

    public void setDskNazwa(String dskNazwa) {
        this.dskNazwa = dskNazwa;
    }

    public Double getWartosc() {
        return wartosc;
    }

    public void setWartosc(Double wartosc) {
        this.wartosc = wartosc;
    }
}
