package real.world.security.support;

import lombok.Getter;

@Getter
public class OptionalRequest {

    final private String path;

    final private String[] methods;

    public OptionalRequest(String path, String[] methods) {
        this.path = path;
        this.methods = methods;
    }

    public static OptionalRequest of(String path, String... methods) {
        return new OptionalRequest(path, methods);
    }

    public static OptionalRequest of(String path) {
        return OptionalRequest.of(path, "GET", "POST", "PUT", "DELETE");
    }

}