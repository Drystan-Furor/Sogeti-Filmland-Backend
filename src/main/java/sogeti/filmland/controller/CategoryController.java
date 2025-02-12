package sogeti.filmland.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sogeti.filmland.dto.CategoryDTO;
import sogeti.filmland.dto.SubscriptionDTO;
import sogeti.filmland.model.User;
import sogeti.filmland.repository.UserRepository;
import sogeti.filmland.service.CategoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    public CategoryController(CategoryService categoryService, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCategories(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
        }

        System.out.println("UserDetails is present: " + userDetails.getUsername());

        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("Resolved user: " + user.getEmail());

        List<CategoryDTO> categories = categoryService.getAllCategories();
        List<SubscriptionDTO> subscriptions = categoryService.getUserSubscriptions(user);

        Map<String, Object> response = new HashMap<>();
        response.put("availableCategories", categories);
        response.put("subscribedCategories", subscriptions);

        return ResponseEntity.ok(response);
    }

}
