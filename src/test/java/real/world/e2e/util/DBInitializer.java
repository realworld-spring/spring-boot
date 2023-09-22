package real.world.e2e.util;

import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.BOB;
import static real.world.fixture.UserFixtures.JOHN;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void 유저들이_회원가입_돼있다() {
        entityManager.persist(JOHN.지정된_ID로_생성(null));
        entityManager.persist(BOB.지정된_ID로_생성(null));
        entityManager.persist(ALICE.지정된_ID로_생성(null));
    }

}
