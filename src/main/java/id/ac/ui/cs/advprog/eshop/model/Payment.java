package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    @Setter
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(Order order, String method, Map<String, String> paymentData) {
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
    }
}