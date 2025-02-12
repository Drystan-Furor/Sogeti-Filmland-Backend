package sogeti.filmland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sogeti.filmland.model.Subscription;
import sogeti.filmland.model.User;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.user.email = :email")
    List<Subscription> findByUser(User user);
}
