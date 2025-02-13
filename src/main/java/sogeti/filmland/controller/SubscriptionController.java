package sogeti.filmland.controller;

import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sogeti.filmland.model.ResponseMessage;
import sogeti.filmland.model.SubscriptionRequest;
import sogeti.filmland.model.SubscriptionShareRequest;
import sogeti.filmland.model.Member;
import sogeti.filmland.repository.MemberRepository;
import sogeti.filmland.service.SubscriptionService;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final MemberRepository memberRepository;

    public SubscriptionController(SubscriptionService subscriptionService, MemberRepository memberRepository) {
        this.subscriptionService = subscriptionService;
        this.memberRepository = memberRepository;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> subscribeToCategory(@RequestBody SubscriptionRequest request) {
        val memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isSubscribed = subscriptionService.subscribe(member.getEmail(), request.getAvailableCategory());

        if (isSubscribed) {
            return ResponseEntity.ok(new ResponseMessage("Abonnement succesvol aangemaakt."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("U bent al geabonneerd op deze categorie."));
        }
    }

    @PostMapping("/share")
    public ResponseEntity<ResponseMessage> shareSubscription(@RequestBody SubscriptionShareRequest request) {
        val memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // Haal de eigenaar op uit de database
        Member owner = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new RuntimeException("Eigenaar niet gevonden."));

        // Haal de klant op uit de database
        Member customer = memberRepository.findByEmail(request.getCustomer())
                .orElseThrow(() -> new RuntimeException("Klant niet gevonden."));

        boolean isShared = subscriptionService.shareSubscription(owner.getEmail(), customer.getEmail(), request.getSubscribedCategory());

        if (isShared) {
            return ResponseEntity.ok(new ResponseMessage("Abonnement succesvol gedeeld met " + request.getCustomer() + "."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Het delen van het abonnement is mislukt. Mogelijk bestaat de gebruiker niet of is deze al geabonneerd."));
        }
    }
}
