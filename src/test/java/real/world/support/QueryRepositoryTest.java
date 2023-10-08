package real.world.support;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import real.world.config.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
public abstract class QueryRepositoryTest {

}