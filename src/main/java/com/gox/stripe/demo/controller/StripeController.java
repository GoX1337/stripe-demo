package com.gox.stripe.demo.controller;

import com.gox.stripe.demo.dto.CreatePayment;
import com.gox.stripe.demo.dto.CreatePaymentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeController {

    @PostMapping("/create-payment-intent")
    public CreatePaymentResponse paymentIntent(@RequestBody CreatePayment createPayment) throws StripeException {
        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setCurrency("eur")
                .setAmount(666L)
                .build();
        // Create a PaymentIntent with the order amount and currency
        PaymentIntent intent = PaymentIntent.create(createParams);
        return new CreatePaymentResponse(intent.getClientSecret());
    }
}
