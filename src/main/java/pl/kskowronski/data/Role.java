package pl.kskowronski.data;

public enum Role {
    USER("user"), ADMIN("admin"), SUPERVISOR("supervisor");

    private String roleName;

    private Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

}
