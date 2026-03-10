package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    @ResponseBody
    public String paymentDetailForm() {
        return "<html><body><h2>Payment Detail</h2></body></html>";
    }

    @GetMapping("/detail/{paymentId}")
    @ResponseBody
    public String paymentDetailPage(@PathVariable("paymentId") String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        return buildDetailHtml(payment, false);
    }

    @GetMapping("/admin/list")
    @ResponseBody
    public String paymentAdminListPage() {
        List<Payment> payments = paymentService.getAllPayments();
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body><h2>Payment Admin List</h2>");
        for (Payment payment : payments) {
            builder.append("<div>")
                    .append(payment.getId())
                    .append(" <a id=\"detailLink\" href=\"/payment/admin/detail/")
                    .append(payment.getId())
                    .append("\">Detail</a></div>");
        }
        builder.append("</body></html>");
        return builder.toString();
    }

    @GetMapping("/admin/detail/{paymentId}")
    @ResponseBody
    public String paymentAdminDetailPage(@PathVariable("paymentId") String paymentId) {
        Payment payment = paymentService.getPayment(paymentId);
        return buildDetailHtml(payment, true);
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable("paymentId") String paymentId,
                                   @RequestParam("status") String status) {
        Payment payment = paymentService.getPayment(paymentId);
        paymentService.setStatus(payment, status);
        return "redirect:/payment/admin/detail/" + paymentId;
    }

    private String buildDetailHtml(Payment payment, boolean adminMode) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body><h2>Payment Detail</h2>")
                .append("<div>").append(payment.getId()).append("</div>")
                .append("<div>").append(payment.getMethod()).append("</div>")
                .append("<div>").append(payment.getStatus()).append("</div>");

        if (adminMode) {
            builder.append("<form method=\"post\" action=\"/payment/admin/set-status/")
                    .append(payment.getId())
                    .append("\"><button id=\"acceptButton\" name=\"status\" value=\"SUCCESS\" type=\"submit\">Accept</button>")
                    .append("<button id=\"rejectButton\" name=\"status\" value=\"REJECTED\" type=\"submit\">Reject</button></form>");
        }

        builder.append("</body></html>");
        return builder.toString();
    }
}