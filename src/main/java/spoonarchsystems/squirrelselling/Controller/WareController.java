package spoonarchsystems.squirrelselling.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import spoonarchsystems.squirrelselling.Model.Entity.ShoppingCart;
import spoonarchsystems.squirrelselling.Model.Service.ShoppingCartService;

import javax.servlet.http.HttpServletRequest;

/**
 * A controller handles shopping cart modifications and wares manipulations
 */
@Controller
public class WareController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Prepares shopping cart content to show and returns shopping cart view name
     * @param model container for shopping cart data
     * @return shopping cart view name
     */
    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        if(shoppingCartService.getShoppingCart().getPositions() == null || shoppingCartService.getShoppingCart().getPositions().isEmpty())
            shoppingCartService.initTestData();
        model.addAttribute("shoppingCart", shoppingCartService.getShoppingCart());
        return "view/ware/shopping_cart";
    }

    /**
     * Removes position with requested number from shopping cart and if cart is not
     * empty redirects to shopping cart view. If it's empty shows home page. If necessary
     * updates wares quantity.
     * @param req request contains shopping cart position number to remove
     * @param shoppingCart shopping cart with updated wares quantity retrieved from form
     * @return redirect to shopping cart view or home view name
     */
    @RequestMapping(value="/shoppingCart", params={"removePos"})
    public String removeShoppingCartPosition(final HttpServletRequest req, @ModelAttribute ShoppingCart shoppingCart) {
        final Integer posNumber = Integer.valueOf(req.getParameter("removePos"));
        shoppingCartService.updateQuantity(shoppingCart);
        shoppingCartService.remove(posNumber);
        if(shoppingCartService.getShoppingCart().getPositions().isEmpty())
            return "view/general/home";
        return "redirect:/shoppingCart";
    }

    /**
     * Removes all positions from shopping cart
     * @return redirect to home page
     */
    @RequestMapping(value="/home", params={"clearCart"})
    public String clearShoppingCart() {
        shoppingCartService.clear();
        return "redirect:/";
    }

    /**
     * Redirects to last visited category
     * @return last visited category view name
     */
    @RequestMapping(value="/home", params={"continueShopping"})
    public String continueShopping() {
        return "redirect:/";
    }
}
