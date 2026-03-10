package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;

    @Mock
    PaymentService paymentService;

    @Mock
    Model model;

    private Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("order-1", products, 1708560000L, "Safira Sudrajat");
    }

    @Test
    void testCreateOrderPage() {
        assertEquals("orderCreate", orderController.createOrderPage());
    }

    @Test
    void testCreateOrderPost() {
        String viewName = orderController.createOrderPost("Safira Sudrajat");

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderService, times(1)).createOrder(orderCaptor.capture());
        assertEquals("Safira Sudrajat", orderCaptor.getValue().getAuthor());
        assertNotNull(orderCaptor.getValue().getId());
        assertEquals("redirect:/order/history", viewName);
    }

    @Test
    void testOrderHistoryPage() {
        assertEquals("orderHistory", orderController.orderHistoryPage());
    }

    @Test
    void testOrderHistoryPost() {
        List<Order> orders = List.of(order);
        when(orderService.findAllByAuthor("Safira Sudrajat")).thenReturn(orders);

        String viewName = orderController.orderHistoryPost("Safira Sudrajat", model);

        assertEquals("orderHistory", viewName);
        verify(model, times(1)).addAttribute("orders", orders);
    }

    @Test
    void testOrderPayPage() {
        when(orderService.findById("order-1")).thenReturn(order);

        String viewName = orderController.orderPayPage("order-1", model);

        assertEquals("orderPay", viewName);
        verify(model, times(1)).addAttribute("order", order);
    }

    @Test
    void testOrderPayPostVoucher() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("method", Payment.VOUCHER_CODE);
        requestParams.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, Payment.VOUCHER_CODE, Map.of("voucherCode", "ESHOP1234ABC5678"));
        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq(Payment.VOUCHER_CODE), anyMap())).thenReturn(payment);

        String viewName = orderController.orderPayPost("order-1", Payment.VOUCHER_CODE, requestParams, model);

        assertEquals("orderPayResult", viewName);
        verify(model, times(1)).addAttribute("payment", payment);
    }

    @Test
    void testOrderPayPostBankTransfer() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("method", Payment.BANK_TRANSFER);
        requestParams.put("bankName", "BCA");
        requestParams.put("referenceCode", "REF-1");
        Payment payment = new Payment(order, Payment.BANK_TRANSFER, Map.of("bankName", "BCA", "referenceCode", "REF-1"));
        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq(Payment.BANK_TRANSFER), anyMap())).thenReturn(payment);

        String viewName = orderController.orderPayPost("order-1", Payment.BANK_TRANSFER, requestParams, model);

        assertEquals("orderPayResult", viewName);
        verify(model, times(1)).addAttribute("payment", payment);
    }

    @Test
    void testOrderPayPostUnknownMethod() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("method", "Unknown");
        Payment payment = new Payment(order, Payment.VOUCHER_CODE, Map.of("voucherCode", "ESHOP1234ABC5678"));
        when(orderService.findById("order-1")).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq("Unknown"), anyMap())).thenReturn(payment);

        String viewName = orderController.orderPayPost("order-1", "Unknown", requestParams, model);

        assertEquals("orderPayResult", viewName);
        verify(model, times(1)).addAttribute("payment", payment);
    }
}