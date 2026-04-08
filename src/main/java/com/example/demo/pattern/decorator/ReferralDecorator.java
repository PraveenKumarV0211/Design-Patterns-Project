package com.example.demo.pattern.decorator;

import com.example.demo.model.Application;

public class ReferralDecorator extends ApplicationDecorator {

    private final String referralInfo;

    public ReferralDecorator(Application wrapped, String referralInfo) {
        super(wrapped);
        this.referralInfo = referralInfo;
        wrapped.setReferralInfo(referralInfo);
    }

    @Override
    public String getDetails() {
        return "Referral: " + referralInfo;
    }
}
