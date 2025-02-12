package sogeti.filmland.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sogeti.filmland.dto.CategoryDTO;
import sogeti.filmland.dto.SubscriptionDTO;
import sogeti.filmland.model.User;
import sogeti.filmland.service.CategoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCategories(@AuthenticationPrincipal User user) {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        List<SubscriptionDTO> subscriptions = categoryService.getUserSubscriptions(user);

        Map<String, Object> response = new HashMap<>();
        response.put("availableCategories", categories);
        response.put("subscribedCategories", subscriptions);

        return ResponseEntity.ok(response);
    }
}
