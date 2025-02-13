package sogeti.filmland.controller;

import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sogeti.filmland.dto.CategoryDTO;
import sogeti.filmland.dto.SubscriptionDTO;
import sogeti.filmland.model.Member;
import sogeti.filmland.repository.MemberRepository;
import sogeti.filmland.service.CategoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final MemberRepository memberRepository;

    public CategoryController(CategoryService categoryService, MemberRepository memberRepository) {
        this.categoryService = categoryService;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCategories() {

        val memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CategoryDTO> categories = categoryService.getCategories(member);
        List<SubscriptionDTO> subscriptions = categoryService.getUserSubscriptions(member);

        Map<String, Object> response = new HashMap<>();
        response.put("availableCategories", categories);
        response.put("subscribedCategories", subscriptions);

        return ResponseEntity.ok(response);
    }

}
