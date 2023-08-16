package com.halfacode.entity;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
public class CheckOutDtoInfo {

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
