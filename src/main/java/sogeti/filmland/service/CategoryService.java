package sogeti.filmland.service;

import org.springframework.stereotype.Service;
import sogeti.filmland.dto.CategoryDTO;
import sogeti.filmland.dto.SubscriptionDTO;
import sogeti.filmland.model.User;
import sogeti.filmland.repository.CategoryRepository;
import sogeti.filmland.repository.SubscriptionRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final SubscriptionRepository subscriptionRepository;

    public CategoryService(CategoryRepository categoryRepository, SubscriptionRepository subscriptionRepository) {
        this.categoryRepository = categoryRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categories = categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(
                        category.getName(),
                        category.getContentLimit(),
                        category.getPrice()
                ))
                .collect(Collectors.toList());

        System.out.println("Available Categories: " + categories);
        return categories;
    }

    public List<SubscriptionDTO> getUserSubscriptions(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        List<SubscriptionDTO> subscriptions = subscriptionRepository.findByUser(user).stream()
                .map(subscription -> new SubscriptionDTO(
                        subscription.getCategory().getName(),
                        subscription.getRemainingContent(),
                        subscription.getCategory().getPrice(),
                        subscription.getStartDate().format(formatter)
                ))
                .collect(Collectors.toList());
        return subscriptions;
    }
}
