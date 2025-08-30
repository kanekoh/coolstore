package com.redhat.coolstore.service;

import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.reactive.messaging.annotations.Broadcast;

import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.utils.Transformers;

@ApplicationScoped
public class ShoppingCartOrderProcessor  {

    @Inject
    Logger log;


    @Inject
    @Channel("orders")
    @Broadcast
    MutinyEmitter<String> ordersEmitter;

    
  
    public void  process(ShoppingCart cart) {
        String cartJson = Transformers.shoppingCartToJson(cart);
        log.info("Sending order from processor: " + cartJson);
        ordersEmitter.send(cartJson)
            .subscribe().with(
                success -> log.info("Order message sent successfully."),
                failure -> log.severe("Failed to send order message: " + failure.getMessage())
            );
    }



}