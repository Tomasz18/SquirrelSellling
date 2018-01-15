package spoonarchsystems.squirrelselling.Controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spoonarchsystems.squirrelselling.Model.Entity.Customer;
import spoonarchsystems.squirrelselling.Model.Entity.CustomerAccount;
import spoonarchsystems.squirrelselling.Model.Entity.Order;
import spoonarchsystems.squirrelselling.Model.Service.AccountService;
import spoonarchsystems.squirrelselling.Model.Service.OrderService;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    private MockMvc mvc;

    @Mock
    private OrderService orderService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private OrderController orderController;

    private void setup() {
        mvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void showOrders() throws Exception {
        setup();
        CustomerAccount customerAccount = new CustomerAccount();
        Customer customer = new Customer();
        ArrayList<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        Order order2 = new Order();
        orders.add(order1);
        orders.add(order2);
        customer.setOrders(orders);
        customerAccount.setCustomer(customer);

        given(accountService.getCurrentCustomer()).willReturn(customerAccount);
        given(orderService.getOrderValue(order1)).willReturn(250.0);
        given(orderService.getOrderValue(order2)).willReturn(300.0);

        mvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/order/order_list"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("ordersValues"));
    }

    @Test
    public void showOrderDetails() throws Exception {
        setup();
        Order order = new Order();
        String orderNumber = "0001/2018-01-15";
        order.setNumber(orderNumber);
        given(orderService.getOrder(1)).willReturn(order);
        given(orderService.getOrderValue(order)).willReturn(15.0);

        mvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/order/order_details"))
                .andExpect(model().attributeExists("orderValue"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("orderValue", 15.0))
        .       andExpect(model().attribute("order", allOf(hasProperty("number", is(orderNumber)))));
    }

}