package pl.kskowronski.data.entity.admin;

public class NppFacilityManagersDTO {

    private Integer prcId;
    private String  name;
    private String  surname;
    private Integer prcNumber;
    private Integer skId;
    private String  skKod;

    public NppFacilityManagersDTO() {
    }

    public Integer getPrcId() {
        return prcId;
    }

    public void setPrcId(Integer prcId) {
        this.prcId = prcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getPrcNumber() {
        return prcNumber;
    }

    public void setPrcNumber(Integer prcNumber) {
        this.prcNumber = prcNumber;
    }

    public Integer getSkId() {
        return skId;
    }

    public void setSkId(Integer skId) {
        this.skId = skId;
    }

    public String getSkKod() {
        return skKod;
    }

    public void setSkKod(String skKod) {
        this.skKod = skKod;
    }
}
