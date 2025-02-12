package sogeti.filmland.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionDTO {
    private String name;
    private int remainingContent;
    private double price;
    private String startDate;
}
