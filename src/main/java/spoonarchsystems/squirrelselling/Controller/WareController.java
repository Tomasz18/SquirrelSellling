package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCartPosition;
import spoonarchsystems.squirrelselling.Model.Service.ShoppingCartService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Controller
public class WareController {
    private static final String SHOPPING_CART = "view/general/shopping_cart";
    private static final String HOME = "view/general/home";

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        if(shoppingCartService.getShoppingCart().getPositions() == null || shoppingCartService.getShoppingCart().getPositions().isEmpty())
            shoppingCartService.initTestData();
        model.addAttribute("shoppingCartPositions", shoppingCartService.getShoppingCart().getPositions());
        model.addAttribute("shoppingCartSum", shoppingCartService.getSum());
        return SHOPPING_CART;
    }

    @RequestMapping(value="/shoppingCart", params={"removePos"})
    public String removeShoppingCartPosition(final HttpServletRequest req) {
        final Integer posNumber = Integer.valueOf(req.getParameter("removePos"));
        shoppingCartService.remove(posNumber);
        if(shoppingCartService.getShoppingCart().getPositions().isEmpty())
            return HOME;
        return "redirect:/shoppingCart";
    }
}
