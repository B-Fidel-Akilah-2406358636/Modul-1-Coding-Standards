package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "orderCreate";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam("author") String author) {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName("Default Product");
        product.setProductQuantity(1);
        products.add(product);

        Order order = new Order(UUID.randomUUID().toString(), products, System.currentTimeMillis(), author);
        orderService.createOrder(order);
        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "orderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam("author") String author, Model model) {
        model.addAttribute("orders", orderService.findAllByAuthor(author));
        return "orderHistory";
    }

    @GetMapping("/pay/{orderId}")
    public String orderPayPage(@PathVariable("orderId") String orderId, Model model) {
        model.addAttribute("order", orderService.findById(orderId));
        return "orderPay";
    }

    @PostMapping("/pay/{orderId}")
    public String orderPayPost(@PathVariable("orderId") String orderId,
                               @RequestParam("method") String method,
                               @RequestParam Map<String, String> requestParams,
                               Model model) {
        Map<String, String> paymentData = new HashMap<>();
        if (Payment.VOUCHER_CODE.equals(method)) {
            paymentData.put("voucherCode", requestParams.get("voucherCode"));
        } else if (Payment.BANK_TRANSFER.equals(method)) {
            paymentData.put("bankName", requestParams.get("bankName"));
            paymentData.put("referenceCode", requestParams.get("referenceCode"));
        }

        Payment payment = paymentService.addPayment(orderService.findById(orderId), method, paymentData);
        model.addAttribute("payment", payment);
        return "orderPayResult";
    }
}