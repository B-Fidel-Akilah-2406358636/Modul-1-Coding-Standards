package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(1);
        products.add(product);

        Order order = new Order("order-1", products, 1708560000L, "Safira Sudrajat");
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment(order, Payment.VOUCHER_CODE, paymentData);
    }

    @Test
    void testSaveCreate() {
        Payment result = paymentRepository.save(payment);

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), paymentRepository.getPayment(payment.getId()).getId());
    }

    @Test
    void testSaveUpdate() {
        paymentRepository.save(payment);
        payment.setStatus(Payment.REJECTED);

        Payment result = paymentRepository.save(payment);

        assertEquals(Payment.REJECTED, result.getStatus());
        assertEquals(Payment.REJECTED, paymentRepository.getPayment(payment.getId()).getStatus());
    }

    @Test
    void testGetPaymentIfNotFound() {
        assertNull(paymentRepository.getPayment("missing-id"));
    }

    @Test
    void testGetAllPayments() {
        paymentRepository.save(payment);

        List<Payment> payments = paymentRepository.getAllPayments();

        assertEquals(1, payments.size());
        assertEquals(payment.getId(), payments.get(0).getId());
    }
}