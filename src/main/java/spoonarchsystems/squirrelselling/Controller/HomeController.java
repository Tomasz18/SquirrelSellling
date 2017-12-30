package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spoonarchsystems.squirrelselling.Model.Service.ShoppingCartService;

@Controller
public class HomeController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/")
    public String home(Model model) {
        if(shoppingCartService.getShoppingCart().getPositions() == null)
            shoppingCartService.initTestData();
        model.addAttribute("shoppingCartSum", shoppingCartService.getSum());
        model.addAttribute("shoppingCartSize", shoppingCartService.getSize());
        return "view/general/home";
    }
}
