package real.world.domain.user.entity;

public enum UserRole {
    ROLE_USER,
    ROLE_ADMIN;

    public String getValue() {
        return this.toString();
    }
}