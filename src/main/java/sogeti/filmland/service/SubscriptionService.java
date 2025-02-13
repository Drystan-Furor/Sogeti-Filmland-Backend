package sogeti.filmland.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sogeti.filmland.model.Category;
import sogeti.filmland.model.Member;
import sogeti.filmland.model.Subscription;
import sogeti.filmland.repository.CategoryRepository;
import sogeti.filmland.repository.MemberRepository;
import sogeti.filmland.repository.SubscriptionRepository;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class SubscriptionService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(MemberRepository memberRepository, CategoryRepository categoryRepository, SubscriptionRepository subscriptionRepository) {
        this.memberRepository = memberRepository;
        this.categoryRepository = categoryRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public boolean subscribe(String email, String categoryName) {
        log.warn("Zoek member met email: {}", email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with email: " + email));

        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with name: " + categoryName));
        log.warn("Gebruiker  gevonden: {}", email);
        log.warn("category   gevonden: {}", categoryName);
        if (isAlreadySubscribed(member, category)) {
            log.warn("already subscribed  : {} {}", member, category);
            return false;
        }


        return subscribeNow(member, category);
    }

    private boolean isAlreadySubscribed(Member member, Category category) {
        return subscriptionRepository.existsByMemberAndCategory(Optional.ofNullable(member), category);
    }

    private boolean subscribeNow(Member member, Category category) {
        Subscription subscription = new Subscription();
        subscription.setMember(member);
        subscription.setCategory(category);
        subscription.setStartDate(LocalDate.now());
        subscription.setRemainingContent(category.getContentLimit());

        subscriptionRepository.save(subscription);
        return true;
    }
}

