package com.redhat.coolstore.service;

import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.utils.Transformers;

import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class ShoppingCartOrderProcessor  {

    @Inject
    Logger log;

    @Inject
    @Channel("orders")
    @Broadcast
    Emitter<String> orderEmitter;

    public void process(ShoppingCart cart) {
        log.info("Sending order from processor: ");
        orderEmitter.send(Transformers.shoppingCartToJson(cart));
    }
}