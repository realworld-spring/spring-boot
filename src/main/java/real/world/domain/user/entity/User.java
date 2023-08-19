package real.world.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String email;

    private String bio;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    protected User() {
    }

    public User(String username, String password, String email, String bio, String imageUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.imageUrl = imageUrl;
    }
}
