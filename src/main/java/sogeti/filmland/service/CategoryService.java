package sogeti.filmland.service;

import org.springframework.stereotype.Service;
import sogeti.filmland.dto.CategoryDTO;
import sogeti.filmland.dto.SubscriptionDTO;
import sogeti.filmland.model.Member;
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

    public List<CategoryDTO> getCategories(Member member) {
        return categoryRepository.findByNotMember(member).stream()
                .map(category -> new CategoryDTO(
                        category.getName(),
                        category.getContentLimit(),
                        category.getPrice()
                ))
                .collect(Collectors.toList());
    }


    public List<SubscriptionDTO> getUserSubscriptions(Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return subscriptionRepository.findByMember(member).stream()
                .map(subscription -> new SubscriptionDTO(
                        subscription.getCategory().getName(),
                        subscription.getRemainingContent(),
                        subscription.getCategory().getPrice(),
                        subscription.getStartDate().format(formatter)
                ))
                .collect(Collectors.toList());
    }
}
