package com.msy.wallet.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ReferenceIdGenerator {
    public String generateReferenceId() {
        Random rd = new Random();
        return Long.toString(10000000000L + (long) (rd.nextDouble() * 90000000000L));
    }
}
