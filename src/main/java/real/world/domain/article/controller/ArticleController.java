package real.world.domain.article.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.dto.response.UploadResponse;
import real.world.domain.article.service.ArticleService;
import real.world.domain.auth.annotation.Auth;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/articles")
    public ResponseEntity<UploadResponse> uploadArticle(@Auth Long loginId,
        @RequestBody @Valid UploadRequest request) {
        final UploadResponse response = articleService.upload(loginId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}

