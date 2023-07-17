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

    public Log() {
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
}
