package real.world.domain.article.service;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SlugUUIDTranslator implements SlugTranslator {

    public String translate(String title) {
        final String suffix = UUID.randomUUID().toString().substring(0, 6);
        final String prefix = title.toLowerCase().replaceAll("\\s+", "-");
        return prefix + "-" + suffix;
    }

}
