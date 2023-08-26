package real.world.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import real.world.error.exception.EmailInvalidException;
import real.world.error.exception.UsernameInvalidException;

@Getter
@Entity(name = "users")
public class User {

    private static final int MAX_USERNAME_LENGTH = 15;

    private static final int MAX_EMAIL_LENGTH = 15;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");

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
        validate();
    }

    private void validate() {
        validateUsername();
        validateEmail();
    }

    private void validateUsername() {
        if(this.username.length() > MAX_USERNAME_LENGTH) {
            throw new UsernameInvalidException();
        }
    }

    private void validateEmail() {
        if(this.email.length() > MAX_EMAIL_LENGTH || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new EmailInvalidException();
        }
    }

}
