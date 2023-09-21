package real.world.domain.article.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.dto.response.ArticleResponse;
import real.world.domain.article.service.ArticleQueryService;
import real.world.domain.article.service.ArticleService;
import real.world.domain.auth.annotation.Auth;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    private final ArticleQueryService articleQueryService;

    public ArticleController(ArticleService articleService, ArticleQueryService articleQueryService) {
        this.articleService = articleService;
        this.articleQueryService = articleQueryService;
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> uploadArticle(@Auth Long loginId,
        @RequestBody @Valid UploadRequest request) {
        final Long articleId = articleService.upload(loginId, request);
        final ArticleResponse response = articleQueryService.getArticle(loginId, articleId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}

