package real.world.domain.follow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.auth.annotation.Auth;
import real.world.domain.follow.service.FollowService;
import real.world.domain.follow.dto.response.ProfileResponse;

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
    public ResponseEntity<ProfileResponse> getProfile(@Auth Long loginId, @PathVariable("username") String username) {
        final ProfileResponse response = followService.getProfile(username, loginId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/profiles/{username}/unfollow")
    public ResponseEntity<ProfileResponse> unfollow(@Auth Long loginId, @PathVariable("username") String username) {
        final ProfileResponse response = followService.unfollow(loginId, username);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}