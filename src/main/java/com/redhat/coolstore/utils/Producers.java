package com.redhat.coolstore.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;


@ApplicationScoped
public class Producers {

    @Dependent
    public Logger produceLog(InjectionPoint injectionPoint) {
        // Return a logger specific to the class that is being injected into
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

}