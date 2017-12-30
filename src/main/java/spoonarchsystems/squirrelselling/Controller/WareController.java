package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCartPosition;
import spoonarchsystems.squirrelselling.Model.Entity.Ware;
import spoonarchsystems.squirrelselling.Model.Service.ShoppingCartService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WareController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        shoppingCartService.initTestData();
        model.addAttribute("shoppingCartPositions", shoppingCartService.getShoppingCart().getPositions());
        model.addAttribute("shoppingCartSum", shoppingCartService.getSum());
        return "view/general/shopping_cart";
    }
}
