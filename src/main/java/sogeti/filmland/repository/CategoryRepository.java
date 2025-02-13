package sogeti.filmland.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sogeti.filmland.model.Category;
import sogeti.filmland.model.Member;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c NOT IN (SELECT s.category FROM Subscription s WHERE s.member = :member)")
    List<Category> findByNotMember(@Param("member") Member member);
}
