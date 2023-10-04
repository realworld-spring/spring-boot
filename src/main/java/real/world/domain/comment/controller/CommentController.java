package real.world.domain.comment.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.auth.annotation.Auth;
import real.world.domain.comment.dto.request.CommentRequest;
import real.world.domain.comment.dto.response.CommentApiResponse;
import real.world.domain.comment.dto.response.CommentResponse;
import real.world.domain.comment.service.CommentService;

@RestController
public class CommentController {

    final private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{slug}/comments")
    ResponseEntity<CommentApiResponse> uploadComment(@Auth Long loginId, @PathVariable String slug, @RequestBody @Valid
        CommentRequest commentRequest) {
        final CommentResponse response = commentService.upload(loginId, slug, commentRequest);

        return new ResponseEntity<>(new CommentApiResponse(response), HttpStatus.OK);
    }

}