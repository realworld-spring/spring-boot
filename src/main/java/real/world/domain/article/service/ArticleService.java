package real.world.domain.article.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import real.world.domain.article.entity.Tag;
import real.world.domain.article.repository.ArticleRepository;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.dto.response.UploadResponse;
import real.world.domain.article.entity.Article;
import real.world.domain.article.repository.TagRepository;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.UserIdNotExistException;

@Service
public class ArticleService {


    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    private final SlugTranslator slugTranslator;

    public ArticleService(UserRepository userRepository, ArticleRepository articleRepository,
        TagRepository tagRepository, SlugTranslator slugTranslator) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.slugTranslator = slugTranslator;
    }

    @Transactional
    public UploadResponse upload(Long loginId, UploadRequest request) {
        final User user = userRepository.findById(loginId)
            .orElseThrow(UserIdNotExistException::new);
        checkAndCreateTag(request.getTags());
        final Article article = requestToEntity(user, request);
        articleRepository.save(article);
        return UploadResponse.of(article);
    }

    private void checkAndCreateTag(Set<String> tagNames) {
        for(String tagName: tagNames) {
            if(tagRepository.findByName(tagName).isEmpty()) {
                final Tag tag = new Tag(tagName);
                tagRepository.save(tag);
            }
        }
    }

    private Article requestToEntity(User user, UploadRequest request) {
        final List<Tag> tags = tagRepository.findByNameIn(request.getTags());
        return new Article(
            user,
            request.getTitle(),
            slugTranslator.translate(request.getTitle()),
            request.getDescription(),
            request.getBody(),
            tags
        );
    }

}
