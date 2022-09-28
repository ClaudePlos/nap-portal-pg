package pl.kskowronski.data.entity.egeria;

import java.math.BigDecimal;

public class AdvancePaymentDTO {

    private String frmName;
    private String creationDate;
    private String rozNumber;
    private BigDecimal dtAmount;
    private BigDecimal ctAmount;
    private String description;
    private String clearingDate;
    private String whetherClearing;

    public String getFrmName() {
        return frmName;
    }

    public void setFrmName(String frmName) {
        this.frmName = frmName;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getRozNumber() {
        return rozNumber;
    }

    public void setRozNumber(String rozNumber) {
        this.rozNumber = rozNumber;
    }

    public BigDecimal getDtAmount() {
        return dtAmount;
    }

    public void setDtAmount(BigDecimal dtAmount) {
        this.dtAmount = dtAmount;
    }

    public BigDecimal getCtAmount() {
        return ctAmount;
    }

    public void setCtAmount(BigDecimal ctAmount) {
        this.ctAmount = ctAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClearingDate() {
        return clearingDate;
    }

    public void setClearingDate(String clearingDate) {
        this.clearingDate = clearingDate;
    }

    public String getWhetherClearing() {
        return whetherClearing;
    }

    public void setWhetherClearing(String whetherClearing) {
        this.whetherClearing = whetherClearing;
    }
}
