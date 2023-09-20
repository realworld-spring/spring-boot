package real.world.e2e.util;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;
    private List<String> tableNames;

    @PostConstruct
    public void setUp() {
        this.tableNames = entityManager.getMetamodel().getEntities().stream()
            .filter(it ->
                it.getJavaType().getDeclaredAnnotation(Entity.class) != null)
            .map(it -> it.getJavaType().getDeclaredAnnotation(Entity.class).name())
            .collect(Collectors.toList());
    }

    @Transactional
    public void clean() {
        entityManager.flush();
        entityManager.createNativeQuery("SET foreign_key_checks = 0").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }
        entityManager.createNativeQuery("SET foreign_key_checks = 1").executeUpdate();
    }

}
