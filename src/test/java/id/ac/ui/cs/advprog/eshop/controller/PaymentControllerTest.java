package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {
    @InjectMocks
    PaymentController paymentController;

    @Mock
    PaymentService paymentService;

    @Mock
    Model model;

    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(1);
        products.add(product);

        Order order = new Order("order-1", products, 1708560000L, "Safira Sudrajat");
        payment = new Payment(order, Payment.VOUCHER_CODE, Map.of("voucherCode", "ESHOP1234ABC5678"));
    }

    @Test
    void testPaymentDetailForm() {
        assertEquals("paymentDetailForm", paymentController.paymentDetailForm());
    }

    @Test
    void testPaymentDetailPage() {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);

        String viewName = paymentController.paymentDetailPage("payment-1", model);

        assertEquals("paymentDetail", viewName);
        verify(model, times(1)).addAttribute("payment", payment);
    }

    @Test
    void testPaymentAdminListPage() {
        List<Payment> payments = List.of(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        String viewName = paymentController.paymentAdminListPage(model);

        assertEquals("paymentAdminList", viewName);
        verify(model, times(1)).addAttribute("payments", payments);
    }

    @Test
    void testPaymentAdminDetailPage() {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);

        String viewName = paymentController.paymentAdminDetailPage("payment-1", model);

        assertEquals("paymentAdminDetail", viewName);
        verify(model, times(1)).addAttribute("payment", payment);
    }

    @Test
    void testSetPaymentStatus() {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);
        when(paymentService.setStatus(payment, Payment.SUCCESS)).thenReturn(payment);

        String viewName = paymentController.setPaymentStatus("payment-1", Payment.SUCCESS);

        assertEquals("redirect:/payment/admin/detail/payment-1", viewName);
        verify(paymentService, times(1)).setStatus(payment, Payment.SUCCESS);
    }
}