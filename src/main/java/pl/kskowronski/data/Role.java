package pl.kskowronski.data;

public enum Role {
    // SUPERVISOR - we use this only when manager want print Pit for workers
    USER("user")
    , ADMIN("admin")
    , SUPERVISOR("supervisor") // access only to list with own workers pits
    , MANAGER("manager") // access to reports
    , HR_MANAGER("hr_manager") // only for manager from HR department
    , EK04("EK04");

    private String roleName;

    private Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

}
