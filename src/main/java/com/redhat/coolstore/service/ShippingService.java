package com.redhat.coolstore.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/shipping")
@ApplicationScoped
public class ShippingService {

    @POST
    @Path("/calculateShipping")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public double calculateShipping(@QueryParam("cartTotal") double cartTotal) {

        if (cartTotal >= 0 && cartTotal < 25) {

            return 2.99;

        } else if (cartTotal >= 25 && cartTotal < 50) {

            return 4.99;

        } else if (cartTotal >= 50 && cartTotal < 75) {

            return 6.99;

        } else if (cartTotal >= 75 && cartTotal < 100) {

            return 8.99;

        } else if (cartTotal >= 100 && cartTotal < 10000) {

            return 10.99;

        }

        return 0;

    }

    @POST
    @Path("/calculateShippingInsurance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public double calculateShippingInsurance(@QueryParam("cartTotal") double cartTotal) {

        if (cartTotal >= 25 && cartTotal < 100) {

            return getPercentOfTotal(cartTotal, 0.02);

        } else if (cartTotal >= 100 && cartTotal < 500) {

            return getPercentOfTotal(cartTotal, 0.015);

        } else if (cartTotal >= 500 && cartTotal < 10000) {

            return getPercentOfTotal(cartTotal, 0.01);

        }

        return 0;
    }

    private static double getPercentOfTotal(double value, double percentOfTotal) {
        return BigDecimal.valueOf(value * percentOfTotal)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}