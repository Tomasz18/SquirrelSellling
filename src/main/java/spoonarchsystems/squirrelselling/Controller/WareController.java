package spoonarchsystems.squirrelselling.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WareController {

    @GetMapping("/shoppingCart")
    public String home() {
        return "view/general/shopping_cart";
    }
}
