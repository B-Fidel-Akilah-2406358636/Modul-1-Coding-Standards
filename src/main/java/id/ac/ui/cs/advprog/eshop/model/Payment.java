package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class Payment {
    public static final String VOUCHER_CODE = "Voucher Code";
    public static final String BANK_TRANSFER = "Bank Transfer";
    public static final String SUCCESS = "SUCCESS";
    public static final String REJECTED = "REJECTED";

    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(Order order, String method, Map<String, String> paymentData) {
        this.id = UUID.randomUUID().toString();
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = determineInitialStatus(method, paymentData);
    }

    public void setStatus(String status) {
        if (SUCCESS.equals(status) || REJECTED.equals(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private String determineInitialStatus(String method, Map<String, String> paymentData) {
        if (VOUCHER_CODE.equals(method)) {
            return isValidVoucherCode(paymentData.get("voucherCode")) ? SUCCESS : REJECTED;
        }

        if (BANK_TRANSFER.equals(method)) {
            return isNotBlank(paymentData.get("bankName")) && isNotBlank(paymentData.get("referenceCode"))
                    ? SUCCESS : REJECTED;
        }

        return REJECTED;
    }

    private boolean isValidVoucherCode(String voucherCode) {
        return voucherCode != null
                && voucherCode.length() == 16
                && voucherCode.startsWith("ESHOP")
                && voucherCode.replaceAll("\\D", "").length() == 8;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isEmpty();
    }
}