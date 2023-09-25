package real.world.domain.article.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.article.dto.request.ArticleUpdateRequest;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.dto.response.ArticleApiResponse;
import real.world.domain.article.dto.response.ArticleDto;
import real.world.domain.article.service.ArticleQueryService;
import real.world.domain.article.service.ArticleService;
import real.world.domain.auth.annotation.Auth;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    private final ArticleQueryService articleQueryService;

    public ArticleController(ArticleService articleService,
        ArticleQueryService articleQueryService) {
        this.articleService = articleService;
        this.articleQueryService = articleQueryService;
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleApiResponse> uploadArticle(@Auth Long loginId,
        @RequestBody @Valid UploadRequest request) {
        final Long articleId = articleService.upload(loginId, request);
        final ArticleDto response = articleQueryService.getArticle(loginId, articleId);
        return new ResponseEntity<>(new ArticleApiResponse(response), HttpStatus.CREATED);
    }

    @GetMapping("/articles/{slug}")
    public ResponseEntity<ArticleApiResponse> getArticle(@Auth Long loginId,
        @PathVariable("slug") String slug) {
        final ArticleDto response = articleQueryService.getArticle(loginId, slug);
        return ResponseEntity.ok(new ArticleApiResponse(response));
    }

    @PutMapping("/articles/{slug}")
    public ResponseEntity<ArticleApiResponse> updateArticle(@Auth Long loginId,
        @PathVariable("slug") String slug, @RequestBody @Valid ArticleUpdateRequest request) {
        final Long articleId = articleService.update(loginId, slug, request);
        final ArticleDto response = articleQueryService.getArticle(loginId, articleId);
        return ResponseEntity.ok(new ArticleApiResponse(response));
    }

    @DeleteMapping("/articles/{slug}")
    public ResponseEntity<Void> deleteArticle(@Auth Long loginId,
        @PathVariable("slug") String slug) {
        articleService.delete(loginId, slug);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

