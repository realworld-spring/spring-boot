package real.world.domain.article.service;

public class StubSlugTranslator implements SlugTranslator {

    @Override
    public String translate(String title) {
        return title;
    }

}
