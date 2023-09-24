package real.world.domain.profile.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.auth.annotation.Auth;
import real.world.domain.follow.service.FollowService;
import real.world.domain.profile.dto.response.ProfileResponse;
import real.world.domain.profile.service.ProfileQueryService;

@RestController
public class ProfileController {

    private final FollowService followService;

    private final ProfileQueryService profileQueryService;

    public ProfileController(FollowService followService, ProfileQueryService profileQueryService) {
        this.followService = followService;
        this.profileQueryService = profileQueryService;
    }

    @GetMapping("/profiles/{username}")
    public ResponseEntity<ProfileResponse> getProfile(@Auth Long loginId, @PathVariable("username") String username) {
        final ProfileResponse response = profileQueryService.getProfile(username, loginId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/profiles/{username}/follow")
    public ResponseEntity<ProfileResponse> follow(@Auth Long loginId, @PathVariable("username") String username) {
        final ProfileResponse response = followService.follow(loginId, username);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/profiles/{username}/unfollow")
    public ResponseEntity<ProfileResponse> unfollow(@Auth Long loginId, @PathVariable("username") String username) {
        final ProfileResponse response = followService.unfollow(loginId, username);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}