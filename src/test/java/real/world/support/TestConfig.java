package real.world.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import real.world.domain.article.service.SlugTranslator;

@TestConfiguration
@Primary
public class TestConfig {

    @Bean
    public SlugTranslator slugTranslator() {
        return new StubSlugTranslator();
    }

}
