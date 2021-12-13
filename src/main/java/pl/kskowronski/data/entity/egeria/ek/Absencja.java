package pl.kskowronski.data.entity.egeria.ek;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ek_absencje")
public class Absencja {

    @Id
    @Column(name = "ab_id")
    private Integer abId;

    @Column(name = "ab_zwol_id", nullable = false)
    private Integer abZwolId;

    @Column(name = "ab_rda_id", nullable = false)
    private Integer abRdaId;

    @Column(name = "ab_prc_id", nullable = false)
    private Integer abPrcId;

    @Column(name = "ab_data_od", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date abDataOd;

    @Column(name = "ab_data_do", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date abDataDo;

    @Column(name = "ab_kod_funduszu")
    private String abKodFunduszu;

    @Column(name = "ab_dni_wykorzystane")
    private Integer abDniWykorzystane;

    @Column(name = "ab_godziny_wykorzystane")
    private Integer abGodzinyWykorzystane;

    @Column(name = "ab_f_anulowana")
    private String abFanulowana;

    public Absencja() {
    }

    public Integer getAbId() {
        return abId;
    }

    public void setAbId(Integer abId) {
        this.abId = abId;
    }

    public Integer getAbZwolId() {
        return abZwolId;
    }

    public void setAbZwolId(Integer abZwolId) {
        this.abZwolId = abZwolId;
    }

    public Integer getAbRdaId() {
        return abRdaId;
    }

    public void setAbRdaId(Integer abRdaId) {
        this.abRdaId = abRdaId;
    }

    public Integer getAbPrcId() {
        return abPrcId;
    }

    public void setAbPrcId(Integer abPrcId) {
        this.abPrcId = abPrcId;
    }

    public Date getAbDataOd() {
        return abDataOd;
    }

    public void setAbDataOd(Date abDataOd) {
        this.abDataOd = abDataOd;
    }

    public Date getAbDataDo() {
        return abDataDo;
    }

    public void setAbDataDo(Date abDataDo) {
        this.abDataDo = abDataDo;
    }

    public String getAbKodFunduszu() {
        return abKodFunduszu;
    }

    public void setAbKodFunduszu(String abKodFunduszu) {
        this.abKodFunduszu = abKodFunduszu;
    }

    public Integer getAbDniWykorzystane() {
        return abDniWykorzystane;
    }

    public void setAbDniWykorzystane(Integer abDniWykorzystane) {
        this.abDniWykorzystane = abDniWykorzystane;
    }

    public Integer getAbGodzinyWykorzystane() {
        return abGodzinyWykorzystane;
    }

    public void setAbGodzinyWykorzystane(Integer abGodzinyWykorzystane) {
        this.abGodzinyWykorzystane = abGodzinyWykorzystane;
    }

    public String getAbFanulowana() {
        return abFanulowana;
    }

    public void setAbFanulowana(String abFanulowana) {
        this.abFanulowana = abFanulowana;
    }
}
