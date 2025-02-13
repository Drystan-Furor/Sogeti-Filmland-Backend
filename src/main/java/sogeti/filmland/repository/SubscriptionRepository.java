package sogeti.filmland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sogeti.filmland.model.Category;
import sogeti.filmland.model.Member;
import sogeti.filmland.model.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByMember(Member member);

    boolean existsByMemberAndCategory(Optional<Member> member, Category category);
}
