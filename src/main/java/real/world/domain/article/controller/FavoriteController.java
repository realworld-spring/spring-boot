package real.world.domain.article.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import real.world.domain.article.dto.response.ArticleApiResponse;
import real.world.domain.article.dto.response.ArticleResponse;
import real.world.domain.article.service.ArticleQueryService;
import real.world.domain.article.service.FavoriteService;
import real.world.domain.auth.annotation.Auth;

@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    private final ArticleQueryService articleQueryService;

    public FavoriteController(FavoriteService favoriteService, ArticleQueryService articleQueryService) {
        this.favoriteService = favoriteService;
        this.articleQueryService = articleQueryService;
    }

    @PostMapping("/articles/{slug}/favorite")
    public ResponseEntity<ArticleApiResponse> favorite(@Auth Long loginId, @PathVariable("slug") String slug) {
        final Long articleId = favoriteService.favorite(loginId, slug);
        final ArticleResponse response = articleQueryService.getArticle(loginId, articleId);
        return ResponseEntity.ok(new ArticleApiResponse(response));
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public ResponseEntity<ArticleApiResponse> unfavorite(@Auth Long loginId, @PathVariable("slug") String slug) {
        final Long articleId = favoriteService.unfavorite(loginId, slug);
        final ArticleResponse response = articleQueryService.getArticle(loginId, articleId);
        return ResponseEntity.ok(new ArticleApiResponse(response));
    }

}
