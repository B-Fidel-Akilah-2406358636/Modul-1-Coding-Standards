package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    private Order order;
    private Map<String, String> voucherPaymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("order-1", products, 1708560000L, "Safira Sudrajat");

        voucherPaymentData = new HashMap<>();
        voucherPaymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testAddPayment() {
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment payment = paymentService.addPayment(order, Payment.VOUCHER_CODE, voucherPaymentData);

        assertEquals(Payment.VOUCHER_CODE, payment.getMethod());
        assertEquals(Payment.SUCCESS, payment.getStatus());
        assertEquals(order.getId(), payment.getOrder().getId());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = new Payment(order, Payment.VOUCHER_CODE, voucherPaymentData);
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, Payment.SUCCESS);

        assertEquals(Payment.SUCCESS, result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment(order, Payment.VOUCHER_CODE, voucherPaymentData);
        doAnswer(invocation -> invocation.getArgument(0)).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, Payment.REJECTED);

        assertEquals(Payment.REJECTED, result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment(order, Payment.VOUCHER_CODE, voucherPaymentData);

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.setStatus(payment, "WAITING_PAYMENT"));
    }

    @Test
    void testGetPayment() {
        Payment payment = new Payment(order, Payment.VOUCHER_CODE, voucherPaymentData);
        doReturn(payment).when(paymentRepository).getPayment(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());

        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(order, Payment.VOUCHER_CODE, voucherPaymentData));
        doReturn(payments).when(paymentRepository).getAllPayments();

        List<Payment> results = paymentService.getAllPayments();

        assertEquals(1, results.size());
    }
}