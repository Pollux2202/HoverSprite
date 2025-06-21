package com.example.demo.SprayOrder.Stripe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class StripeService {

    private final Gson gson = new Gson();


//    @Value("${stripe.return.url}")
//    private String returnUrl;

    public StripeService() {
        // Initialize Stripe API key
        Stripe.apiKey = "sk_test_51PuaxhP0Z87jNI1y6YOLl7MiOzrE05JiTVJNVpNAGu1bCa66qYqHX1zX3d41BANYH3FEYWG5EpvxvsqPrApnwk5b00RYSKr46T";
    }

    public JSONObject createCheckoutSession(long amount) throws StripeException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types[]", "card");
            params.put("line_items", Arrays.asList(
                    Map.of(
                            "price_data", Map.of(
                                    "currency", "vnd",
                                    "product_data", Map.of(
                                            "name", "Spraying Service"
                                    ),
                                    "unit_amount", amount
                            ),
                            "quantity", 1
                    )
            ));
            params.put("mode", "payment");
            params.put("success_url", "http://127.0.0.1:5500/EEET2580-Final-Project/FrontEnd/html/Sprite_Success.html");
            params.put("cancel_url", "http://127.0.0.1:5500/EEET2580-Final-Project/FrontEnd/html/Sprite_Failed.html");

            // Create the session
            Session session = Session.create(params);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("id", session.getId());
            jsonResponse.put("url", session.getUrl()); // Ensure this is the correct method
            return jsonResponse;
        } catch (StripeException e) {
            Logger.getLogger(StripeService.class.getName()).severe("Stripe error: " + e.getMessage());
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("error", "Failed to create checkout session.");
            return errorResponse;
        }
    }





//    public Map<String, String> getSessionStatus(String sessionId) throws Exception {
//        Session session = Session.retrieve(sessionId);
//
//        Map<String, String> statusMap = new HashMap<>();
//        statusMap.put("status", session.getStatus());
//        statusMap.put("customer_email", session.getCustomerDetails().getEmail());
//
//        return statusMap;
//    }
}
