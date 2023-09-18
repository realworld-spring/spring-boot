package real.world.domain.article.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.repository.Repository;
import real.world.domain.article.entity.Tag;

public interface TagRepository extends Repository<Tag, Long> {

    Tag save(Tag tag);

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    List<Tag> findByNameIn(Set<String> names);

}
