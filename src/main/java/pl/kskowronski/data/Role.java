package pl.kskowronski.data;

public enum Role {
    // SUPERVISOR - we use this only when manager want print Pit for workers
    USER("user"), ADMIN("admin"), SUPERVISOR("supervisor"), MANAGER("manager");

    private String roleName;

    private Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

}
