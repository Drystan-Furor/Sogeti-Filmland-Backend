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

    // Controleer of een lid al is geabonneerd op een categorie
    Optional<Subscription> findByMemberAndCategory(Member member, Category category);

    // Zoek een abonnement op basis van het e-mailadres van de hoofdabonnee en de categorie
    Optional<Subscription> findByMember_EmailAndCategory_Name(String email, String categoryName);

    // Zoek alle abonnementen waarbij een bepaalde gebruiker is toegevoegd als gedeelde abonnee
    List<Subscription> findBySharedWithEmail(String sharedWithEmail);
}
