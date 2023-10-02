package real.world.e2e.util;

import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.BOB;
import static real.world.fixture.UserFixtures.JOHN;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import real.world.fixture.ArticleFixtures;
import real.world.fixture.FollowFixtures;

@Component
public class DBInitializer {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void 유저들이_회원가입_돼있다() {
        entityManager.persist(JOHN.지정된_ID로_생성(null));
        entityManager.persist(ALICE.지정된_ID로_생성(null));
        entityManager.persist(BOB.지정된_ID로_생성(null));
    }

    @Transactional
    public void 게시물이_하나_업로드_돼있다() {
        유저들이_회원가입_돼있다();
        entityManager.persist(ArticleFixtures.게시물.생성(JOHN.getId()));
    }
    
    @Transactional
    public void 게시물이_업로드_돼있다() {
        유저들이_회원가입_돼있다();
        entityManager.persist(ArticleFixtures.게시물.생성(JOHN.getId()));
        entityManager.persist(ArticleFixtures.게시물_2.생성(JOHN.getId()));
        entityManager.persist(ArticleFixtures.게시물_3.생성(JOHN.getId()));
    }

    @Transactional
    public void JOHN이_ALICE를_팔로우한다() {
        유저들이_회원가입_돼있다();
        entityManager.persist(FollowFixtures.JOHN이_ALICE를_팔로우.생성());
    }

    @Transactional
    public void JOHN이_ALICE와_BOB을_팔로우한다() {
        유저들이_회원가입_돼있다();
        entityManager.persist(FollowFixtures.JOHN이_ALICE를_팔로우.생성());
        entityManager.persist(FollowFixtures.JOHN이_BOB를_팔로우.생성());
    }

}
