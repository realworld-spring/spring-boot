package real.world.domain.follow.controller;

import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.auth.annotation.Auth;
import real.world.domain.follow.service.FollowService;
import real.world.domain.follow.dto.response.ProfileResponse;
import real.world.domain.user.entity.UserRole;

@RestController
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/profiles/{username}/follow")
    public ResponseEntity<ProfileResponse> follow(@Auth Long loginId, @PathVariable("username") String username) {
        final ProfileResponse response = followService.follow(loginId, username);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/profiles/{username}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable("username") String username) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Long followerId;
        if(hasAuthority(authentication)) {
            followerId = Long.valueOf((String) authentication.getPrincipal());
        } else { followerId = null; }

        final ProfileResponse response = followService.getProfile(username, followerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/profiles/{username}/unfollow")
    public ResponseEntity<ProfileResponse> unfollow(@Auth Long loginId, @PathVariable("username") String username) {
        final ProfileResponse response = followService.unfollow(loginId, username);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private boolean hasAuthority(Authentication authentication) {
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        final GrantedAuthority requiredAuthority = new SimpleGrantedAuthority(UserRole.ROLE_USER.toString());

        return authorities.contains(requiredAuthority);
    }

}