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
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        if(shoppingCartService.getShoppingCart().getPositions() == null || shoppingCartService.getShoppingCart().getPositions().isEmpty())
            shoppingCartService.initTestData();
        model.addAttribute("shoppingCartPositions", shoppingCartService.getShoppingCart().getPositions());
        model.addAttribute("shoppingCartSum", shoppingCartService.getSum());
        model.addAttribute("shoppingCartSize", shoppingCartService.getSize());
        return "view/general/shopping_cart";
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

    @RequestMapping(value="/orderForm", params={"placeOrder"})
    public String placeOrder() {
        return "view/general/order_form";
    }
}
