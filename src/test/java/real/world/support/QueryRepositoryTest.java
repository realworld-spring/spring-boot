package real.world.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import real.world.config.JpaConfig;
import real.world.e2e.util.DBInitializer;
import real.world.e2e.util.DatabaseCleaner;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, DBInitializer.class, DatabaseCleaner.class})
public abstract class QueryRepositoryTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void clean() {
        databaseCleaner.clean();
    }

}