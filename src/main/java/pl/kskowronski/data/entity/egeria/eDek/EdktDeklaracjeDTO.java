package pl.kskowronski.data.entity.egeria.eDek;

import java.util.Date;

public class EdktDeklaracjeDTO {

    private Integer dklId;
    private Integer dklStatus;
    private String dklTdlKod;
    private Integer dklPrcId;
    private Date dklDataOd;
    private Date dklDataDo;
    private String dklXmlVisual;
    private Integer dklFrmId;
    private String dklFrmNazwa;
    private String dklYear;

    private String dklPrcNazwisko;
    private String dklPrcImie;


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

    public String getDklFrmNazwa() {
        return dklFrmNazwa;
    }

    public void setDklFrmNazwa(String dklFrmNazwa) {
        this.dklFrmNazwa = dklFrmNazwa;
    }

    public String getDklYear() {
        return dklYear;
    }

    public void setDklYear(String dklYear) {
        this.dklYear = dklYear;
    }

    public String getDklPrcNazwisko() {
        return dklPrcNazwisko;
    }

    public void setDklPrcNazwisko(String dklPrcNazwisko) {
        this.dklPrcNazwisko = dklPrcNazwisko;
    }

    public String getDklPrcImie() {
        return dklPrcImie;
    }

    public void setDklPrcImie(String dklPrcImie) {
        this.dklPrcImie = dklPrcImie;
    }
}
