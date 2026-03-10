package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {
    private final List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        int index = findIndexById(payment.getId());
        if (index != -1) {
            paymentData.remove(index);
            paymentData.add(index, payment);
            return payment;
        }

        paymentData.add(payment);
        return payment;
    }

    public Payment getPayment(String paymentId) {
        for (Payment savedPayment : paymentData) {
            if (savedPayment.getId().equals(paymentId)) {
                return savedPayment;
            }
        }
        return null;
    }

    public List<Payment> getAllPayments() {
        return paymentData;
    }

    private int findIndexById(String paymentId) {
        for (int index = 0; index < paymentData.size(); index++) {
            if (paymentData.get(index).getId().equals(paymentId)) {
                return index;
            }
        }
        return -1;
    }
}