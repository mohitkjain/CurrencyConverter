package com.jain.xcoin;

public interface XCoin {
    public double getConversionRate(String fromCurrencyCode, String toCurrencyCode) throws XCoinException;
}
