package real.world.domain.user.entity;

public enum UserRole {
    ROLE_USER,
    ROLE_ANONYMOUS;

    public String getValue() {
        return this.toString();
    }

}