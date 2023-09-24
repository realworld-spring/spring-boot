package real.world.support;

import real.world.domain.article.service.SlugTranslator;

public class StubSlugTranslator implements SlugTranslator {

    @Override
    public String translate(String title) {
        return title.toLowerCase().replaceAll("\\s+", "-");
    }

}
