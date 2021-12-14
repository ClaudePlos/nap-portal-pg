package pl.kskowronski.data.entity.egeria.ek;

public class EkSkladnikDTO {

    private String dskNazwa;

    private Double wartosc;

    public EkSkladnikDTO() {
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
