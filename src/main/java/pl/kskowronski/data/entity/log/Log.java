package pl.kskowronski.data.entity.log;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NPP_LOG")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "PRC_ID")
    private Integer prcId;

    @Column(name = "EVENT")
    private String event;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AUDIT_DC")
    private Date auditDc;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SK")
    private String sk;

    @Column(name = "COMPANY")
    private String company;

    public Log() {
    }

    public Log(Integer id, Integer prcId, String event, Date auditDc, String description, String sk, String company) {
        this.id = id;
        this.prcId = prcId;
        this.event = event;
        this.auditDc = auditDc;
        this.description = description;
        this.sk = sk;
        this.company = company;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrcId() {
        return prcId;
    }

    public void setPrcId(Integer prcId) {
        this.prcId = prcId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getAuditDc() {
        return auditDc;
    }

    public void setAuditDc(Date auditDc) {
        this.auditDc = auditDc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
