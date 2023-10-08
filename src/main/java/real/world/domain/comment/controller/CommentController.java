package real.world.domain.comment.controller;


import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.auth.annotation.Auth;
import real.world.domain.comment.dto.request.CommentRequest;
import real.world.domain.comment.dto.response.CommentApiResponse;
import real.world.domain.comment.dto.response.MultipleCommentApiResponse;
import real.world.domain.comment.dto.response.CommentResponse;
import real.world.domain.comment.service.CommentQueryService;
import real.world.domain.comment.service.CommentService;

@RestController
public class CommentController {

    final private CommentService commentService;

    final private CommentQueryService commentQueryService;

    public CommentController(CommentService commentService, CommentQueryService commentQueryService) {
        this.commentService = commentService;
        this.commentQueryService = commentQueryService;
    }

    @PostMapping("/articles/{slug}/comments")
    ResponseEntity<CommentApiResponse> uploadComment(@Auth Long loginId, @PathVariable String slug, @RequestBody @Valid
        CommentRequest commentRequest) {
        final CommentResponse response = commentService.upload(loginId, slug, commentRequest);

        return new ResponseEntity<>(new CommentApiResponse(response), HttpStatus.OK);
    }

    @GetMapping("/articles/{slug}/comments")
    ResponseEntity<MultipleCommentApiResponse> getComment(@Auth Long loginId, @PathVariable String slug) {
        final List<CommentResponse> comments = commentQueryService.getComments(loginId, slug);

        return new ResponseEntity<>(new MultipleCommentApiResponse(comments), HttpStatus.OK);
    }

    @DeleteMapping("/articles/{slug}/comments/{id}")
    ResponseEntity<MultipleCommentApiResponse> deleteComment(@Auth Long loginId, @PathVariable Long id) {
        commentService.delete(loginId, id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}