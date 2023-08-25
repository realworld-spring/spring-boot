package real.world.fixture;

import lombok.Getter;
import org.springframework.test.util.ReflectionTestUtils;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.entity.User;

@Getter
public enum UserFixtures {

    JOHN("John", "1234", "john@email.com", "im john", "john.png"),
    ALICE("Alice", "1234", "alice@email.com", "im alice", "alice.png"),
    BOB("Bob", "1234", "bob@email.com", "im bob", "bob.png");

    private final String username;
    private final String password;
    private final String email;
    private final String bio;
    private final String imageUrl;

    UserFixtures(String username, String password, String email, String bio, String imageUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }

    public User 생성() {
        return 생성(0L);
    }

    public User 생성(Long id) {
        final User user = new User(
            this.username,
            this.password,
            this.email,
            this.bio,
            this.imageUrl
        );
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    public RegisterRequest 회원가입을_한다() {
        final RegisterRequest request = new RegisterRequest();
        ReflectionTestUtils.setField(request, "username", this.username);
        ReflectionTestUtils.setField(request, "password", this.password);
        ReflectionTestUtils.setField(request, "email", this.email);
        return request;
    }

}
