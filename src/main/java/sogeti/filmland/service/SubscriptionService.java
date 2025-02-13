package sogeti.filmland.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public boolean shareSubscription(String ownerEmail, String sharedEmail, String categoryName) {
        log.warn("Delen van abonnement: {} -> {}", ownerEmail, sharedEmail);

        Member owner = memberRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Eigenaar niet gevonden met email: " + ownerEmail));

        Member sharedMember = memberRepository.findByEmail(sharedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Ontvanger niet gevonden met email: " + sharedEmail));

        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Categorie niet gevonden: " + categoryName));

        Subscription ownerSubscription = subscriptionRepository.findByMemberAndCategory(owner, category)
                .orElseThrow(() -> new IllegalArgumentException("Abonnement niet gevonden voor de eigenaar"));

        if (subscriptionRepository.findByMemberAndCategory(sharedMember, category).isPresent()) {
            log.warn("Gebruiker {} is al geabonneerd op {}", sharedEmail, categoryName);
            return false;
        }

        // Verdeel de resterende content
        int newRemainingContent = ownerSubscription.getRemainingContent() / 2;
        ownerSubscription.setRemainingContent(newRemainingContent);

        Subscription sharedSubscription = new Subscription();
        sharedSubscription.setMember(sharedMember);
        sharedSubscription.setCategory(category);
        sharedSubscription.setStartDate(LocalDate.now());
        sharedSubscription.setRemainingContent(newRemainingContent);

        subscriptionRepository.save(ownerSubscription);
        subscriptionRepository.save(sharedSubscription);

        log.info("Abonnement succesvol gedeeld tussen {} en {}", ownerEmail, sharedEmail);
        return true;
    }
}

