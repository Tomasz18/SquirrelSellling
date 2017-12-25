package spoonarchsystems.squirrelselling.Controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import spoonarchsystems.squirrelselling.Model.Entity.Order;

import java.util.Date;

@Controller
public class HomeController {

    //USUNAC
    @Autowired
    SessionFactory sessionFactory;

    @GetMapping("/")
    @Transactional
    public String home() {
        Session session = sessionFactory.getCurrentSession();
        Order order = new Order();
        order.setNumber("123456789123456");
        order.setStatus("zlozone");
        order.setSubmissionDate(new Date());
        order.setPostponementTime(0);
        order.setComplaining(false);
        order.setPersonalCollection(false);
        order.setDeliveryCost(123.32);
        session.persist(order);
        return "view/general/home";
    }
}
