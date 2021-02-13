package com.gox.stripe.demo.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    Logger log = LoggerFactory.getLogger(StripeWebhookController.class);

    @Value( "${stripe.webhook.key}" )
    private String endpointSecret;

    @PostMapping("/webhook")
    public void stripeWebHook(@RequestHeader("Stripe-Signature") String sigHeader, @RequestBody String payload){
        Event event = null;
        if(endpointSecret != null && sigHeader != null) {
            try {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            } catch (SignatureVerificationException e) {
                // Invalid signature
                log.error("⚠️  Webhook error while validating signature.");
                return;
            }
        }
        // Deserialize the nested object inside the event
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                log.info("Payment for {} succeeded.", paymentIntent.getAmount());
                // Then define and call a method to handle the successful payment intent.
                // handlePaymentIntentSucceeded(paymentIntent);
                break;
            case "payment_method.attached":
                PaymentMethod paymentMethod = (PaymentMethod) stripeObject;
                // Then define and call a method to handle the successful attachment of a PaymentMethod.
                // handlePaymentMethodAttached(paymentMethod);
                break;
            default:
                log.info("Unhandled event type: {}", event.getType());
                break;
        }
        return;
    }
}
