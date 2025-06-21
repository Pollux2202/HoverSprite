package com.example.demo.SprayOrder.Stripe;

import com.example.demo.SprayOrder.SprayOrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class StripeWebHookController {

    @Autowired
    private SprayOrderService sprayOrderService;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        String endpointSecret = "webhook_secret_here";  // The secret from Stripe Dashboard

        try {
            // Verify the signature
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            // Process the event
            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getData().getObject();
                // Handle successful payment
                sprayOrderService.handlePaymentSuccess(session.getId());
            }

            return ResponseEntity.ok("Success");
        } catch (SignatureVerificationException e) {
            // Invalid signature
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }
    }


}
