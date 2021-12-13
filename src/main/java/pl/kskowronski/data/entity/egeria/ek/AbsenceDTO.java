package pl.kskowronski.data.entity.egeria.ek;

import java.util.Date;

public class AbsenceDTO {

    private Integer abId;
    private Integer abZwolId;
    private Integer abRdaId;
    private Integer abPrcId;
    private Date abDataOd;
    private Date abDataDo;
    private String abKodFunduszu;
    private Integer abDniWykorzystane;
    private Integer abGodzinyWykorzystane;
    private String abFanulowana;
    private String abFrmName;
    private String abTypeOfAbsence;

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

    public String getAbFrmName() {
        return abFrmName;
    }

    public void setAbFrmName(String abFrmName) {
        this.abFrmName = abFrmName;
    }

    public String getAbTypeOfAbsence() {
        return abTypeOfAbsence;
    }

    public void setAbTypeOfAbsence(String abTypeOfAbsence) {
        this.abTypeOfAbsence = abTypeOfAbsence;
    }
}
