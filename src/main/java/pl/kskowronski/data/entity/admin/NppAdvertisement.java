package pl.kskowronski.data.entity.admin;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "npp_advertisements")
public class NppAdvertisement {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "LP")
    private String lp;

    @Column(name = "TEXT")
    private String text;

    public NppAdvertisement() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLp() {
        return lp;
    }

    public void setLp(String lp) {
        this.lp = lp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
