package sogeti.filmland.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sogeti.filmland.model.ResponseMessage;
import sogeti.filmland.model.SubscriptionRequest;
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
}
