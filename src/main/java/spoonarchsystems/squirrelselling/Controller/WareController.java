package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spoonarchsystems.squirrelselling.Model.Service.ShoppingCartService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WareController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        if(shoppingCartService.getShoppingCart().getPositions() == null || shoppingCartService.getShoppingCart().getPositions().isEmpty())
            shoppingCartService.initTestData();
        model.addAttribute("shoppingCart", shoppingCartService.getShoppingCart());
        model.addAttribute("shoppingCartSum", shoppingCartService.getSum());
        return "view/ware/shopping_cart";
    }

    @RequestMapping(value="/shoppingCart", params={"removePos"})
    public String removeShoppingCartPosition(final HttpServletRequest req) {
        final Integer posNumber = Integer.valueOf(req.getParameter("removePos"));
        shoppingCartService.remove(posNumber);
        if(shoppingCartService.getShoppingCart().getPositions().isEmpty())
            return "view/general/home";
        return "redirect:/shoppingCart";
    }

    @RequestMapping(value="/home", params={"clearCart"})
    public String clearShoppingCart() {
        shoppingCartService.clear();
        return "redirect:/";
    }

    @RequestMapping(value="/home", params={"continueShopping"})
    public String continueShopping() {
        return "redirect:/";
    }
}
