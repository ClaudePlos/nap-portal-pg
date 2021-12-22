package pl.kskowronski.data.entity.egeria.eDek;

//EDKT_DEKLARACJE

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "EDKT_DEKLARACJE")
public class EdktDeklaracje {

    @Id
    @Column(name = "DKL_ID")
    private Integer dklId;

    @Column(name = "DKL_STATUS")
    private Integer dklStatus;

    @Column(name = "DKL_TDL_KOD") // Pit11
    private String dklTdlKod;

    @Column(name = "DKL_PRC_ID")
    private Integer dklPrcId;

    @Column(name = "DKL_DATA_OD")
    private Date dklDataOd;

    @Column(name = "DKL_DATA_DO")
    private Date dklDataDo;

    @Column(name = "DKL_XML_WIZUAL")
    private String dklXmlVisual;

    @Column(name = "DKL_FRM_ID")
    private Integer dklFrmId;

    public EdktDeklaracje() {
    }

    public Integer getDklId() {
        return dklId;
    }

    public void setDklId(Integer dklId) {
        this.dklId = dklId;
    }

    public Integer getDklStatus() {
        return dklStatus;
    }

    public void setDklStatus(Integer dklStatus) {
        this.dklStatus = dklStatus;
    }

    public String getDklTdlKod() {
        return dklTdlKod;
    }

    public void setDklTdlKod(String dklTdlKod) {
        this.dklTdlKod = dklTdlKod;
    }

    public Integer getDklPrcId() {
        return dklPrcId;
    }

    public void setDklPrcId(Integer dklPrcId) {
        this.dklPrcId = dklPrcId;
    }

    public Date getDklDataOd() {
        return dklDataOd;
    }

    public void setDklDataOd(Date dklDataOd) {
        this.dklDataOd = dklDataOd;
    }

    public Date getDklDataDo() {
        return dklDataDo;
    }

    public void setDklDataDo(Date dklDataDo) {
        this.dklDataDo = dklDataDo;
    }

    public String getDklXmlVisual() {
        return dklXmlVisual;
    }

    public void setDklXmlVisual(String dklXmlVisual) {
        this.dklXmlVisual = dklXmlVisual;
    }

    public Integer getDklFrmId() {
        return dklFrmId;
    }

    public void setDklFrmId(Integer dklFrmId) {
        this.dklFrmId = dklFrmId;
    }
}
