package sogeti.filmland.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class FrontEndController {

    public String showHomePage() {
        return "index";
    }
}
