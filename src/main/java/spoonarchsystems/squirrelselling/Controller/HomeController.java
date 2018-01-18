package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spoonarchsystems.squirrelselling.Model.Service.ShoppingCartService;

/**
 * A controller handles general application sites
 */
@Controller
public class HomeController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Prepares home page view
     * @return home page view name
     */
    @GetMapping("/")
    public String home() {
        if(shoppingCartService.getShoppingCart().getPositions() == null)
            shoppingCartService.initTestData();
        return "view/general/home";
    }
}
