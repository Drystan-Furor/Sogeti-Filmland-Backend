package sogeti.filmland.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sogeti.filmland.model.ResponseMessage;
import sogeti.filmland.model.SubscriptionRequest;
import sogeti.filmland.model.SubscriptionShareRequest;
import sogeti.filmland.service.SubscriptionService;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> subscribeToCategory(
            @RequestBody SubscriptionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseMessage("Authenticatie vereist. Log in en probeer opnieuw."));
        }

        String email = userDetails.getUsername();

        boolean isSubscribed = subscriptionService.subscribe(email, request.getAvailableCategory());

        if (isSubscribed) {
            return ResponseEntity.ok(new ResponseMessage("Abonnement succesvol aangemaakt."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("U bent al geabonneerd op deze categorie."));
        }
    }

    @PostMapping("/share")
    public ResponseEntity<ResponseMessage> shareSubscription(
            @RequestBody SubscriptionShareRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseMessage("Authenticatie vereist. Log in en probeer opnieuw."));
        }

        String ownerEmail = userDetails.getUsername();
        boolean isShared = subscriptionService.shareSubscription(ownerEmail, request.getCustomer(), request.getSubscribedCategory());

        if (isShared) {
            return ResponseEntity.ok(new ResponseMessage("Abonnement succesvol gedeeld met " + request.getCustomer() + "."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("Het delen van het abonnement is mislukt. Mogelijk bestaat de gebruiker niet of is deze al geabonneerd."));
        }
    }
}
