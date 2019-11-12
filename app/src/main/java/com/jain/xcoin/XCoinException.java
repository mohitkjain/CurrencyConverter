package com.jain.xcoin;

class XCoinException extends Exception {
    private static final long serialVersionUID = 7606608827802815021L;

    public XCoinException(String detailMessage) {
        super(detailMessage);
    }

    public XCoinException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
