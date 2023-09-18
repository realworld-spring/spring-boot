package real.world.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.regex.Pattern;
import lombok.Getter;
import real.world.error.exception.EmailInvalidException;
import real.world.error.exception.UsernameInvalidException;

@Getter
@Entity(name = "users")
public class User {

    private static final int MAX_USERNAME_LENGTH = 15;

    private static final int MAX_EMAIL_LENGTH = 30;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = MAX_USERNAME_LENGTH)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = MAX_EMAIL_LENGTH)
    private String email;

    private String bio;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    protected User() {
    }

    public User(String username, String password, String email, String bio, String imageUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.role = UserRole.ROLE_USER;
        validate();
    }

    public void update(String username, String password, String email, String bio, String imageUrl) {
        updateUsername(username);
        updatePassword(password);
        updateEmail(email);
        updateBio(bio);
        updateImageUrl(imageUrl);
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

    private void updateUsername(String username) {
        if(username != null) { this.username = username; }
    }

    private void updatePassword(String password) {
        if(password != null) { this.password = password; }
    }

    private void updateEmail(String email) {
        if(email != null) { this.email = email; }
    }

    private void updateBio(String bio) {
        if(bio != null) { this.bio = bio; }
    }

    private void updateImageUrl(String imageUrl) {
        if(imageUrl != null) { this.imageUrl = imageUrl; }
    }

}