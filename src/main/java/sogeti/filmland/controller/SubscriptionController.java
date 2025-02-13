package sogeti.filmland.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<?> subscribeToCategory(@RequestBody SubscriptionRequest request) {

        boolean isSubscribed = subscriptionService.subscribe(request.getEmail(), request.getAvailableCategory());

        if (isSubscribed) {
            return ResponseEntity.ok(new ResponseMessage("Abonnement succesvol aangemaakt."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("U bent al geabonneerd op deze categorie."));
        }
    }
}

