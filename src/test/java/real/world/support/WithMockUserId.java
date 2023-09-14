package real.world.support;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;
import real.world.domain.user.entity.UserRole;
import real.world.fixture.UserFixtures;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserIdFactory.class)
public @interface WithMockUserId {

    UserFixtures user();

    UserRole role() default UserRole.ROLE_USER;

}
