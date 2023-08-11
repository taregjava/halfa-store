package com.halfacode.dto;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class CheckoutInfo {
    private float productCost;
    private float productTotal;
    private float shippingCostTotal;
    private float paymentTotal;
    private int deliverDays;
    private boolean codSupported;


    public Date getDeliverDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, deliverDays);

        return calendar.getTime();
    }
}