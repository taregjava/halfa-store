package com.halfacode.util;

public class HalfaStoreUtility {

    public static float calculateDiscountPercent(float cost, float price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }

        if (cost < 0 || cost > price) {
            throw new IllegalArgumentException("Cost must be between zero and the price.");
        }

        return ((price - cost) / price) * 100;
    }
}
