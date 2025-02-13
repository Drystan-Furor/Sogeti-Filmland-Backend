package sogeti.filmland.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionShareRequest {
    private String email;             // Eigenaar van het abonnement
    private String customer;          // Klant met wie het gedeeld wordt
    private String subscribedCategory; // De naam van de categorie die gedeeld wordt
}
